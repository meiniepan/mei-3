package com.wuyou.worker.adapter;

import android.support.annotation.Nullable;

import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.mvp.order.OrderAfterFragment;
import com.wuyou.worker.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.Date;
import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class OrderAfterRvAdapter extends BaseQuickAdapter<OrderInfoEntity, BaseHolder> {
    private OrderAfterFragment fragment;

    public OrderAfterRvAdapter(OrderAfterFragment fragment, int layoutResId, @Nullable List<OrderInfoEntity> data) {
        super(layoutResId, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseHolder helper, OrderInfoEntity item) {
        String dispatch = TribeDateUtils.dateFormat(new Date(item.dispatched_at * 1000));
        helper.setText(R.id.tv_create_time, item.order_no)
                .setText(R.id.tv_category, item.service.service_name)
                .setText(R.id.tv_address, item.address.city_name + item.address.district + item.address.area)
                .setText(R.id.tv_deliver_time, dispatch);
    }

}
