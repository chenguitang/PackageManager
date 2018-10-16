package com.posin.packagesmanager.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.posin.packagesmanager.R;
import com.posin.packagesmanager.base.PackagesConfig;
import com.posin.packagesmanager.bean.AppInfo;
import com.posin.packagesmanager.bean.PackagesMessage;
import com.posin.packagesmanager.ui.contract.HomeContract;
import com.posin.packagesmanager.utils.AppStateUtils;
import com.posin.packagesmanager.utils.AppUtils;

import java.io.FileOutputStream;
import java.util.ArrayList;
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
 * FileName: HomePresenter
 * Author: Greetty
 * Time: 2018/10/15 11:08
 * Desc: TODO
 */
public class HomePresenter implements HomeContract.IHomePresenter {

    private static final String TAG = "HomePresenter";
    private Context context;
    private HomeContract.IHomeView homeView;

    private List<AppInfo> listApps;

    public HomePresenter(Context context, HomeContract.IHomeView homeView) {
        this.context = context;
        this.homeView = homeView;
    }

    @Override
    public void loadAllApp() {
        homeView.showProgress();
        Observable.create(new ObservableOnSubscribe<List<AppInfo>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<AppInfo>> observable) throws Exception {
                List<AppInfo> listApp = new AppUtils().getAllApp(context);
//                Thread.sleep(1000);
                observable.onNext(listApp);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AppInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<AppInfo> listAppInfo) {
                        homeView.loadAllAppSuccess(listAppInfo);
                        this.onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        homeView.dismissProgress();
                        homeView.loadAllAppFailure(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        homeView.dismissProgress();
                    }
                });


    }

    @Override
    public void switchModel(final boolean isUserModel, final List<AppInfo> listApps, final String path) {

        homeView.showProgress();
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Boolean> switchObservable) throws Exception {

                Gson gson = new Gson();
                List<PackagesMessage.DisabledBean> listDisableApp = new ArrayList<>();
                String model = isUserModel ? "user" : "admin";
                for (AppInfo appInfo : listApps) {
                    if (!appInfo.isShowOnUserMode()) {
                        PackagesMessage.DisabledBean disabledApp = new PackagesMessage.
                                DisabledBean(appInfo.getPackageName(), appInfo.getClassName());
                        listDisableApp.add(disabledApp);

                        AppStateUtils.setPackageEnable(context, appInfo.getPackageName(),
                                appInfo.getClassName(), !isUserModel);
                    }
                }
                PackagesMessage disablePackagesMessage = new PackagesMessage(model, listDisableApp);
                String disablePackagesJson = gson.toJson(disablePackagesMessage);

                FileOutputStream fos = new FileOutputStream(path);
                fos.write(disablePackagesJson.getBytes("utf-8"));
                fos.flush();
                fos.close();

                Log.d(TAG, "disablePackagesJson: " + disablePackagesJson);

                Observable.timer(6, TimeUnit.SECONDS)
                        .doOnNext(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                Observable.create(new ObservableOnSubscribe<Boolean>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<Boolean>
                                                                  checkObservable) throws Exception {
                                        for (AppInfo appInfo : listApps) {
                                            if (!appInfo.isShowOnUserMode()) {

                                                int packageState = AppStateUtils.getPackageState(context,
                                                        appInfo.getPackageName(), appInfo.getClassName());

                                                boolean visible = AppStateUtils.isVisible(packageState);

                                                boolean userModel = PackagesConfig.getUserModel();

//                                                Log.e(TAG, "ClassName: " + appInfo.getClassName() +
//                                                        "  visible: " + visible + "   userModel: " + userModel);

                                                if (!userModel && visible) {
                                                    Log.e(TAG, "修改失败");
                                                    checkObservable.onError(
                                                            new Exception(context.getString(
                                                                    R.string.switch_model_failure)));
                                                    return;
                                                } else if (userModel && !visible) {
                                                    Log.e(TAG, "修改失败");
                                                    checkObservable.onError(
                                                            new Exception(context.getString(
                                                                    R.string.switch_model_failure)));
                                                    return;
                                                }
                                            }
                                        }
                                        checkObservable.onNext(true);
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
                                                switchObservable.onError(e);
                                            }

                                            @Override
                                            public void onComplete() {
                                                switchObservable.onNext(true);
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
                        homeView.dismissProgress();
                        homeView.switchModelFailure(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        homeView.dismissProgress();
                        homeView.switchModelSuccess(isUserModel);
                    }
                });
    }
}
