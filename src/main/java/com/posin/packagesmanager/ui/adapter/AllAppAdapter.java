package com.posin.packagesmanager.ui.adapter;

import android.content.Context;
import android.media.tv.TvContentRating;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.posin.packagesmanager.R;
import com.posin.packagesmanager.bean.AppInfo;
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
public class AllAppAdapter extends RecyclerView.Adapter<AllAppAdapter.AppItemMessageHolder> {


    private Context context;
    private List<AppInfo> listAppInfo;

    public AllAppAdapter(Context context, List<AppInfo> listAppInfo) {
        this.context = context;
        this.listAppInfo = listAppInfo;
    }

    @Override
    public AppItemMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_packages_list, parent, false);
        return new AllAppAdapter.AppItemMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(AppItemMessageHolder holder, int position) {

        AppInfo appInfo = listAppInfo.get(position);
        holder.ivAppIcon.setImageDrawable(appInfo.getAppIcon());
        holder.tvAppName.setText(appInfo.getAppName());
        holder.tvAppDesc.setText(appInfo.getPackageName() + " / " + appInfo.getClassName() +
                " /UID=" + appInfo.getAppUId());
        holder.sbSwitchApp.setChecked(appInfo.ismHideOnUserMode());
    }

    @Override
    public int getItemCount() {
        return listAppInfo.size();
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

        public AppItemMessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
