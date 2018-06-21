package com.wuyou.worker.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.R;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.util.CounterDisposableObserver;
import com.wuyou.worker.util.RxUtil;
import com.wuyou.worker.view.activity.BaseActivity;
import com.wuyou.worker.view.activity.MainActivity;
import com.wuyou.worker.view.widget.panel.EnvironmentChoosePanel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public class LoginActivity extends BaseActivity<LoginContract.View, LoginContract.Presenter> implements LoginContract.View {
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_verify)
    EditText loginVerify;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.login_send_verify)
    Button reSendCaptcha;
    private CounterDisposableObserver observer;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_login_phone;
    }

    @Override
    public void showError(String message, int res) {
        ToastUtils.ToastMessage(this, message);
    }


    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.tint_bg);
        findViewById(R.id.back_door).setOnClickListener(v -> showChangeEnvironment());
    }

    @Override
    public void loginSuccess() {
        Intent view = new Intent(this, MainActivity.class);
        startActivity(view);
        finish();
        ToastUtils.ToastMessage(getCtx(), R.string.login_success);
    }

    @Override
    public void getVerifySuccess() {
        observer = new CounterDisposableObserver(reSendCaptcha);
        RxUtil.countdown(59).subscribe(observer);
    }

    @Override
    protected LoginContract.Presenter getPresenter() {
        return new LoginPresenter();
    }

    @OnClick({R.id.login_send_verify, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_send_verify:
                String phone = loginPhone.getText().toString().trim();
                if (!CommonUtil.checkPhone("", phone, this)) return;
                showLoadingDialog();
                mPresenter.getVerifyCode(phone);
                loginVerify.requestFocus();
                break;
            case R.id.login:
                String phone1 = loginPhone.getText().toString().trim();
                if (!CommonUtil.checkPhone("", phone1, getCtx())) return;
                showLoadingDialog();
                mPresenter.doLogin(phone1, loginVerify.getText().toString().trim());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (observer != null) {
            observer.dispose();
        }

    }

    private int clickTime = 0;
    private long firstTime = 0;

    private void showChangeEnvironment() {
        if (clickTime == 0) {
            firstTime = System.currentTimeMillis();
        }
        clickTime++;
        if (clickTime == 5) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - firstTime <= 2000) {
                EnvironmentChoosePanel choosePanel = new EnvironmentChoosePanel(this);
                choosePanel.show();
                clickTime = 0;
                firstTime = 0;
            }
        }
    }
}
