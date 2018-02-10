package com.wuyou.worker.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.bean.entity.OrderInfoListEntity;
import com.wuyou.worker.mvp.order.ChoseServerActivity;
import com.wuyou.worker.mvp.order.OrderBeforeFragment;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.util.MyRecyclerViewScrollListener;
import com.wuyou.worker.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by solang on 2018/2/5.
 */

public class OrderBeforeRvAdapter extends BaseQuickAdapter<OrderInfoEntity, BaseHolder> {
    private final MyRecyclerViewScrollListener scrollListener;
    private OrderBeforeFragment fragment;

    public OrderBeforeRvAdapter(MyRecyclerViewScrollListener scrollListener, OrderBeforeFragment fragment, int layoutResId, @Nullable List<OrderInfoEntity> data) {
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
        dispatch.setOnClickListener(view -> {
            confirm(item.id,getData().indexOf(item));
        });

    }

    private void confirm(String id, int i) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .confirm(CarefreeApplication.getInstance().getUserInfo().getUid(), id, QueryMapBuilder.getIns().buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ToastUtils.ToastMessage(fragment.getActivity(),"操作成功！");
                        getData().remove(i);
                        OrderBeforeRvAdapter.this.notifyItemRemoved(i);
                        if (!(getData().size()>0)){
                            scrollListener.setRefresh();
                        }
                    }

                });
    }
}
