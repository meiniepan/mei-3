package com.wuyou.worker.util;

import android.widget.Button;
import android.widget.TextView;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by hjn on 2018/2/2.
 * 发送验证码
 */

public class CounterDisposableObserver extends DisposableObserver<Integer>{
    TextView button;
    public CounterDisposableObserver(Button button) {
       this.button = button;
    }

    @Override
    protected void onStart() {
        button.setEnabled(false);
    }

    @Override
    public void onNext(Integer value) {
        button.setText(value + "秒后重发");
    }

    @Override
    public void onError(Throwable e) {
        button.setEnabled(true);
        button.setText("发送验证码");
    }

    @Override
    public void onComplete() {
        button.setEnabled(true);
        button.setText("发送验证码");
    }
}
