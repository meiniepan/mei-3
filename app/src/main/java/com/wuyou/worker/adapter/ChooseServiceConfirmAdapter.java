package com.wuyou.worker.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.ChosenServiceEntity;
import com.wuyou.worker.bean.entity.ServiceSort2Entity;
import com.wuyou.worker.bean.entity.ServiceSortConfirmEntity;
import com.wuyou.worker.bean.entity.ServiceSortEntity;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.util.GlideUtils;

import java.util.List;

/**
 * Created by solang on 2018/2/5.
 */

public class ChooseServiceConfirmAdapter extends BaseQuickAdapter<ChosenServiceEntity, BaseHolder> {

    public ChooseServiceConfirmAdapter(int layoutResId, @Nullable List<ChosenServiceEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ChosenServiceEntity item) {
        ImageView imageView = helper.getView(R.id.iv_service_confirm);
        GlideUtils.loadRoundCornerImageWithBitmap(mContext, item.photo, imageView);
        helper.setText(R.id.tv_name2, item.title).setText(R.id.tv_service_confirm_num, "x "+item.number)
                .setText(R.id.tv_service_confirm_sub_name, item.spec)
                .setText(R.id.tv_service_confirm_price, CommonUtil.formatPrice(item.price));

    }

}
