package com.wuyou.worker.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gs.buluo.common.widget.CaptchaEditText;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.util.CounterDisposableObserver;
import com.wuyou.worker.util.RxUtil;
import com.wuyou.worker.view.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by hjn91 on 2018/2/2.
 */

public class CaptchaInputActivity extends BaseActivity<LoginContract.View, LoginContract.Presenter> implements LoginContract.View {
    @BindView(R.id.input_captcha_edit)
    CaptchaEditText inputCaptchaEdit;
    @BindView(R.id.input_captcha_re_send)
    Button reSendCaptcha;
    private CounterDisposableObserver observer;
    private String phone;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        phone = getIntent().getStringExtra(Constant.PHONE);
        int flag = getIntent().getIntExtra(Constant.INPUT_PHONE_FLAG, 0);
        observer = new CounterDisposableObserver(reSendCaptcha);
        RxUtil.countdown(59).subscribe(observer);
        inputCaptchaEdit.showKeyBoard();
        inputCaptchaEdit.setInputCompleteListener(() -> {
            if (flag == 0) { //reset password
                jumpToReset(inputCaptchaEdit.getStrPassword());
            } else { //register
                doLogin(inputCaptchaEdit.getStrPassword());
            }
        });
    }

    private void jumpToReset(String captcha) {
        inputCaptchaEdit.clear();
        Intent view = new Intent(getCtx(), ResetPwdActivity.class);
        view.putExtra(Constant.CAPTCHA, captcha);
        startActivity(view);
    }

    private void doLogin(String inputCaptcha) {
        showLoadingDialog();
        mPresenter.doLogin(phone, inputCaptcha);
        inputCaptchaEdit.clear();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_captcha_input;
    }

    public void re_send(View view) {
        showLoadingDialog();
        mPresenter.getVerifyCode(phone);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (observer != null) {
            observer.dispose();
        }
    }

    @Override
    public void loginSuccess() {
        Intent view = new Intent(getCtx(), CompletingInfoActivity.class);
        startActivity(view);
    }

    @Override
    public void getVerifySuccess() {
        RxUtil.countdown(59).subscribe(observer);
    }
}
