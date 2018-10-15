package com.posin.packagesmanager.ui.contract;

import com.posin.packagesmanager.base.BaseContract;
import com.posin.packagesmanager.bean.AppInfo;

import java.util.List;

/**
 * FileName: HomeContract
 * Author: Greetty
 * Time: 2018/10/15 11:06
 * Desc: TODO
 */
public interface HomeContract {

    interface IHomeView extends BaseContract.IBaseView {

        void loadAllAppSuccess(List<AppInfo> listApps);

        void loadAllAppFailure(String errorMessage);

        void switchModelSuccess(boolean isUserModel);

        void switchModelFailure(String errorMessage);
    }

    interface IHomePresenter extends BaseContract.IBasePresenter {
        /**
         * 加载所有应用数据
         */
        void loadAllApp();

        /**
         * 切换使用模式
         *
         * @param isUserModel 是否为用户模式
         * @param listApps    APP应用数据
         * @param path        保存文件的路径
         */
        void switchModel(boolean isUserModel, List<AppInfo> listApps, String path);
    }

}
