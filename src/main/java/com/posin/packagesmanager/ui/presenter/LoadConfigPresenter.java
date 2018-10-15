package com.posin.packagesmanager.ui.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.primitives.Booleans;
import com.google.gson.Gson;
import com.posin.packagesmanager.bean.AppInfo;
import com.posin.packagesmanager.bean.PackagesMessage;
import com.posin.packagesmanager.ui.contract.LoadAppConfigContract;
import com.posin.packagesmanager.utils.shell.SuShell;

import java.io.File;
import java.io.FileInputStream;
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
 * FileName: LoadConfigPresenter
 * Author: Greetty
 * Time: 2018/10/15 10:09
 * Desc: TODO
 */
public class LoadConfigPresenter implements LoadAppConfigContract.ILoadAppConfigPresenter {

    private static final String TAG = "LoadConfigPresenter";
    private Context context;
    private LoadAppConfigContract.ILoadAppConfigView loadAppConfigView;
    private List<PackagesMessage.DisabledBean> listPackages;
    private boolean isUserModel = true;

    public LoadConfigPresenter(Context context, LoadAppConfigContract.ILoadAppConfigView
            loadAppConfigView) {
        this.context = context;
        this.loadAppConfigView = loadAppConfigView;
        listPackages = new ArrayList<>();
    }

    @Override
    public void readConfig(Context context, final String path) {

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observable) throws Exception {
                File file = new File(path);
                //判断文件是否存在
                if (!file.exists()) {
                    observable.onNext(true);
                    return;
                }

                //修改文件权限
                if (!file.canRead() || !file.canWrite()) {
                    SuShell.exec("chmod 777 " + file.getAbsolutePath(), null, 10000);
                }

                FileInputStream fis = new FileInputStream(file);

                byte[] data = new byte[(int) file.length()];
                int size = 0;
                while (size < data.length) {
                    int rd = fis.read(data);
                    if (rd > 0)
                        size += rd;
                    else
                        break;
                }

                String disablePackages = new String(data, "utf-8");

                Log.e(TAG, "disablePackages: " + disablePackages);

                PackagesMessage packagesMessage = new Gson().fromJson(disablePackages, PackagesMessage.class);
                if (!TextUtils.isEmpty(packagesMessage.getModel()))
                    isUserModel = packagesMessage.getModel().equals("user");

                for (PackagesMessage.DisabledBean disabledBean : packagesMessage.getDisabled()) {
                    listPackages.add(disabledBean);
                }
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
                        loadAppConfigView.dismissProgress();
                        loadAppConfigView.loadConfigFailure(e.toString());
                        Log.e(TAG, "======================================================");
                        Log.e(TAG, "数据加载失败....");
                        Log.e(TAG, "======================================================");
                    }

                    @Override
                    public void onComplete() {
                        loadAppConfigView.dismissProgress();
                        loadAppConfigView.loadConfigSuccess(isUserModel, listPackages);
                    }
                });
    }
}
