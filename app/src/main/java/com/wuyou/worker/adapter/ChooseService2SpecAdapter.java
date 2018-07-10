package com.wuyou.worker.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.ChosenServiceEntity;
import com.wuyou.worker.bean.entity.ServiceSort2;
import com.wuyou.worker.bean.entity.ServiceSort2Spec;
import com.wuyou.worker.mvp.order.AddReduceNumListener;
import com.wuyou.worker.util.CommonUtil;

import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class ChooseService2SpecAdapter extends BaseQuickAdapter<ServiceSort2Spec, BaseHolder> {

    private final ServiceSort2 superItem;
    private AddReduceNumListener addReduceNumListener;
    

    public ChooseService2SpecAdapter(ServiceSort2 item, int layoutResId, @Nullable List<ServiceSort2Spec> data) {
        super(layoutResId, data);
        superItem = item;
    }

    @Override
    protected void convert(BaseHolder helper, ServiceSort2Spec item) {
        helper.setText(R.id.tv_service_sub_name, item.name)
                .setText(R.id.tv_service_sub_price, "¥" + CommonUtil.formatPrice(item.price));
        TextView reduce = helper.getView(R.id.tv_service_sub_reduce);
        TextView num = helper.getView(R.id.tv_service_sub_num);
        TextView add = helper.getView(R.id.tv_service_sub_add);
        if (item.number > 0) {
            showReduce(num, reduce);
        } else {
            hideReduce(num, reduce);
        }
        add.setOnClickListener(v -> {
            if (item.number == item.stock) {
                ToastUtils.ToastMessage(mContext, "库存不足！");
                return;
            }
            if (item.number == 0) {
                showReduce(num, reduce);
            }
            item.number = item.number + 1;
            num.setText(item.number + "");
            ChosenServiceEntity entity = new ChosenServiceEntity();
            entity.id = superItem.id;
            entity.subId = item.id;
            entity.number = item.number;
            entity.photo = superItem.photo;
            entity.title = superItem.title;
            entity.spec = item.name;
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
            entity.id = superItem.id;
            entity.subId = item.id;
            entity.number = item.number;
            entity.photo = superItem.photo;
            entity.title = superItem.title;
            entity.spec = item.name;
            entity.price = item.price;
            addReduceNumListener.reduceNum(entity);
        });
    }

    public void setAddReduceNumLis(AddReduceNumListener listener) {
        this.addReduceNumListener = listener;
    }

    private void showReduce(TextView num, TextView reduce) {
        num.setVisibility(View.VISIBLE);
        reduce.setVisibility(View.VISIBLE);
    }

    private void hideReduce(TextView num, TextView reduce) {
        num.setVisibility(View.INVISIBLE);
        reduce.setVisibility(View.INVISIBLE);
    }
}
