package com.wuyou.worker.view.EAEhelper;

import android.view.View;


/**
 * Item点击监听类
 *
 * @author free46000
 */
public abstract class OnItemClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        BaseViewHolder viewHolder = ListenerUtil.getViewHolderByItemView(v);
        if (viewHolder == null) {
            return;
        }

        onItemClick(viewHolder);
    }

    /**
     * 点击回调 可以通过viewHolder get到需要的数据
     */
    public abstract void onItemClick(BaseViewHolder viewHolder);


}
