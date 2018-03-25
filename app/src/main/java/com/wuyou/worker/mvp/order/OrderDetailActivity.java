package com.wuyou.worker.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
import com.wuyou.worker.view.activity.BaseActivity;
import com.wuyou.worker.view.activity.FinishOrderActivity;
import com.wuyou.worker.view.activity.MainActivity;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by solang on 2018/2/5.
 */

public class OrderDetailActivity extends BaseActivity {
    @BindView(R.id.tv_accept_time)
    TextView tvAcceptTime;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.tv_server_time)
    TextView tvServerTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    @BindView(R.id.tv_is_payed)
    TextView tvIsPayed;
    @BindView(R.id.btn_divide_bill)
    Button btnDivideBill;
    String orderId;
    int fromWhere;

    @Override
    protected int getContentLayout() {
        return R.layout.order_detail;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        fromWhere = getIntent().getIntExtra(Constant.DIVIDE_ORDER_FROM, 0);
        initData();
    }

    private void initData() {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getOrdersDetail(orderId, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderInfoEntity> response) {
                        initUI(response.data);
                    }

                });
    }

    private void initUI(OrderInfoEntity data) {
        tvAcceptTime.setText(TribeDateUtils.dateFormat(new Date(data.accept_at * 1000)));
        tvCategory.setText(data.category);
        tvServerTime.setText(data.service_time);
        tvAddress.setText(data.address.city_name + data.address.district + data.address.area+data.address.address);
        tvPhone.setText(data.phone);
        tvCreateTime.setText(TribeDateUtils.dateFormat(new Date(data.created_at * 1000)));
        tvId.setText(data.order_no);
        tvSum.setText(data.price + "元");
        tvPayWay.setText(data.pay_type);
        tvIsPayed.setText(data.pay_status);
        if (fromWhere == 1) {
            btnDivideBill.setText("出发");
            btnDivideBill.setOnClickListener(view -> {
                CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                        .confirm(QueryMapBuilder.getIns().put("worker_id", CarefreeDaoSession.getInstance().getUserId()).put("order_id", orderId).buildPost())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseSubscriber<BaseResponse>() {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                        });

            });
        } else if (fromWhere == 2) {
            btnDivideBill.setText("完成");
            btnDivideBill.setOnClickListener(view -> {
                Intent intent = new Intent(getCtx(), FinishOrderActivity.class);
                intent.putExtra(Constant.ORDER_INFO, data);
                startActivity(intent);
            });
        } else if (fromWhere == 3) {
            btnDivideBill.setText("评价");
            btnDivideBill.setOnClickListener(view -> {
                ToastUtils.ToastMessage(OrderDetailActivity.this, "此功能暂未开通！");
            });
        }
    }


    @OnClick(R.id.btn_divide_bill)
    public void onViewClicked() {
    }
}
