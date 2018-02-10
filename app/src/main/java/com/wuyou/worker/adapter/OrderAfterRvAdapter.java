package com.wuyou.worker.adapter;

import android.support.annotation.Nullable;
import android.widget.Button;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.mvp.order.OrderAfterFragment;
import com.wuyou.worker.mvp.order.OrderIngFragment;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        String create_time = TribeDateUtils.dateFormat(new Date(item.created_at * 1000));
        helper.setText(R.id.tv_create_time, create_time)
                .setText(R.id.tv_category, item.category)
                .setText(R.id.tv_address, item.address)
                .setText(R.id.tv_sum, item.price);
    }

}
