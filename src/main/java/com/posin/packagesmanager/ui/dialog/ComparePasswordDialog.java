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
import com.posin.packagesmanager.ui.activity.MainActivity;
import com.posin.packagesmanager.ui.contract.ComparePasswordContract;
import com.posin.packagesmanager.ui.presenter.ComparePasswordPresenter;
import com.posin.packagesmanager.utils.DensityUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * FileName: ComparePasswordDialog
 * Author: Greetty
 * Time: 2018/10/11 13:17
 * Desc: TODO
 */
public class ComparePasswordDialog extends BaseDialog {

    private Context mContext;

    private String mTitle;
    private ComparePasswordPresenter mComparePasswordPresenter;
    private ComparePasswordContract.IComparePasswordView mComparePasswordView;


    @BindView(R.id.tv_dialog_title)
    TextView tvDialogTitle;
    @BindView(R.id.et_compare_password)
    EditText etPassword;

    public ComparePasswordDialog(Context context, String title,
                                 ComparePasswordContract.IComparePasswordView comparePasswordView) {
        super(context);
        this.mContext = context;
        this.mTitle = title;
        this.mComparePasswordView = comparePasswordView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_compare_password;
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
            lp.height = DensityUtils.dp2px(mContext, 300);
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

        mComparePasswordPresenter = new ComparePasswordPresenter(mComparePasswordView);
    }

    @OnClick({R.id.btn_compare_ok, R.id.btn_compare_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_compare_ok:

                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(password)) {
                    mComparePasswordView.compareFailure("密码不能为空！");
                    return;
                }
                mComparePasswordPresenter.comparePassword(password);
                break;
            case R.id.btn_compare_cancel:
                this.dismiss();
                mComparePasswordPresenter.cancelCompare();
                break;
            default:
                break;
        }
    }

}
