package com.wuyou.worker.mvp.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.ChooseService2Adapter;
import com.wuyou.worker.adapter.ChooseServiceAdapter;
import com.wuyou.worker.adapter.OrderStatusAdapter;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.bean.entity.ServiceSort2Entity;
import com.wuyou.worker.bean.entity.ServiceSortEntity;
import com.wuyou.worker.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/7/5.
 */

public class ExtraChooseServiceActivity extends BaseActivity {
    @BindView(R.id.rv_extra_choose_service_l)
    RecyclerView rvExtraChooseServiceL;
    @BindView(R.id.rv_extra_choose_service_r)
    RecyclerView rvExtraChooseServiceR;
    @BindView(R.id.tv_extra_sum)
    TextView tvExtraSum;
    @BindView(R.id.tv_extra_settle)
    TextView tvExtraSettle;
    @BindView(R.id.tv_extra_text)
    TextView tvExtraText;
    @BindView(R.id.sl_extra_choose_service)
    StatusLayout slExtraChooseService;
    ChooseServiceAdapter adapterLeft;
    ChooseService2Adapter adapterRight;
    List<ServiceSortEntity> dataLeft = new ArrayList();
    List<ServiceSort2Entity> dataRight = new ArrayList();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_extra_choose_service;
    }

    private void initLeftRv() {
        adapterLeft = new ChooseServiceAdapter(R.layout.item_service_sort, dataLeft);
        rvExtraChooseServiceL.setLayoutManager(new LinearLayoutManager(this));
        rvExtraChooseServiceL.setAdapter(adapterLeft);
    }


    private void initRightRv() {
        adapterRight = new ChooseService2Adapter(R.layout.item_service_sort_2, dataRight);
        rvExtraChooseServiceR.setLayoutManager(new LinearLayoutManager(this));
        rvExtraChooseServiceR.setAdapter(adapterRight);
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("选择服务");
        initLeftRv();
        initRightRv();
    }


    @OnClick(R.id.tv_extra_settle)
    public void onViewClicked() {
    }

    private void setOnNoChosen() {
        tvExtraText.setText("未选择服务");
        tvExtraSum.setVisibility(View.INVISIBLE);
        tvExtraSettle.setBackgroundResource(R.drawable.dark_orange_round_bac);
    }
    private void setOnChosen() {
        tvExtraText.setText("合计:");
        tvExtraSum.setVisibility(View.VISIBLE);
        tvExtraSettle.setBackgroundResource(R.drawable.orange_round_bac);
    }
}
