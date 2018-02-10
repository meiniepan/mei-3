package com.wuyou.worker.mvp;

/**
 * Created by hjn on 2016/11/3.
 */
public abstract class BasePresenter <T extends IBaseView> {
    public T mView;

    public void attach(T mView) {
        this.mView = mView;
    }

    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }

    public boolean isAttach(){
        return mView!=null;
    }
}