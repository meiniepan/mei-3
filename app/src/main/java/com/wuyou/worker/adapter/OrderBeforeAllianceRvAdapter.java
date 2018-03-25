package com.wuyou.worker.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.mvp.order.ChoseServerAllianceActivity;
import com.wuyou.worker.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.Date;
import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class OrderBeforeAllianceRvAdapter extends BaseQuickAdapter<OrderInfoEntity, BaseHolder> {
    private Activity activity;

    public OrderBeforeAllianceRvAdapter(Activity activity, int layoutResId, @Nullable List<OrderInfoEntity> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseHolder helper, OrderInfoEntity item) {
        String dispatch = TribeDateUtils.dateFormat(new Date(item.dispatched_at * 1000));
        helper.setText(R.id.tv_create_time, item.order_no)
                .setText(R.id.tv_category, item.service.service_name)
                .setText(R.id.tv_address, item.address.city_name + item.address.district + item.address.area)
                .setText(R.id.tv_deliver_time, dispatch);
        Button dispatchBt = helper.getView(R.id.btn_divide_bill);

        if (item.is_dispatch.equals("0")) {
            dispatchBt.setText("分单");
            dispatchBt.setOnClickListener(view -> {
                Intent intent = new Intent(activity, ChoseServerAllianceActivity.class);
                intent.putExtra(Constant.ORDER_ID,item.id);
                activity.startActivity(intent);
            });
        } else {
            dispatchBt.setText("发信息");
            dispatchBt.setOnClickListener(view -> {
                ToastUtils.ToastMessage(activity,"此功能暂未开通！");
            });
        }
    }
}
