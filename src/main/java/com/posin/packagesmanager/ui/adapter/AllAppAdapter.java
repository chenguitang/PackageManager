package com.posin.packagesmanager.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.posin.packagesmanager.R;
import com.posin.packagesmanager.base.PackagesConfig;
import com.posin.packagesmanager.bean.AppInfo;
import com.posin.packagesmanager.ui.contract.SaveAppConfigContract;
import com.posin.packagesmanager.ui.contract.ManagerAppContract;
import com.posin.packagesmanager.ui.presenter.SaveAppConfigPresenter;
import com.posin.packagesmanager.ui.presenter.ManagerAppPresenter;
import com.posin.packagesmanager.view.LoadingDialog;
import com.posin.packagesmanager.view.SwitchButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FileName: AllAppAdapter
 * Author: Greetty
 * Time: 2018/10/12 16:13
 * Desc: TODO
 */
public class AllAppAdapter extends RecyclerView.Adapter<AllAppAdapter.AppItemMessageHolder> implements ManagerAppContract.IManagerAppView, SaveAppConfigContract.ISaveConfigView {

    private static final String TAG = "AllAppAdapter";
    private Context context;
    private List<AppInfo> listAppInfo;
    private ManagerAppPresenter managerAppPresenter;
    private SaveAppConfigPresenter saveAppConfigPresenter;
    private LoadingDialog mLoadingDialog;
    private int operationPosition;
    private AppItemMessageHolder mHandler;

    public AllAppAdapter(Context context, List<AppInfo> listAppInfo) {
        this.context = context;
        this.listAppInfo = listAppInfo;
        managerAppPresenter = new ManagerAppPresenter(context, this);
        saveAppConfigPresenter = new SaveAppConfigPresenter(context, this);
        mLoadingDialog = new LoadingDialog(context, "正在拼命加载中");
    }

    @Override
    public AppItemMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_packages_list, parent, false);
        return new AllAppAdapter.AppItemMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(final AppItemMessageHolder holder, final int position) {

        holder.setIsRecyclable(false);

        final AppInfo appInfo = listAppInfo.get(position);
        holder.ivAppIcon.setImageDrawable(appInfo.getAppIcon());
        holder.tvAppName.setText(appInfo.getAppName());
        holder.tvAppDesc.setText(appInfo.getPackageName() + " / " + appInfo.getClassName() +
                " /UID=" + appInfo.getAppUId());
        holder.sbSwitchApp.setChecked(appInfo.ismHideOnUserMode());
        holder.sbSwitchApp.setChecked(listAppInfo.get(position).ismHideOnUserMode());

        holder.sbSwitchApp.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                Log.e(TAG, "position： " + position + "   isChecked: " + isChecked);
                listAppInfo.get(position).setmHideOnUserMode(isChecked);
            }
        });

        holder.sbSwitchApp.setOnClickChangeListener(new SwitchButton.onClickChangerListener() {
            @Override
            public void onClickChange(SwitchButton view, boolean isChecked) {
                managerAppPresenter.managerApp(context, listAppInfo.get(position).getClassName(),
                        listAppInfo.get(position).getPackageName(), !isChecked);
                operationPosition = position;
                mHandler = holder;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAppInfo.size();
    }

    @Override
    public void managerSuccess(boolean isVisible, int state) {
//        Toast.makeText(context, "修改成功.", Toast.LENGTH_SHORT).show();
//        Log.e(TAG, "isVisible: " + isVisible + "  operationPosition:" + operationPosition);
//        Log.e(TAG, "修改成功11： " + listAppInfo.get(operationPosition).toString());
        if (listAppInfo.size() > operationPosition) {
            if (PackagesConfig.getUserModel()) {
                //用户模式下，打开隐藏app的开关，isVisible返回的是false,图标已被隐藏
                //管理员模式下，打开隐藏app的开关，isVisible返回的是true,图标未被隐藏，只有切换到用户模式才会隐藏
                listAppInfo.get(operationPosition).setmHideOnUserMode(!isVisible);
            }
            listAppInfo.get(operationPosition).setmState(state);
        }
//        Log.e(TAG, "修改成功22： " + listAppInfo.get(operationPosition).toString());

        saveAppConfigPresenter.saveConfig(context,
                PackagesConfig.Disable_APP_CONFIG_FILE, PackagesConfig.getUserModel(), listAppInfo);
    }

    @Override
    public void managerFailure(boolean isVisible, int state, String errorMessage) {
        Toast.makeText(context, "修改失败，请重新尝试.", Toast.LENGTH_SHORT).show();
        if (listAppInfo.size() > operationPosition) {
            listAppInfo.get(operationPosition).setmHideOnUserMode(!isVisible);
            listAppInfo.get(operationPosition).setmState(state);
        }

        if (mHandler != null) {
            mHandler.sbSwitchApp.setChecked(!isVisible);
        }
    }


    @Override
    public void showProgress() {
        mLoadingDialog.show();
    }

    @Override
    public void dismissProgress() {
        if (mLoadingDialog != null)
            if (mLoadingDialog.isShowing())
                mLoadingDialog.dismiss();
    }

    @Override
    public void saveSuccess() {
        Toast.makeText(context, "修改成功，数据保存成功.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveFailure(String errorMessage) {
        Toast.makeText(context, "数据保存失败.", Toast.LENGTH_SHORT).show();
    }

    public class AppItemMessageHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_app_icon)
        ImageView ivAppIcon;
        @BindView(R.id.tv_app_name)
        TextView tvAppName;
        @BindView(R.id.tv_app_desc)
        TextView tvAppDesc;
        @BindView(R.id.sb_switch_app)
        SwitchButton sbSwitchApp;
//        @BindView(R.id.sb_switch_app)
//        CheckBox sbSwitchApp;

        public AppItemMessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<AppInfo> getListData() {
        return listAppInfo;
    }
}
