package com.wuyou.worker.mvp.login;


import com.wuyou.worker.mvp.BasePresenter;
import com.wuyou.worker.mvp.IBaseView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public interface LoginContract {
    interface View extends IBaseView {
        void loginSuccess();
        void getVerifySuccess();
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void doLogin(String phone,String captcha);
         void doLoginPassword(String userName,String psw){};
        abstract void getVerifyCode(String phone);
    }
}
