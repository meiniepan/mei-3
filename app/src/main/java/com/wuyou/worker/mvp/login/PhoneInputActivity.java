package com.wuyou.worker.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.UserInfo;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.UserApis;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.view.activity.BaseActivity;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn91 on 2018/2/2.
 */

public class PhoneInputActivity extends BaseActivity {
    @BindView(R.id.input_phone_title)
    TextView inputPhoneTitle;
    @BindView(R.id.input_phone)
    EditText inputPhone;
    private int flag = 1;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        flag = getIntent().getIntExtra(Constant.INPUT_PHONE_FLAG, 1);
        if (flag == 0) { //reset password
            inputPhoneTitle.setText("请输入手机号，重新设置密码");
        } else if (flag == 1) {//register
            inputPhoneTitle.setText("请输入手机号，创建账号");
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_input_phone;
    }


    public void sendCaptcha(View view) {
        String phone = inputPhone.getText().toString().trim();
        if (!CommonUtil.checkPhone("", phone, this)) return;
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .getVerifyCode(QueryMapBuilder.getIns().put("phone", phone).buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse) {
                        Intent view = new Intent(getCtx(), CaptchaInputActivity.class);
                        view.putExtra(Constant.INPUT_PHONE_FLAG, flag);
                        view.putExtra(Constant.PHONE, phone);
                        startActivity(view);
                    }
                });
    }
}
