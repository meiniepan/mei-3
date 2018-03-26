package com.wuyou.worker.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.R;
import com.wuyou.worker.mvp.login.LoginActivity;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class AppStartActivity extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        new Handler().postDelayed(() -> {
            if (CarefreeApplication.getInstance().getUserInfo() != null) {
                startActivity(new Intent(getCtx(), MainActivity.class));
                finish();
            } else {
                Intent view = new Intent(getCtx(), LoginActivity.class);
                startActivity(view);
                finish();
            }

        }, 1000);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_start;
    }
}
