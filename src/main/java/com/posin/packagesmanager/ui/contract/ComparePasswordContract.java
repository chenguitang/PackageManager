package com.posin.packagesmanager.ui.contract;

import com.posin.packagesmanager.base.BaseContract;

/**
 * FileName: ComparePasswordContract
 * Author: Greetty
 * Time: 2018/10/11 15:14
 * Desc: TODO
 */
public interface ComparePasswordContract {

    interface IComparePasswordView extends BaseContract.IBaseView {
        /**
         * 密码校验成功
         */
        void compareSuccess();

        /**
         * 密码校验失败
         */
        void compareFailure(String errorMessage);

        /**
         * 取消检验密码
         */
        void cancelCompare();
    }

    interface IComparePasswordPresenter extends BaseContract.IBasePresenter {
        /**
         * 检验密码
         *
         * @param password 密码
         */
        void comparePassword(String password);

        /**
         * 取消校验密码
         */
        void cancelCompare();
    }
}
