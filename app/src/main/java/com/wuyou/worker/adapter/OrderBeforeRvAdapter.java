package com.wuyou.worker.adapter;

import android.support.annotation.Nullable;
import android.widget.Button;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
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
        String dispatch = TribeDateUtils.dateFormat(new Date(item.dispatched_at * 1000));
        helper.setText(R.id.tv_create_time, item.order_no)
                .setText(R.id.tv_category, item.service.service_name)
                .setText(R.id.tv_address, item.address.city_name + item.address.district + item.address.area+item.address.address)
                .setText(R.id.tv_deliver_time, dispatch);
        Button dispatchBt = helper.getView(R.id.btn_divide_bill);
        dispatchBt.setOnClickListener(view -> {
            confirm(item.order_id, getData().indexOf(item));
        });

    }

    private void confirm(String id, int i) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .confirm(QueryMapBuilder.getIns().put("worker_id", CarefreeDaoSession.getInstance().getUserId()).put("order_id", id).buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ToastUtils.ToastMessage(fragment.getActivity(), "操作成功！");
                        getData().remove(i);
                        OrderBeforeRvAdapter.this.notifyItemRemoved(i);
                        if (!(getData().size() > 0)) {
                            scrollListener.setRefresh();
                        }
                    }

                });
    }
}
