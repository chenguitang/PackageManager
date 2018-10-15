package com.posin.packagesmanager.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.posin.packagesmanager.bean.AppInfo;
import com.posin.packagesmanager.bean.PackagesMessage;
import com.posin.packagesmanager.ui.contract.SaveAppConfigContract;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * FileName: SaveAppConfigPresenter
 * Author: Greetty
 * Time: 2018/10/15 9:22
 * Desc: TODO
 */
public class SaveAppConfigPresenter implements SaveAppConfigContract.ISaveConfigPresenter {

    private static final String TAG = "SaveAppConfigPresenter";
    private Context mContext;
    private SaveAppConfigContract.ISaveConfigView saveConfigView;

    public SaveAppConfigPresenter(Context context, SaveAppConfigContract.ISaveConfigView saveConfigView) {
        this.mContext = context;
        this.saveConfigView = saveConfigView;
    }

    @Override
    public void saveConfig(Context context, final String path, final boolean isUserModel,
                           final List<AppInfo> listAppInfo) {

        saveConfigView.showProgress();
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observable) throws Exception {

                Gson gson = new Gson();
                List<PackagesMessage.DisabledBean> listDisableApp = new ArrayList<>();
                String model = isUserModel ? "user" : "admin";
                for (AppInfo appInfo : listAppInfo) {
                    if (appInfo.ismHideOnUserMode()) {
                        PackagesMessage.DisabledBean disabledApp = new PackagesMessage.
                                DisabledBean(appInfo.getPackageName(), appInfo.getClassName());
                        listDisableApp.add(disabledApp);
                    }
                }
                PackagesMessage disablePackagesMessage = new PackagesMessage(model, listDisableApp);
                String disablePackagesJson = gson.toJson(disablePackagesMessage);

                FileOutputStream fos = new FileOutputStream(path);
                fos.write(disablePackagesJson.getBytes("utf-8"));
                fos.flush();
                fos.close();

                Log.e(TAG, "disablePackagesJson: " + disablePackagesJson);
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
                        this.onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        saveConfigView.dismissProgress();
                        saveConfigView.saveFailure(e.toString());

                    }

                    @Override
                    public void onComplete() {
                        saveConfigView.dismissProgress();
                        saveConfigView.saveSuccess();
                    }
                });
    }

}
