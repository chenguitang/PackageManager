package com.posin.packagesmanager.ui.activity;

import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.posin.packagesmanager.R;
import com.posin.packagesmanager.base.BaseActivity;
import com.posin.packagesmanager.base.PackagesConfig;
import com.posin.packagesmanager.bean.AppInfo;
import com.posin.packagesmanager.bean.PackagesMessage;
import com.posin.packagesmanager.ui.adapter.AllAppAdapter;
import com.posin.packagesmanager.ui.contract.ComparePasswordContract;
import com.posin.packagesmanager.ui.contract.HomeContract;
import com.posin.packagesmanager.ui.contract.LoadAppConfigContract;
import com.posin.packagesmanager.ui.contract.ModifyPasswordContract;
import com.posin.packagesmanager.ui.dialog.ComparePasswordDialog;
import com.posin.packagesmanager.ui.dialog.ModifyPasswordDialog;
import com.posin.packagesmanager.ui.presenter.HomePresenter;
import com.posin.packagesmanager.ui.presenter.LoadConfigPresenter;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements
        ComparePasswordContract.IComparePasswordView, ModifyPasswordContract.IModifyPasswordView,
        LoadAppConfigContract.ILoadAppConfigView, HomeContract.IHomeView {

    private static final String TAG = "MainActivity";

    private ComparePasswordDialog comparePasswordDialog;
    private ModifyPasswordDialog modifyPasswordDialog;
    private HomePresenter homePresenter;
    private LoadConfigPresenter loadConfigPresenter;
    private AllAppAdapter allAppAdapter;

    private List<PackagesMessage.DisabledBean> listDisableApp;
    private boolean mIsUserModel;
    private List<AppInfo> listApps;


    @BindView(R.id.rv_list_app)
    RecyclerView rvListApp;

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
//                Toast.makeText(mContext, "正在开发中 ....", Toast.LENGTH_SHORT).show();
                if (allAppAdapter != null) {
                    homePresenter.switchModel(!mIsUserModel, allAppAdapter.getListData(),
                            PackagesConfig.Disable_APP_CONFIG_FILE);
                } else {
                    Toast.makeText(mContext, "数据有误,请关闭应用重新打开", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.modify_password:
                modifyPasswordDialog = new ModifyPasswordDialog(this, "修改登录密码", this);
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
    public void initUI() {

        rvListApp.setLayoutManager(new LinearLayoutManager(this));
        rvListApp.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void initData() {


        comparePasswordDialog = new ComparePasswordDialog(this, "登录App管理器", this);
        comparePasswordDialog.show();

        homePresenter = new HomePresenter(this, this);
        loadConfigPresenter = new LoadConfigPresenter(this, this);

        loadConfigPresenter.readConfig(this, PackagesConfig.Disable_APP_CONFIG_FILE);

    }

    @Override
    public void compareSuccess() {
        if (comparePasswordDialog != null) {
            if (comparePasswordDialog.isShowing()) {
                comparePasswordDialog.dismiss();
            }
        }
        Log.d(TAG, "密码检验成功，正在加载数据.");
        homePresenter.loadAllApp();
    }

    @Override
    public void compareFailure(String errorMessage) {
//        Toast.makeText(mContext, "密码有误，请重新输入密码！", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "密码错误");
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cancelCompare() {
        Log.d(TAG, "退出应用");
        MainActivity.this.finish();
        System.exit(0);
    }

    @Override
    public void showProgress() {
        showLoadingDialog("正在拼命加载中");
    }

    @Override
    public void dismissProgress() {
        dismissLoadingDialog();
    }

    @Override
    public void modifyPasswordSuccess() {
        if (modifyPasswordDialog != null) {
            if (modifyPasswordDialog.isShowing()) {
                modifyPasswordDialog.dismiss();
            }
        }
    }

    @Override
    public void modifyPasswordFailure(String errorMessage) {
        Log.d(TAG, "密码修改失败");
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cancelModifyPassword() {
        Log.d(TAG, "取消修改密码");
        Toast.makeText(mContext, "您已取消修改密码", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadAllAppSuccess(List<AppInfo> listApps) {
        Log.d(TAG, "数据加载成功，正在刷新应用列表.");

        for (int i = 0; i < listApps.size(); i++) {
            if (listDisableApp == null || listDisableApp.size() <= 0) {
                break;
            }
            for (PackagesMessage.DisabledBean disabledApp : listDisableApp) {
                if (disabledApp.getPackageName().equals(listApps.get(i).getPackageName()) &&
                        disabledApp.getClassName().equals(listApps.get(i).getClassName())) {
                    listApps.get(i).setmHideOnUserMode(true);
                    Log.e(TAG, "package name: " + disabledApp.getPackageName());
                }
            }
        }
        this.listApps = listApps;
        allAppAdapter = new AllAppAdapter(this, listApps);
        rvListApp.setAdapter(allAppAdapter);
    }

    @Override
    public void loadAllAppFailure(String errorMessage) {
        Log.d(TAG, "数据加载失败，无法加载应用列表");
        Toast.makeText(mContext, "加载失败，请退出应用,再重新打开App管理器: " + errorMessage,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void switchModelSuccess(boolean isUserModel) {
        mCommonToolbar.setTitle(isUserModel ? "用户模式" : "管理员模式");
        this.mIsUserModel = isUserModel;
        Toast.makeText(mContext, "切换成功，当前使用模式为：" + (isUserModel ? "用户模式" : "管理员模式"),
                Toast.LENGTH_SHORT).show();
        Log.d(TAG, "模式切换成功，当前使用模式为：" + (isUserModel ? "用户模式" : "管理员模式"));
        PackagesConfig.setUserModel(isUserModel);
    }

    @Override
    public void switchModelFailure(String errorMessage) {
        Log.d(TAG, "切换模式失败");
        Toast.makeText(mContext, "切换模式失败: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadConfigSuccess(boolean isUserModel, List<PackagesMessage.DisabledBean> listPackages) {
        listDisableApp = listPackages;
        this.mIsUserModel = isUserModel;
        PackagesConfig.setUserModel(isUserModel);
        Log.d(TAG, "isUserModel: " + isUserModel);
        Log.d(TAG, "加载应用保存的配置文件成功");
        mCommonToolbar.setTitle(isUserModel ? "用户模式" : "管理员模式");
//        for (PackagesMessage.DisabledBean disableApp : listPackages) {
//            Log.e(TAG, "packagesMessage: " + disableApp.toString());
//        }
    }

    @Override
    public void loadConfigFailure(String errorMessage) {
        Toast.makeText(mContext, "数据加载失败： " + errorMessage, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "加载应用保存的配置文件成功");
    }

}
