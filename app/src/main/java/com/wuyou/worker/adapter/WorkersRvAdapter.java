package com.wuyou.worker.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.WorkerEntity;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class WorkersRvAdapter extends BaseQuickAdapter<WorkerEntity, BaseHolder> {
    private Activity activity;

    public WorkersRvAdapter(Activity activity, int layoutResId, @Nullable List<WorkerEntity> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseHolder helper, WorkerEntity item) {
        helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_distance, item.distance);
        ImageView imageView = helper.getView(R.id.avatar);
        CommonUtil.glideCircleLoad(activity,item.image,imageView);
    }
}
