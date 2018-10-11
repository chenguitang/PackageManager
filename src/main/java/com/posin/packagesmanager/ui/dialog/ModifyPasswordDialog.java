package com.posin.packagesmanager.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.posin.packagesmanager.R;
import com.posin.packagesmanager.base.BaseDialog;
import com.posin.packagesmanager.utils.DensityUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * FileName: ComparePasswordDialog
 * Author: Greetty
 * Time: 2018/10/11 13:17
 * Desc: TODO
 */
public class ModifyPasswordDialog extends BaseDialog {

    private Context mContext;

    private String mTitle;


    @BindView(R.id.tv_dialog_title)
    TextView tvDialogTitle;
    @BindView(R.id.et_modify_password)
    EditText etModifyPassword;
    @BindView(R.id.et_modify_compare_password)
    EditText etModifyComparePassword;


    public ModifyPasswordDialog(Context context,String title) {
        super(context);
        this.mContext = context;
        this.mTitle=title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_modify_password;
    }

    @Override
    public void initDialogSetting() {
        //点击空白处不能取消dialog弹框
//        setCanceledOnTouchOutside(false);

        //修改dialog弹框大小
        Window mWindow = this.getWindow();
        if (mWindow != null) {
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.gravity = Gravity.CENTER;
            //适配不同密度的机器屏幕
            lp.width = DensityUtils.dp2px(mContext, 550);
            lp.height = DensityUtils.dp2px(mContext, 330);
            mWindow.setAttributes(lp);

            //只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            //加上下面这一行弹出对话框时软键盘随之弹出
            mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void initData() {
        tvDialogTitle.setText(mTitle);
    }

    @OnClick({R.id.btn_compare_ok, R.id.btn_compare_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_compare_ok:

                break;
            case R.id.btn_compare_cancel:
                this.dismiss();
                break;
            default:
                break;
        }
    }

}
