package com.wuyou.worker.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.mvp.order.OrderIngFragment;
import com.wuyou.worker.util.MyRecyclerViewScrollListener;
import com.wuyou.worker.view.activity.FinishOrderActivity;
import com.wuyou.worker.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.Date;
import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class OrderIngRvAdapter extends BaseQuickAdapter<OrderInfoEntity, BaseHolder> {
    private final MyRecyclerViewScrollListener scrollListener;
    private OrderIngFragment fragment;

    public OrderIngRvAdapter(MyRecyclerViewScrollListener scrollListener, OrderIngFragment fragment, int layoutResId, @Nullable List<OrderInfoEntity> data) {
        super(layoutResId, data);
        this.fragment = fragment;
        this.scrollListener = scrollListener;
    }

    @Override
    protected void convert(BaseHolder helper, OrderInfoEntity item) {
        String dispatch = TribeDateUtils.dateFormat(new Date(item.dispatched_at * 1000));
        helper.setText(R.id.tv_create_time, item.order_no)
                .setText(R.id.tv_category, item.service.service_name)
                .setText(R.id.tv_address, item.address.city_name + item.address.district + item.address.area)
                .setText(R.id.tv_deliver_time, dispatch);
        Button button = helper.getView(R.id.btn_divide_bill);
        button.setOnClickListener(view -> confirm(item, helper.getAdapterPosition()));
        if (item.is_finished == 1) {
            button.setText("已完工");
            button.setEnabled(false);
        } else {
            button.setText("确认完成");
            button.setEnabled(true);
        }
    }

    private void confirm(OrderInfoEntity infoEntity, int i) {
        infoEntity.position = i;
        Intent intent = new Intent(fragment.getActivity(), FinishOrderActivity.class);
        intent.putExtra(Constant.ORDER_INFO, infoEntity);
        fragment.getActivity().startActivity(intent);
    }
}
