package com.wuyou.worker.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.OrderDetailServiceAdapter;
import com.wuyou.worker.bean.entity.OrderDetailInfoEntity;
import com.wuyou.worker.bean.entity.ServiceEntity;
import com.wuyou.worker.event.OrderChangeEvent;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.util.RxUtil;
import com.wuyou.worker.view.activity.BaseActivity;
import com.wuyou.worker.view.activity.FinishOrderActivity;
import com.wuyou.worker.view.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjn on 2018/2/6.
 */

public class OrderDetailActivity extends BaseActivity {
    @BindView(R.id.order_detail_status)
    TextView orderDetailStatus;
    @BindView(R.id.order_detail_un_pay_warn)
    TextView orderDetailWarn;
    @BindView(R.id.order_detail_address)
    TextView orderDetailAddress;
    @BindView(R.id.order_detail_name)
    TextView orderDetailName;
    @BindView(R.id.order_detail_phone)
    TextView orderDetailPhone;
    @BindView(R.id.order_detail_create_time)
    TextView orderDetailCreateTime;
    @BindView(R.id.order_detail_number)
    TextView orderDetailNumber;
    @BindView(R.id.order_detail_pay_method)
    TextView orderDetailPayMethod;
    @BindView(R.id.order_detail_pay_serial)
    TextView orderDetailBillSerial;
    @BindView(R.id.order_detail_serve_time)
    TextView orderDetailServeTime;
    @BindView(R.id.order_detail_remark)
    TextView orderDetailRemark;
    @BindView(R.id.order_detail_pay_time)
    TextView orderDetailPayTime;
    @BindView(R.id.order_detail_store_name)
    TextView orderDetailStoreName;
    @BindView(R.id.order_detail_fee)
    TextView orderDetailFee;
    @BindView(R.id.order_detail_other_fee)
    TextView orderDetailOtherFee;
    @BindView(R.id.order_detail_amount)
    TextView orderDetailAmount;
    @BindView(R.id.order_detail_delivery_time)
    TextView orderDeliveryTime;
    @BindView(R.id.tv_second_time)
    TextView orderSecondTime;
    @BindView(R.id.ll_second_time)
    LinearLayout llSecondTime;
    @BindView(R.id.order_detail_change)
    TextView orderDetailChange;
    @BindView(R.id.order_detail_go)
    TextView orderDetailGo;
    @BindView(R.id.order_detail_finish)
    TextView orderDetailFinish;
    @BindView(R.id.order_detail_bottom)
    View bottomView;
    @BindView(R.id.rv_service_detail)
    RecyclerView recyclerView;
    OrderDetailServiceAdapter adapter;
    private String orderId;
    private OrderDetailInfoEntity infoEntity;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.order_detail);
        baseStatusLayout.setErrorAction(
                v -> getOrderDetail(orderId)
        );
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        getOrderDetail(orderId);
    }


    private void getOrderDetail(String orderId) {
        baseStatusLayout.showProgressView();
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getOrdersDetail(orderId, QueryMapBuilder.getIns().put("worker_id", CarefreeDaoSession.getInstance().getUserId()).buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<OrderDetailInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderDetailInfoEntity> orderInfoEntityBaseResponse) {
                        baseStatusLayout.showContentView();
                        setData(orderInfoEntityBaseResponse.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        showErrMessage(e.getDisplayMessage());
                    }
                });
    }

    public void setData(OrderDetailInfoEntity data) {
        infoEntity = data;
        if (infoEntity.status == 1) orderDetailWarn.setVisibility(View.VISIBLE);
//        if (data.status == 2 && data.second_payment != 0) {
//            orderDetailWarn.setVisibility(View.VISIBLE);
//            orderDetailWarn.setText("待支付附加金额 " + data.second_payment + "元");
//        }
//        if (data.status != 2 && data.second_payment != 0) {
//
//        }
        orderDetailStatus.setText(CommonUtil.getOrderStatusString(data.status));
        orderDetailStoreName.setText(data.shop.shop_name);
        float pureFee = data.amount + data.second_payment;
        float visitFee = 0;
        float total;
        for (ServiceEntity e : data.services
                ) {
            if (e.stage.equals("1")) {
                visitFee = e.visiting_fee;
            }
        }
        total = pureFee + visitFee;
        orderDetailFee.setText(CommonUtil.formatPrice(pureFee));
        orderDetailOtherFee.setText(CommonUtil.formatPrice(visitFee));
        orderDetailAmount.setText(CommonUtil.formatPrice(total));
        orderDetailName.setText(data.address.name);
        orderDetailAddress.setText(String.format("%s%s%s%s", data.address.city, data.address.district, data.address.area, data.address.address));
        orderDetailPhone.setText(data.address.mobile);

        orderDetailCreateTime.setText(TribeDateUtils.dateFormat(new Date(data.created_at * 1000)));
        orderDeliveryTime.setText(TribeDateUtils.dateFormat(new Date(data.dispatch_at * 1000)));
        if (data.second_pay_time > 0) {
            llSecondTime.setVisibility(View.VISIBLE);
            orderSecondTime.setText(TribeDateUtils.dateFormat(new Date(data.second_pay_time * 1000)));
        }
        orderDetailNumber.setText(data.order_no);
        orderDetailServeTime.setText(data.service_date + "  " + data.service_time);
        orderDetailRemark.setText(data.remark);
        if (!TextUtils.isEmpty(data.serial)) orderDetailBillSerial.setText(data.serial);
        orderDetailPayMethod.setText(data.pay_type);
        orderDetailPayTime.setText(TribeDateUtils.dateFormat(new Date(data.pay_time * 1000)));
        initRv(data.services);
        setStatusUI(data);
    }

    private void initRv(List<ServiceEntity> service) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getCtx());
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new OrderDetailServiceAdapter(R.layout.item_order_detail_service_confirm, service);
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.order_detail_change, R.id.order_detail_go, R.id.order_detail_finish})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        if (TextUtils.isEmpty(orderId)) return;
        switch (view.getId()) {
            case R.id.order_detail_change:
                intent.setClass(getCtx(), OrderChangeTimeActivity.class);
                intent.putExtra(Constant.ORDER_ID, orderId);
                intent.putExtra(Constant.ADDRESS_BEAN, infoEntity.address);
                startActivity(intent);
                break;
            case R.id.order_detail_go:
                confirmToGo();
                break;
            case R.id.order_detail_finish:
                intent.setClass(getCtx(), FinishOrderActivity.class);
                intent.putExtra(Constant.ORDER_INFO, infoEntity);
                startActivity(intent);
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

    private void confirmToGo() {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .confirm(QueryMapBuilder.getIns().put("worker_id", CarefreeDaoSession.getInstance().getUserId()).put("order_id", orderId).buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        EventBus.getDefault().post(new OrderChangeEvent());
                    }
                });
    }

    public void setStatusUI(OrderDetailInfoEntity beanDetail) {
        if (beanDetail.status == 2 && beanDetail.is_finished != 1) {
            bottomView.setVisibility(View.VISIBLE);
            orderDetailChange.setVisibility(View.GONE);
            orderDetailGo.setVisibility(View.GONE);
            orderDetailFinish.setVisibility(View.VISIBLE);
        } else if (beanDetail.status == 1) {
            bottomView.setVisibility(View.VISIBLE);
            orderDetailGo.setVisibility(View.VISIBLE);
            orderDetailFinish.setVisibility(View.GONE);
            if (beanDetail.service_time_is_changed == 0)
                orderDetailChange.setVisibility(View.VISIBLE);
        } else {
            bottomView.setVisibility(View.GONE);
            orderDetailChange.setVisibility(View.GONE);
            orderDetailGo.setVisibility(View.GONE);
            orderDetailFinish.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderChanged(OrderChangeEvent event) {
        if (event.getServeDate() != null) {//改约时间，只改了时间，不需要重新拉数据
            orderDetailServeTime.setText(String.format("%s  %s", event.getServeDate(), event.getServeTime()));
            orderDetailChange.setVisibility(View.GONE);
        } else {
            showLoadingDialog();
            getOrderDetail(orderId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}