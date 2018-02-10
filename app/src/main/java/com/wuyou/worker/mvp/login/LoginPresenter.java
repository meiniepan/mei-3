package com.wuyou.worker.mvp.login;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.bean.UserInfo;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.UserApis;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class LoginPresenter extends LoginContract.Presenter {
    @Override
    void doLogin(String phone, String captcha) {
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .doLogin(QueryMapBuilder.getIns().put("mobile", phone).put("captcha", captcha).buildPost())
                .subscribeOn(Schedulers.io())
                .flatMap(userInfoBaseResponse -> CarefreeRetrofit.getInstance().createApi(UserApis.class)
                        .getUserInfo(userInfoBaseResponse.data.getUid(), QueryMapBuilder.getIns().buildGet()))
                .doOnNext(userInfoBaseResponse -> CarefreeApplication.getInstance().setUserInfo(userInfoBaseResponse.data))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse) {
                        mView.loginSuccess();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }

    @Override
    void doLoginPassword(String userName, String psw) {
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .doLogin(QueryMapBuilder.getIns().put("username", userName).put("password", psw).buildPost())
                .subscribeOn(Schedulers.io())
                .flatMap(userInfoBaseResponse -> CarefreeRetrofit.getInstance().createApi(UserApis.class)
                        .getUserInfo(userInfoBaseResponse.data.getUid(), QueryMapBuilder.getIns().buildGet()))
                .doOnNext(userInfoBaseResponse -> CarefreeApplication.getInstance().setUserInfo(userInfoBaseResponse.data))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse) {
                        mView.loginSuccess();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }

    @Override
    void getVerifyCode(String phone) {
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .getVerifyCode(QueryMapBuilder.getIns().put("mobile", phone).buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse) {
                        mView.getVerifySuccess();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }
}
