package com.posin.packagesmanager.ui.contract;

import android.content.Context;

import com.posin.packagesmanager.base.BaseContract;
import com.posin.packagesmanager.bean.AppInfo;

import java.util.List;

/**
 * FileName: SaveAppConfigContract
 * Author: Greetty
 * Time: 2018/10/15 9:19
 * Desc: TODO
 */
public interface SaveAppConfigContract {

    interface ISaveConfigView extends BaseContract.IBaseView {

        /**
         * 保存成功
         */
        void saveSuccess();

        /**
         * 保存失败
         *
         * @param errorMessage
         */
        void saveFailure(String errorMessage);
    }

    interface ISaveConfigPresenter extends BaseContract.IBasePresenter {

        /**
         * 保存修改
         *
         * @param context     context
         * @param path        保存路径
         * @param isUserModel 是否为用户模式
         * @param listAppInfo 应用数据
         */
        void saveConfig(Context context, String path, boolean isUserModel,
                        List<AppInfo> listAppInfo);
    }
}
