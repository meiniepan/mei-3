package com.wuyou.worker.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.mvp.order.OrderStatusFragment;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.util.MyRecyclerViewScrollListener;
import com.wuyou.worker.view.activity.FinishOrderActivity;
import com.wuyou.worker.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by solang on 2018/2/5.
 */

public class OrderStatusAdapter extends BaseQuickAdapter<OrderInfoEntity, BaseHolder> {
    private final MyRecyclerViewScrollListener scrollListener;
    private OrderStatusFragment fragment;

    public OrderStatusAdapter(MyRecyclerViewScrollListener scrollListener, OrderStatusFragment fragment, int layoutResId, @Nullable List<OrderInfoEntity> data) {
        super(layoutResId, data);
        this.fragment = fragment;
        this.scrollListener = scrollListener;
    }

    @Override
    protected void convert(BaseHolder helper, OrderInfoEntity item) {
        String dispatch = TribeDateUtils.dateFormat(new Date(item.dispatched_at * 1000));
        helper.setText(R.id.order_status_state, getStatusText(item.status))
                .setText(R.id.tv_create_time, item.order_no)
                .setText(R.id.tv_category, item.service.service_name)
                .setText(R.id.tv_address, item.address.city_name + item.address.district + item.address.area + item.address.address)
                .setText(R.id.tv_deliver_time, dispatch)
                .setText(R.id.tv_server_time, item.service_date + "  " + item.service_time);
        Button dispatchBt = helper.getView(R.id.btn_divide_bill);
        setUpWithButton(item, dispatchBt);
    }

    private void setUpWithButton(OrderInfoEntity item, Button dispatchBt) {
        if (item.status == 1) {
            dispatchBt.setText(R.string.confirm_go);
            dispatchBt.setVisibility(View.VISIBLE);
            dispatchBt.setBackgroundResource(R.drawable.pay_selector);
        } else if (item.status == 2) {
            dispatchBt.setBackgroundResource(R.drawable.login_selector);
            if (item.is_finished == 1) {
                dispatchBt.setText(R.string.already_finish);
                dispatchBt.setVisibility(View.GONE);
                dispatchBt.setEnabled(false);
            } else {
                dispatchBt.setVisibility(View.VISIBLE);
                dispatchBt.setText(R.string.confirm_finish);
                dispatchBt.setEnabled(true);
            }
        } else {
            dispatchBt.setVisibility(View.GONE);
        }

        dispatchBt.setOnClickListener(view -> {
            dealWithClick(item);
        });
    }

    private String getStatusText(int status) {
        switch (status) {
            case 1:
                return fragment.getString(R.string.wait_go);
            case 2:
                return fragment.getString(R.string.ing);
            case 3:
                return fragment.getString(R.string.wait_comment);
            case 4:
                return fragment.getString(R.string.finished);
        }
        return fragment.getString(R.string.finished);
    }

    private void dealWithClick(OrderInfoEntity item) {
        if (item.status == 1) {
            confirmToStart(item.order_id, getData().indexOf(item));
        } else if (item.status == 2) {
            confirmToFinish(item);
        }
    }

    private void confirmToFinish(OrderInfoEntity infoEntity) {
        Intent intent = new Intent(fragment.getActivity(), FinishOrderActivity.class);
        intent.putExtra(Constant.ORDER_INFO, infoEntity);
        fragment.getActivity().startActivity(intent);
    }

    private void confirmToStart(String id, int i) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .confirm(QueryMapBuilder.getIns().put("worker_id", CarefreeDaoSession.getInstance().getUserId()).put("order_id", id).buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ToastUtils.ToastMessage(fragment.getActivity(), "操作成功！");
                        getData().remove(i);
                        OrderStatusAdapter.this.notifyItemRemoved(i);
                        if (!(getData().size() > 0)) {
                            scrollListener.setRefresh();
                        }
                    }
                });
    }
}
