package com.wuyou.worker.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.view.activity.MainActivity;
import com.wuyou.worker.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by solang on 2018/2/2.
 */

public class AccountLoginFragment extends BaseFragment<LoginContract.View, LoginContract.Presenter> implements LoginContract.View {
    @BindView(R.id.et_login_phone)
    EditText loginPhone;
    @BindView(R.id.et_login_psw)
    EditText loginPsw;


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_login_account;
    }

    @Override
    public void showError(String message, int res) {
        ToastUtils.ToastMessage(getContext(), message);
    }

    @Override
    protected LoginContract.Presenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    public void loginSuccess() {
        ToastUtils.ToastMessage(getContext(), "login success");
        Intent view = new Intent(getActivity(), MainActivity.class);
        startActivity(view);
    }

    @Override
    public void getVerifySuccess() {

    }


    @OnClick({R.id.login, R.id.tv_forget, R.id.btn_new_regist})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.login:
                //                String phone = loginPhone.getText().toString().trim();
//                if (!CommonUtil.checkPhone("", phone, getActivity())) return;
                showLoadingDialog();
                mPresenter.doLoginPassword(loginPhone.getText().toString().trim(), loginPsw.getText().toString().trim());
                break;
            case R.id.tv_forget:
                intent = new Intent(getActivity(), PhoneInputActivity.class);
                intent.putExtra(Constant.INPUT_PHONE_FLAG, 0);
                startActivity(intent);
                break;
            case R.id.btn_new_regist:
                intent = new Intent(getActivity(), PhoneInputActivity.class);
                intent.putExtra(Constant.INPUT_PHONE_FLAG, 1);
                startActivity(intent);
                break;

        }
    }
}
