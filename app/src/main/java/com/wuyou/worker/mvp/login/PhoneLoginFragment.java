package com.wuyou.worker.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.R;
import com.wuyou.worker.util.CounterDisposableObserver;
import com.wuyou.worker.util.RxUtil;
import com.wuyou.worker.view.activity.MainActivity;
import com.wuyou.worker.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by solang on 2018/2/2.
 */

public class PhoneLoginFragment extends BaseFragment<LoginContract.View, LoginContract.Presenter> implements LoginContract.View {
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
        ToastUtils.ToastMessage(getContext(), message);
    }


    @Override
    protected void bindView(Bundle savedInstanceState) {
        observer = new CounterDisposableObserver(reSendCaptcha);

    }

    @Override
    public void loginSuccess() {
        ToastUtils.ToastMessage(getContext(), "login success");
        Intent view = new Intent(getActivity(), MainActivity.class);
        startActivity(view);
    }

    @Override
    public void getVerifySuccess() {
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
//                if (!CommonUtil.checkPhone("", phone, this)) return;
                mPresenter.getVerifyCode(phone);
                break;
            case R.id.login:
//                String phone = loginPhone.getText().toString().trim();
//                if (!CommonUtil.checkPhone("", phone, getActivity())) return;
                showLoadingDialog();
                mPresenter.doLogin(loginPhone.getText().toString().trim(), loginVerify.getText().toString().trim());
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
}
