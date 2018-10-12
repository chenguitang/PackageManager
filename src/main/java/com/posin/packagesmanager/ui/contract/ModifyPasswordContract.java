package com.posin.packagesmanager.ui.contract;

import com.posin.packagesmanager.base.BaseContract;

/**
 * FileName: ModifyPasswordContract
 * Author: Greetty
 * Time: 2018/10/12 8:42
 * Desc: TODO
 */
public interface ModifyPasswordContract {

    interface IModifyPasswordView extends BaseContract.BaseView {
        /**
         * 密码修改成功
         */
        void modifyPasswordSuccess();

        /**
         * 密码修改失败
         */
        void modifyPasswordFailure(String errorMessage);

        /**
         * 取消修改密码
         */
        void cancelModifyPassword();
    }

    interface IModifyPasswordPresenter extends BaseContract.BasePresenter {

        /**
         * 修改密码
         */
        void modifyPassword(String password);


        /**
         * 取消修改密码
         */
        void cancelModifyPassword();
    }

}
