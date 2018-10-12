package com.posin.packagesmanager.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.posin.packagesmanager.R;
import com.posin.packagesmanager.base.BaseDialog;
import com.posin.packagesmanager.ui.contract.ModifyPasswordContract;
import com.posin.packagesmanager.ui.presenter.ModifyPasswordPresenter;
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
    private ModifyPasswordContract.IModifyPasswordPresenter mModifyPasswordPresenter;
    private ModifyPasswordContract.IModifyPasswordView mModifyPasswordView;


    @BindView(R.id.tv_dialog_title)
    TextView tvDialogTitle;
    @BindView(R.id.et_modify_password)
    EditText etModifyPassword;
    @BindView(R.id.et_modify_compare_password)
    EditText etModifyComparePassword;


    public ModifyPasswordDialog(Context context, String title,
                                ModifyPasswordContract.IModifyPasswordView modifyPasswordView) {
        super(context);
        this.mContext = context;
        this.mTitle = title;
        this.mModifyPasswordView = modifyPasswordView;
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
        mModifyPasswordPresenter = new ModifyPasswordPresenter(mContext, mModifyPasswordView);
    }

    @OnClick({R.id.btn_compare_ok, R.id.btn_compare_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_compare_ok:

                String modifyPassword = etModifyPassword.getText().toString().trim();
                String comparePassword = etModifyComparePassword.getText().toString().trim();

                if (TextUtils.isEmpty(modifyPassword) || TextUtils.isEmpty(comparePassword)) {
//                    Toast.makeText(mContext, "新密码及确认新密码不能为空，请检查重新输入.",
//                            Toast.LENGTH_SHORT).show();
                    mModifyPasswordView.modifyPasswordFailure("新密码及确认新密码不能为空，请检查重新输入.");
                    return;
                }

                if (!modifyPassword.equals(comparePassword)) {
//                    Toast.makeText(mContext, "新密码与确认新密码不一致，请重新输入",
//                            Toast.LENGTH_SHORT).show();
                    mModifyPasswordView.modifyPasswordFailure("新密码与确认新密码不一致，请重新输入");
                    return;
                }

                if (modifyPassword.length() < 6 || comparePassword.length() < 6 ||
                        modifyPassword.length() > 16 || comparePassword.length() > 16) {
                    mModifyPasswordView.modifyPasswordFailure("新密码长度必须为6~16位，请检查重新输入");
                    return;
                }

                mModifyPasswordPresenter.modifyPassword(comparePassword);
                break;
            case R.id.btn_compare_cancel:
                mModifyPasswordPresenter.cancelModifyPassword();
                this.dismiss();
                break;
            default:
                break;
        }
    }

}
