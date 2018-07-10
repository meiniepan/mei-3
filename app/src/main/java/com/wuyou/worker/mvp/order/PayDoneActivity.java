package com.wuyou.worker.mvp.order;

import android.os.Bundle;
import android.view.View;

import com.wuyou.worker.R;
import com.wuyou.worker.view.activity.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/7/10.
 */

public class PayDoneActivity extends BaseActivity {


    @Override
    protected int getContentLayout() {
        return R.layout.activity_pay_done;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("收款二维码");
    }


    @OnClick({R.id.tv_check_order_detail, R.id.tv_back_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_check_order_detail:
                break;
            case R.id.tv_back_home:
                break;
        }
    }
}
