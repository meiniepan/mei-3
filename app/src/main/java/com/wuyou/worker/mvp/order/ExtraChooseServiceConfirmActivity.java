package com.wuyou.worker.mvp.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.wuyou.worker.R;
import com.wuyou.worker.adapter.ChooseService2Adapter;
import com.wuyou.worker.adapter.ChooseServiceConfirmAdapter;
import com.wuyou.worker.bean.entity.ServiceSort2Entity;
import com.wuyou.worker.bean.entity.ServiceSortConfirmEntity;
import com.wuyou.worker.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/7/5.
 */

public class ExtraChooseServiceConfirmActivity extends BaseActivity {

    ChooseServiceConfirmAdapter adapter;
    List<ServiceSortConfirmEntity> data = new ArrayList();
    @BindView(R.id.tv_service_confirm_num)
    TextView tvServiceConfirmNum;
    @BindView(R.id.rv_service_confirm)
    RecyclerView rvServiceConfirm;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_extra_choose_service_confirm;
    }


    private void initRv() {
        adapter = new ChooseServiceConfirmAdapter(R.layout.item_service_confirm, data);
        rvServiceConfirm.setLayoutManager(new LinearLayoutManager(this));
        rvServiceConfirm.setAdapter(adapter);
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("结算确认");
        initRv();
    }


    @OnClick(R.id.tv_generate_qr_code)
    public void onViewClicked() {
    }
}
