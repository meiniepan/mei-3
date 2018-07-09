package com.wuyou.worker.adapter;

import android.support.annotation.Nullable;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.worker.bean.entity.ServiceSort2Entity;
import com.wuyou.worker.bean.entity.ServiceSortConfirmEntity;
import com.wuyou.worker.bean.entity.ServiceSortEntity;

import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class ChooseServiceConfirmAdapter extends BaseQuickAdapter<ServiceSortConfirmEntity, BaseHolder> {

    public ChooseServiceConfirmAdapter(int layoutResId, @Nullable List<ServiceSortConfirmEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ServiceSortConfirmEntity item) {
//        String dispatch = TribeDateUtils.dateFormat(new Date(item.dispatched_at * 1000));
//        helper.setText(R.id.order_status_state, getStatusText(item.status))

    }

}
