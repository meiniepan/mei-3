package com.wuyou.worker.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.ServiceSortEntity;

import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class ChooseServiceAdapter extends BaseQuickAdapter<ServiceSortEntity, BaseHolder> {

    public ChooseServiceAdapter(int layoutResId, @Nullable List<ServiceSortEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ServiceSortEntity item) {
        TextView textView = helper.getView(R.id.tv_service_sort);
        textView.setText(item.category_name);
        if (item.click) {
            textView.setBackgroundColor(mContext.getResources().getColor(R.color.tint_bg));
            textView.setTextColor(mContext.getResources().getColor(R.color.common_dark));
        } else {
            textView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            textView.setTextColor(mContext.getResources().getColor(R.color.common_gray));
        }

    }

}
