package com.posin.packagesmanager.ui.contract;

import android.content.Context;

import com.posin.packagesmanager.base.BaseContract;
import com.posin.packagesmanager.bean.AppInfo;
import com.posin.packagesmanager.bean.PackagesMessage;

import java.util.List;

/**
 * FileName: SaveAppConfigContract
 * Author: Greetty
 * Time: 2018/10/15 9:19
 * Desc: TODO
 */
public interface LoadAppConfigContract {

    interface ILoadAppConfigView extends BaseContract.IBaseView {

        /**
         * 保存成功
         */
        void loadConfigSuccess(boolean isUserModel, List<PackagesMessage.DisabledBean> listPackages);

        /**
         * 保存失败
         *
         * @param errorMessage
         */
        void loadConfigFailure(String errorMessage);
    }

    interface ILoadAppConfigPresenter extends BaseContract.IBasePresenter {


        /**
         * 读取应用记录APP的配置文件
         *
         * @param context context
         * @param path    文件路径
         * @return List<AppInfo>
         */
        void readConfig(Context context, String path);
    }
}
