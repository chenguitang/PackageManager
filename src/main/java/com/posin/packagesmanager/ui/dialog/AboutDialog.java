package com.posin.packagesmanager.ui.dialog;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.posin.packagesmanager.R;
import com.posin.packagesmanager.base.BaseDialog;
import com.posin.packagesmanager.utils.DensityUtils;
import com.posin.packagesmanager.utils.LanguageUtils;

import butterknife.BindView;

/**
 * FileName: AboutDialog
 * Author: Greetty
 * Time: 2018/10/16 8:46
 * Desc: TODO
 */
public class AboutDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;

    @BindView(R.id.iv_about_close)
    ImageView ivClose;
    @BindView(R.id.tv_about_app_version)
    TextView tvAppVersionName;

    public AboutDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_about;
    }

    @Override
    public void initDialogSetting() {

        //点击空白处不能取消dialog弹框
        setCanceledOnTouchOutside(false);

        //修改dialog弹框大小
        Window mWindow = this.getWindow();
        if (mWindow != null) {
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.gravity = Gravity.CENTER;
            //适配不同密度的机器屏幕
            boolean is_cn = LanguageUtils.isCn(mContext);
            if (is_cn){
                lp.width = DensityUtils.dp2px(mContext, 570);
                lp.height = DensityUtils.dp2px(mContext, 650);
            }else{
                lp.width = DensityUtils.dp2px(mContext, 600);
                lp.height = DensityUtils.dp2px(mContext, 720);
            }
            mWindow.setAttributes(lp);
        }
    }

    @Override
    public void initData() {
        try {
            String versionName = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionName;
            tvAppVersionName.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ivClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.dismiss();
    }
}
