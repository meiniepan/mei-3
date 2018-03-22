package com.wuyou.worker.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.mvp.order.OrderAfterFragment;
import com.wuyou.worker.mvp.order.OrderIngFragment;
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
        String create_time = TribeDateUtils.dateFormat(new Date(item.created_at * 1000));
        helper.setText(R.id.tv_create_time, create_time)
                .setText(R.id.tv_category, item.category)
                .setText(R.id.tv_address, item.address)
                .setText(R.id.tv_sum, item.price);
        Button dispatch = helper.getView(R.id.btn_divide_bill);
        dispatch.setOnClickListener(view -> confirm(item,helper.getAdapterPosition()));

    }

    private void confirm(OrderInfoEntity infoEntity, int i) {
        infoEntity.position = i;
        Intent intent = new Intent(fragment.getActivity(), FinishOrderActivity.class);
        intent.putExtra(Constant.ORDER_INFO,infoEntity);
        fragment.getActivity().startActivity(intent);
    }
}
