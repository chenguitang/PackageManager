package com.posin.packagesmanager.ui.contract;

import android.content.Context;

import com.posin.packagesmanager.base.BaseContract;

/**
 * FileName: ManagerAppContract
 * Author: Greetty
 * Time: 2018/10/13 12:41
 * Desc: TODO
 */
public interface ManagerAppContract {

    interface IManagerAppView extends BaseContract.BaseView {

        /**
         * 操作成功
         */
        void managerSuccess(boolean isVisible, int state);

        /**
         * 操作失败
         *
         * @param errorMessage 错误信息
         */
        void managerFailure(boolean isVisible, int state, String errorMessage);
    }

    interface IManagerAppPresenter extends BaseContract.BasePresenter {

        /**
         * 操作应用图标是否显示
         *
         * @param context
         * @param className
         * @param packageName
         * @param isAble
         */
        void managerApp(Context context, String className, String packageName, boolean isAble);

    }

}
