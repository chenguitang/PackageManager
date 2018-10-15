package com.posin.packagesmanager.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.posin.packagesmanager.R;
import com.posin.packagesmanager.view.LoadingDialog;

import butterknife.ButterKnife;

/**
 * FileName: BaseActivity
 * Author: Greetty
 * Time: 2018/10/11 10:10
 * Desc: Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    private LoadingDialog mLoadingDialog;
    public Toolbar mCommonToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSavedInstanceState(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        ButterKnife.bind(this);

        mCommonToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        if (mCommonToolbar != null) {
            initToolBar();
            setSupportActionBar(mCommonToolbar);
        }

        initUI();
        initData();
    }

    /**
     * 显示加载进度框
     *
     * @param title String
     */
    public void showLoadingDialog(String title) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, title);
            mLoadingDialog.show();
        } else {
            if (!mLoadingDialog.isShowing()) {
                mLoadingDialog.setTitle(title);
                mLoadingDialog.show();
            } else {
                mLoadingDialog.setTitle(title);
            }
        }
    }

    /**
     * 隐藏加载进度框
     */
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        }
    }

    public abstract int getLayoutId();

    public abstract void initUI();

    public abstract void initData();

    public abstract void initToolBar();

    public void initSavedInstanceState(Bundle savedInstanceState) {
    }

}
