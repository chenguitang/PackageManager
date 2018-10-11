package com.posin.packagesmanager.ui.presenter;

import android.util.Log;

import com.posin.packagesmanager.ui.contract.ComparePasswordContract;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * FileName: ComparePasswordPresenter
 * Author: Greetty
 * Time: 2018/10/11 15:18
 * Desc: TODO
 */
public class ComparePasswordPresenter implements ComparePasswordContract.IComparePasswordPresenter {

    private static final String TAG = "PasswordPresenter";
    private ComparePasswordContract.IComparePasswordView mComparePasswordView;

    public ComparePasswordPresenter(ComparePasswordContract.IComparePasswordView mComparePasswordView) {
        this.mComparePasswordView = mComparePasswordView;
    }

    @Override
    public void comparePassword(final String password) {

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observable) throws Exception {
                if (!password.equals("1234")) {
                    observable.onError(new Exception("密码错误,请重新输入密码!"));
                }else{
                    observable.onNext(true);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        Log.e(TAG, "onNext");
                        this.onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Log.e(TAG, "onError");
                        mComparePasswordView.compareFailure(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                        mComparePasswordView.compareSuccess();
                    }
                });
    }

    @Override
    public void cancelCompare() {
        mComparePasswordView.cancelCompare();
    }
}
