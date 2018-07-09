package com.wuyou.worker.adapter;

import android.support.annotation.Nullable;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.bean.entity.ServiceSort2Entity;
import com.wuyou.worker.mvp.order.OrderStatusFragment;
import com.wuyou.worker.util.MyRecyclerViewScrollListener;

import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class ChooseService2Adapter extends BaseQuickAdapter<ServiceSort2Entity, BaseHolder> {

    public ChooseService2Adapter(int layoutResId, @Nullable List<ServiceSort2Entity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ServiceSort2Entity item) {
//        String dispatch = TribeDateUtils.dateFormat(new Date(item.dispatched_at * 1000));
//        helper.setText(R.id.order_status_state, getStatusText(item.status))

    }

}
