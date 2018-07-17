package com.wuyou.worker.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.ChosenServiceEntity;
import com.wuyou.worker.bean.entity.ServiceSort2;
import com.wuyou.worker.mvp.order.AddReduceNumListener;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.util.GlideUtils;

import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class ChooseService2Adapter extends BaseQuickAdapter<ServiceSort2, BaseHolder> {
    AddReduceNumListener addReduceNumListener;

    public ChooseService2Adapter(int layoutResId, @Nullable List<ServiceSort2> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ServiceSort2 item) {
        LinearLayout llSpec = helper.getView(R.id.ll_spec);
        LinearLayout llControl = helper.getView(R.id.ll_title_control);
        TextView reduce = helper.getView(R.id.tv_service_reduce);
        TextView num = helper.getView(R.id.tv_service_num);
        TextView add = helper.getView(R.id.tv_service_add);
        ImageView avatar = helper.getView(R.id.iv_service);
        GlideUtils.loadRoundCornerImage(mContext, item.photo, avatar);
        helper.setText(R.id.name2, item.title);
        if (item.number > 0) {
            showReduce(num, reduce,item.number);

        } else {
            hideReduce(num, reduce);
        }
        if ("0".equals(item.has_specification)) {
            llSpec.setVisibility(View.GONE);
            llControl.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_service_price, "¥" + CommonUtil.formatPrice(item.price));
            add.setOnClickListener(v -> {
                if (item.number == item.stock) {
                    ToastUtils.ToastMessage(mContext, "库存不足！");
                    return;
                }
                if (item.number == 0) {
                    showReduce(num, reduce, item.number);
                }
                item.number = item.number + 1;
                num.setText(item.number + "");
                ChosenServiceEntity entity = new ChosenServiceEntity();
                entity.service_id = item.id;
                entity.number = item.number;
                entity.image = item.photo;
                entity.service_name = item.title;
                entity.has_specification = "0";
                entity.price = item.price;
                addReduceNumListener.addNum(entity);
            });
            reduce.setOnClickListener(v ->
            {
                if (item.number <= 1) {
                    hideReduce(num, reduce);
                }
                item.number = item.number - 1;
                num.setText(item.number + "");
                ChosenServiceEntity entity = new ChosenServiceEntity();
                entity.service_id = item.id;
                entity.number = item.number;
                entity.image = item.photo;
                entity.service_name = item.title;
                entity.has_specification = "0";
                entity.price = item.price;
                addReduceNumListener.reduceNum(entity);
            });

        } else {
            llSpec.setVisibility(View.VISIBLE);
            llControl.setVisibility(View.GONE);
            initRv(helper, item);
        }
    }

    private void showReduce(TextView num, TextView reduce, int number) {
        num.setVisibility(View.VISIBLE);
        reduce.setVisibility(View.VISIBLE);
        num.setText(number+"");
    }

    private void hideReduce(TextView num, TextView reduce) {
        num.setVisibility(View.INVISIBLE);
        reduce.setVisibility(View.INVISIBLE);
    }

    private void initRv(BaseHolder helper, ServiceSort2 item) {
        RecyclerView recyclerView = helper.getView(R.id.rv_service_sub);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ChooseService2SpecAdapter adapter = new ChooseService2SpecAdapter(item,R.layout.item_service_sort_2_sub, item.specification);
        adapter.setAddReduceNumLis(addReduceNumListener);
        recyclerView.setAdapter(adapter);
    }

    public void setAddReduceNumLis(AddReduceNumListener listener) {
        this.addReduceNumListener = listener;
    }
}
