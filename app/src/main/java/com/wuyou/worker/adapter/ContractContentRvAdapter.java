package com.wuyou.worker.adapter;

import android.support.annotation.Nullable;

import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.ContractContentEntity;
import com.wuyou.worker.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class ContractContentRvAdapter extends BaseQuickAdapter<ContractContentEntity, BaseHolder> {

    public ContractContentRvAdapter(int layoutResId, @Nullable List<ContractContentEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ContractContentEntity item) {
        helper.setText(R.id.title, item.title)
                .setText(R.id.content, item.content);
    }
}
