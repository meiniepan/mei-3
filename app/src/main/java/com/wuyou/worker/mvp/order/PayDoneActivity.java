package com.wuyou.worker.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.IsPayedEntity;
import com.wuyou.worker.bean.entity.QrEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.view.activity.BaseActivity;
import com.wuyou.worker.view.activity.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Solang on 2018/7/10.
 */

public class PayDoneActivity extends BaseActivity {

    String orderId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_pay_done;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("支付完成");
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
    }


    @OnClick({R.id.tv_check_order_detail, R.id.tv_back_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_check_order_detail:
                startActivity(new Intent(getCtx(), OrderDetailActivity.class).putExtra(Constant.ORDER_ID, orderId));
                break;
            case R.id.tv_back_home:
                startActivity(new Intent(getCtx(), MainActivity.class));
                break;
            case R.id.back:
                jumpHome();
                break;
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        jumpHome();
    }

    private void jumpHome() {
        startActivity(new Intent(getCtx(), MainActivity.class));
    }
}
