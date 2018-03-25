package com.wuyou.worker.mvp.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.UserInfo;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.UserApis;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.view.activity.BaseActivity;
import com.wuyou.worker.view.activity.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn91 on 2018/2/2.
 */

public class CompletingInfoActivity extends BaseActivity {
    @BindView(R.id.complete_info_nickname)
    EditText completeInfoNickname;
    @BindView(R.id.complete_info_pwd)
    EditText completeInfoPwd;
    @BindView(R.id.complete_info_pwd_again)
    EditText completeInfoPwdAgain;
    @BindView(R.id.talk_later)
    TextView talkLater;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        talkLater.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_completing_info;
    }

    //点击确定
    public void setPwdInfo(View view) {
        if (completeInfoPwd.length() < 6) {
            ToastUtils.ToastMessage(getCtx(), "密码长度至少6位");
            return;
        }
        String pwd = completeInfoPwd.getText().toString();
        String pwdAgain = completeInfoPwdAgain.getText().toString();
        if (!TextUtils.equals(pwd, pwdAgain)) {
            ToastUtils.ToastMessage(getCtx(), "两次密码输入不一致");
            return;
        }

        UserInfo userInfo = CarefreeApplication.getInstance().getUserInfo();
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .updatePwd(userInfo.getWorker_id(),
                        QueryMapBuilder.getIns().put("password", CommonUtil.getMD5Str(pwd)).put("confirm_password", pwdAgain).buildPost())
                .subscribeOn(Schedulers.io())
                .doOnNext(baseResponse -> {
                    userInfo.setPassword(pwd);
                    CarefreeApplication.getInstance().getUserInfoDao().save(userInfo);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        jumpToMain();
                    }
                });
    }

    private void jumpToMain() {
        Intent view = new Intent(getCtx(), MainActivity.class);
        startActivity(view);
    }


    @OnClick(R.id.talk_later)
    public void onViewClicked() {
        jumpToMain();
    }
}
