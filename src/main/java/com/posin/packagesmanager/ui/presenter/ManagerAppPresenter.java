package com.posin.packagesmanager.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.posin.packagesmanager.base.PackagesConfig;
import com.posin.packagesmanager.bean.AppInfo;
import com.posin.packagesmanager.ui.contract.ManagerAppContract;
import com.posin.packagesmanager.utils.AppStateUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * FileName: ManagerAppPresenter
 * Author: Greetty
 * Time: 2018/10/13 13:04
 * Desc: TODO
 */
public class ManagerAppPresenter implements ManagerAppContract.IManagerAppPresenter {

    private static final String TAG = "ManagerAppPresenter";

    private Context context;
    private int mState;
    private boolean isVisAble;
    private boolean isUserModel;

    private ManagerAppContract.IManagerAppView mManagerAppView;

    public ManagerAppPresenter(Context context, ManagerAppContract.IManagerAppView
            mManagerAppView) {
        this.context = context;
        this.mManagerAppView = mManagerAppView;
    }

    @Override
    public void managerApp(final Context context, final String className,
                           final String packageName, final boolean isAble) {

        mManagerAppView.showProgress();
        isUserModel = PackagesConfig.getUserModel();
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Boolean>
                                          managerObservable) throws Exception {

                if (isUserModel) {
                    AppStateUtils.setPackageEnable(context, packageName, className, isAble);
                    mState = AppStateUtils.getPackageState(context, packageName, className);
                    isVisAble = AppStateUtils.isVisible(mState);
                }
                Observable.timer(isUserModel ? 6 : 2, TimeUnit.SECONDS)
                        .doOnNext(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                //查询应用状态是否已修改
                                Observable.create(new ObservableOnSubscribe<Boolean>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<Boolean>
                                                                  checkObservable) throws Exception {
                                        int packageState = AppStateUtils.getPackageState(context,
                                                packageName, className);
                                        boolean visible = AppStateUtils.isVisible(packageState);
                                        Log.e(TAG, "visible: " + visible);
                                        isVisAble = visible;
                                        mState = AppStateUtils.getPackageState(context, packageName, className);

                                        if (visible != isAble && isUserModel) {
                                            Log.e(TAG, "修改失败...");
                                            checkObservable.onError(new Exception("修改应用状态失败."));
                                        } else {
                                            checkObservable.onNext(true);


                                        }
                                    }
                                }).subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<Boolean>() {
                                            @Override
                                            public void onSubscribe(@NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onNext(@NonNull Boolean aBoolean) {
                                                this.onComplete();
                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {
                                                managerObservable.onError(e);
                                            }

                                            @Override
                                            public void onComplete() {
                                                managerObservable.onNext(true);
                                            }
                                        });

                            }
                        }).subscribe();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        this.onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        mManagerAppView.dismissProgress();
                        mManagerAppView.managerFailure(isVisAble, mState, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mManagerAppView.dismissProgress();
                        mManagerAppView.managerSuccess(isVisAble, mState);
                    }
                });
    }
}
