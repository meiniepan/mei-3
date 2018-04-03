package com.wuyou.worker.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.PartnerEntity;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class PartnerListRvAdapter extends BaseQuickAdapter<PartnerEntity, BaseHolder> {
    private Activity activity;

    public PartnerListRvAdapter(Activity activity, int layoutResId, @Nullable List<PartnerEntity> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseHolder helper, PartnerEntity item) {
        helper.setText(R.id.tv_name, item.username)
        .setText(R.id.tv_distance,item.distance);
        ImageView imageView = helper.getView(R.id.avatar);
        CommonUtil.glideCircleLoad(activity, item.head_image, imageView);
    }
}
