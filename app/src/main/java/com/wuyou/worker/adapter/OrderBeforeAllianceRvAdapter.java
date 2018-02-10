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
        String create_time = TribeDateUtils.dateFormat(new Date(item.created_at*1000));
        helper.setText(R.id.tv_create_time, create_time)
                .setText(R.id.tv_category, item.category)
                .setText(R.id.tv_address, item.address)
                .setText(R.id.tv_sum, item.price);
        Button dispatch = helper.getView(R.id.btn_divide_bill);

        if (item.is_dispatch.equals("0")) {
            dispatch.setText("分单");
            dispatch.setOnClickListener(view -> {
                Intent intent = new Intent(activity, ChoseServerAllianceActivity.class);
                intent.putExtra(Constant.ORDER_ID,item.id);
                activity.startActivity(intent);
            });
        } else {
            dispatch.setText("发信息");
            dispatch.setOnClickListener(view -> {
                ToastUtils.ToastMessage(activity,"此功能暂未开通！");
            });
        }
    }
}
