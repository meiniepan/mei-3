package com.wuyou.worker.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.util.GlideUtils;
import com.wuyou.worker.util.RxUtil;
import com.wuyou.worker.view.activity.BaseActivity;
import com.wuyou.worker.view.activity.FinishOrderActivity;
import com.wuyou.worker.view.activity.MainActivity;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.order_detail_serve_way)
    TextView orderDetailServeWay;
    @BindView(R.id.order_detail_serve_time)
    TextView orderDetailServeTime;
    @BindView(R.id.order_detail_remark)
    TextView orderDetailRemark;
    @BindView(R.id.order_detail_pay_time)
    TextView orderDetailPayTime;
    @BindView(R.id.order_detail_second_payment)
    TextView orderDetailSecondPayment;
    @BindView(R.id.order_detail_store_name)
    TextView orderDetailStoreName;
    @BindView(R.id.order_detail_picture)
    ImageView orderDetailPicture;
    @BindView(R.id.order_detail_serve_name)
    TextView orderDetailServeName;
    @BindView(R.id.order_detail_goods_number)
    TextView orderDetailGoodsNumber;
    @BindView(R.id.order_detail_fee)
    TextView orderDetailFee;
    @BindView(R.id.order_detail_other_fee)
    TextView orderDetailOtherFee;
    @BindView(R.id.order_detail_amount)
    TextView orderDetailAmount;
    @BindView(R.id.order_detail_change)
    TextView orderDetailChange;
    @BindView(R.id.order_detail_go)
    TextView orderDetailGo;
    @BindView(R.id.order_detail_finish)
    TextView orderDetailFinish;
    private String orderId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        showLoadingDialog();
        getOrderDetail(orderId);
    }

    private void getOrderDetail(String orderId) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getOrdersDetail(orderId, QueryMapBuilder.getIns().put("worker_id", CarefreeDaoSession.getInstance().getUserId()).buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<OrderInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderInfoEntity> orderInfoEntityBaseResponse) {
                        setData(orderInfoEntityBaseResponse.data);
                    }
                });
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_detail;
    }


    @Override
    public void showError(String message, int res) {
        ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
    }

    public void setData(OrderInfoEntity data) {
        if (data.status == 1) orderDetailWarn.setVisibility(View.VISIBLE);
        if (data.status == 2 && data.second_payment != 0) {
            orderDetailWarn.setVisibility(View.VISIBLE);
            orderDetailWarn.setText("待支付附加金额 " + data.second_payment + "元");
        }
        if (data.status != 2 && data.second_payment != 0) {
            findViewById(R.id.order_detail_second_payment_area).setVisibility(View.VISIBLE);
        }
        GlideUtils.loadImage(this, data.service.photo, orderDetailPicture);
        orderDetailStatus.setText(CommonUtil.getOrderStatusString(data.status));
        orderDetailStoreName.setText(data.shop.shop_name);
        orderDetailServeName.setText(data.service.title);
        orderDetailSecondPayment.setText(CommonUtil.formatPrice(data.second_payment));
        orderDetailGoodsNumber.setText(data.number + "");
        orderDetailOtherFee.setText(CommonUtil.formatPrice(data.service.visiting_fee));
        orderDetailFee.setText(CommonUtil.formatPrice(data.service.price));
        orderDetailAmount.setText(CommonUtil.formatPrice(data.amount));
        orderDetailName.setText(data.address.name);
        orderDetailAddress.setText(String.format("%s%s%s%s", data.address.city, data.address.district, data.address.area, data.address.address));
        orderDetailPhone.setText(data.address.mobile);

        orderDetailCreateTime.setText(TribeDateUtils.dateFormat(new Date(data.created_at * 1000)));
        orderDetailNumber.setText(data.order_no);
        orderDetailServeWay.setText(data.service_mode);
        orderDetailServeTime.setText(data.service_date + "  " + data.service_time);
        orderDetailRemark.setText(data.remark);
        if (!TextUtils.isEmpty(data.serial)) orderDetailBillSerial.setText(data.serial);
        orderDetailPayMethod.setText(data.pay_type);
        orderDetailPayTime.setText(TribeDateUtils.dateFormat(new Date(data.pay_time * 1000)));

        setStatusUI(data);
    }

    @OnClick({R.id.order_detail_change, R.id.order_detail_go, R.id.order_detail_finish})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        if (TextUtils.isEmpty(orderId)) return;
        switch (view.getId()) {
            case R.id.order_detail_change:
                intent.setClass(getCtx(), OrderChangeTimeActivity.class);
                startActivity(intent);
                break;
            case R.id.order_detail_go:
                intent.setClass(getCtx(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.order_detail_finish:
                intent.setClass(getCtx(), FinishOrderActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void setStatusUI(OrderInfoEntity beanDetail) {
        if (beanDetail.status == 2 && beanDetail.is_finished != 1) {
            orderDetailFinish.setVisibility(View.VISIBLE);
        }

        if (beanDetail.status == 1) {
            orderDetailChange.setVisibility(View.VISIBLE);
            orderDetailGo.setVisibility(View.VISIBLE);
        }

    }
}