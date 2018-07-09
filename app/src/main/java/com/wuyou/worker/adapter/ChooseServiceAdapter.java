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
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.bean.entity.ServiceSortEntity;
import com.wuyou.worker.mvp.order.OrderStatusFragment;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.util.MyRecyclerViewScrollListener;
import com.wuyou.worker.view.activity.FinishOrderActivity;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by solang on 2018/2/5.
 */

public class ChooseServiceAdapter extends BaseQuickAdapter<ServiceSortEntity, BaseHolder> {

    public ChooseServiceAdapter(int layoutResId, @Nullable List<ServiceSortEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ServiceSortEntity item) {
//        String dispatch = TribeDateUtils.dateFormat(new Date(item.dispatched_at * 1000));
//        helper.setText(R.id.order_status_state, getStatusText(item.status))

    }

}
