package com.wuyou.worker.mvp.order;

import android.os.Bundle;

import com.wuyou.worker.R;
import com.wuyou.worker.view.activity.BaseActivity;

/**
 * Created by Solang on 2018/7/5.
 */

public class ExtraChooseServiceActivity extends BaseActivity {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_extra_choose_service;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
setTitleText("选择服务");

    }
}
