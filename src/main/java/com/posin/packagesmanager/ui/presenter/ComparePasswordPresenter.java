package com.posin.packagesmanager.ui.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.posin.packagesmanager.R;
import com.posin.packagesmanager.base.SpConfig;
import com.posin.packagesmanager.ui.contract.ComparePasswordContract;
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
 * FileName: ComparePasswordPresenter
 * Author: Greetty
 * Time: 2018/10/11 15:18
 * Desc: TODO
 */
public class ComparePasswordPresenter implements ComparePasswordContract.IComparePasswordPresenter {

    private static final String TAG = "PasswordPresenter";
    private ComparePasswordContract.IComparePasswordView mComparePasswordView;
    private Context mContext;

    public ComparePasswordPresenter(ComparePasswordContract.IComparePasswordView
                                            mComparePasswordView, Context context) {
        this.mComparePasswordView = mComparePasswordView;
        this.mContext = context;
    }

    @Override
    public void comparePassword(final String password) {

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observable) throws Exception {

                String savePassword = (String) SPUtil.get(mContext, SpConfig.MANAGER_PASSWORD, "1234");
//                Log.e(TAG, "savePassword: " + savePassword);
                if (TextUtils.isEmpty(savePassword)) {
                    savePassword = "1234";
                }
                if (!password.equals(savePassword)) {
                    observable.onError(new Exception(
                            mContext.getString(R.string.password_error_input_again)));
                } else {
                    observable.onNext(true);
                }
            }
        }).subscribeOn(Schedulers.io())
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
                    public void onError(@NonNull Throwable throwable) {
                        throwable.printStackTrace();
                        mComparePasswordView.compareFailure(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mComparePasswordView.compareSuccess();
                    }
                });
    }

    @Override
    public void cancelCompare() {
        mComparePasswordView.cancelCompare();
    }
}
