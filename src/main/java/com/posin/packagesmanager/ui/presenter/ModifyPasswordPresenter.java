package com.posin.packagesmanager.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.posin.packagesmanager.base.SpConfig;
import com.posin.packagesmanager.ui.contract.ModifyPasswordContract;
import com.posin.packagesmanager.utils.SPUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * FileName: ModifyPasswordPresenter
 * Author: Greetty
 * Time: 2018/10/12 8:46
 * Desc: TODO
 */
public class ModifyPasswordPresenter implements ModifyPasswordContract.IModifyPasswordPresenter {

    private static final String TAG = "ModifyPasswordPresenter";
    private Context mContext;
    private ModifyPasswordContract.IModifyPasswordView mModifyPasswordView;

    public ModifyPasswordPresenter(Context mContext,
                                   ModifyPasswordContract.IModifyPasswordView mModifyPasswordView) {
        this.mContext = mContext;
        this.mModifyPasswordView = mModifyPasswordView;
    }

    @Override
    public void modifyPassword(final String password) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observable) throws Exception {
                Log.e(TAG, "修改的密码为： " + password);
                SPUtil.put(mContext, SpConfig.MANAGER_PASSWORD, password);
                observable.onNext(true);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        if (aBoolean)
                            this.onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        mModifyPasswordView.modifyPasswordFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mModifyPasswordView.modifyPasswordSuccess();
                    }
                });
    }

    @Override
    public void cancelModifyPassword() {
        mModifyPasswordView.cancelModifyPassword();
    }
}
