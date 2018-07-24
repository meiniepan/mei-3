package com.wuyou.worker.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gs.buluo.common.BaseApplication;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.ChooseService2Adapter;
import com.wuyou.worker.adapter.ChooseServiceConfirmAdapter;
import com.wuyou.worker.bean.entity.ChosenServiceEntity;
import com.wuyou.worker.bean.entity.ChosenServicePostEntity;
import com.wuyou.worker.bean.entity.ServiceSort2Entity;
import com.wuyou.worker.bean.entity.ServiceSortConfirmEntity;
import com.wuyou.worker.event.OrderChangeEvent;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.view.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Solang on 2018/7/5.
 */

public class ExtraChooseServiceConfirmActivity extends BaseActivity {

    ChooseServiceConfirmAdapter adapter;
    Float total;
    ArrayList<ChosenServiceEntity> data;
    @BindView(R.id.tv_service_confirm_num)
    TextView tvServiceConfirmNum;
    @BindView(R.id.rv_service_confirm)
    RecyclerView rvServiceConfirm;
    String orderId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_extra_choose_service_confirm;
    }


    private void initRv() {
        data = getIntent().getParcelableArrayListExtra(Constant.CHOSEN_SERVICE);
        adapter = new ChooseServiceConfirmAdapter(R.layout.item_service_confirm, data);
        rvServiceConfirm.setLayoutManager(new LinearLayoutManager(this));
        rvServiceConfirm.setAdapter(adapter);
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("结算确认");
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        total = getIntent().getFloatExtra(Constant.CHOSEN_SERVICE_TOTAL, 0F);
        tvServiceConfirmNum.setText("¥" +CommonUtil.formatPrice(total));
        initRv();
    }


    @OnClick(R.id.tv_generate_qr_code)
    public void onViewClicked() {
        showLoadingDialog();
        ArrayList<ChosenServicePostEntity> postData = new ArrayList<>();
        for (ChosenServiceEntity e : data
                ) {
            ChosenServicePostEntity entity = new ChosenServicePostEntity();
            if (("0").equals(e.has_specification)) {
                entity.specification_id = 0;
                entity.amount = e.price * e.number;
            } else {
                entity.specification_id = Integer.parseInt(e.specification.id);
                entity.amount = e.specification.price * e.number;
            }
            entity.service_id = Integer.parseInt(e.service_id);
            entity.number = e.number;
            postData.add(entity);
        }
        String detail = new Gson().toJson(postData);
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .additionalCost(QueryMapBuilder.getIns()
                        .put("order_id", orderId)
                        .put("detail", detail)
                        .put("amount", total + "").buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        doJump();
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (12022 == e.getCode()) {
                            ToastUtils.ToastMessage(getCtx(), "支付已完成，为您跳转详情页");
                            startActivity(new Intent(getCtx(), OrderDetailActivity.class).putExtra(Constant.ORDER_ID, orderId));
                        }
                    }
                });

    }

    private void doJump() {
        Intent intent = new Intent(getCtx(), ChooseProceedsTypeActivity.class);
        intent.putExtra(Constant.ORDER_ID, orderId);
        intent.putExtra(Constant.CHOSEN_SERVICE_TOTAL, total);
        startActivity(intent);
        finish();
    }
}
