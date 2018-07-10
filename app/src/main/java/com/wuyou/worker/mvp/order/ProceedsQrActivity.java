package com.wuyou.worker.mvp.order;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyou.worker.R;
import com.wuyou.worker.view.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by Solang on 2018/7/10.
 */

public class ProceedsQrActivity extends BaseActivity {
    @BindView(R.id.tv_proceeds_sum)
    TextView tvProceedsSum;
    @BindView(R.id.iv_proceeds_qr)
    ImageView ivProceedsQr;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_proceeds_type;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("收款二维码");
    }

}
