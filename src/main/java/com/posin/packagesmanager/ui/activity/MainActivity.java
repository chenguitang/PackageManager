package com.posin.packagesmanager.ui.activity;

import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.posin.packagesmanager.R;
import com.posin.packagesmanager.base.BaseActivity;
import com.posin.packagesmanager.ui.contract.ComparePasswordContract;
import com.posin.packagesmanager.ui.dialog.ComparePasswordDialog;
import com.posin.packagesmanager.ui.dialog.ModifyPasswordDialog;
import com.posin.packagesmanager.ui.presenter.ComparePasswordPresenter;
import com.posin.packagesmanager.view.SwitchButton;

import java.lang.reflect.Method;

public class MainActivity extends BaseActivity implements ComparePasswordContract.IComparePasswordView {

    private ComparePasswordDialog comparePasswordDialog;

    private ComparePasswordPresenter mComparePasswordPresenter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setLogo(R.mipmap.icon_app_model2);
        mCommonToolbar.setTitle("用户模式");
    }

    /**
     * 加载菜单按钮
     *
     * @param menu Menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 菜单按钮Item点击事件
     *
     * @param item 菜单Item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.switch_model:
                Toast.makeText(mContext, "正在开发中 ....", Toast.LENGTH_SHORT).show();
                break;
            case R.id.modify_password:

                ModifyPasswordDialog modifyPasswordDialog = new ModifyPasswordDialog(this, "修改登录密码");
                modifyPasswordDialog.show();


                break;
            case R.id.system_exit:
                MainActivity.this.finish();
                System.exit(0);
                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示菜单Item中的图片
     *
     * @param featureId 图片ID
     * @param view      View
     * @param menu      Menu
     * @return boolean
     */
    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",
                            Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPreparePanel(featureId, view, menu);
    }

    @Override
    public void initData() {
        mComparePasswordPresenter = new ComparePasswordPresenter(this);
        comparePasswordDialog = new ComparePasswordDialog(this, "登录App管理器", this);
        comparePasswordDialog.show();

    }

    @Override
    public void compareSuccess() {
        if (comparePasswordDialog != null) {
            if (comparePasswordDialog.isShowing()) {
                comparePasswordDialog.dismiss();
            }
        }
    }

    @Override
    public void compareFailure(String errorMessage) {
//        Toast.makeText(mContext, "密码有误，请重新输入密码！", Toast.LENGTH_SHORT).show();
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cancelCompare() {
        MainActivity.this.finish();
        System.exit(0);
    }

    @Override
    public void showProgress() {
        showLoadingDialog("加载中");
    }

    @Override
    public void dismissProgress() {
        dismissLoadingDialog();
    }
}
