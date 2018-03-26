package com.wuyou.worker.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.wuyou.worker.R;

import butterknife.BindView;

/**
 * Created by DELL on 2018/3/26.
 */

public class OrderStatisticsActivity extends BaseActivity {
    @BindView(R.id.order_statistic_count)
    TextView orderStatisticCount;
    @BindView(R.id.order_statistic_count_waiting)
    TextView orderStatisticCountWaiting;
    @BindView(R.id.order_statistic_month_amount)
    TextView orderStatisticMonthAmount;
    @BindView(R.id.order_statistic_total_amount)
    TextView orderStatisticTotalAmount;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_statistics;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }
}
