package com.wuyou.worker.adapter;

import android.support.annotation.Nullable;

import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.WalletTransactionEntity;
import com.wuyou.worker.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.Date;
import java.util.List;

/**
 * Created by DELL on 2018/3/25.
 */

public class WalletTransactionAdapter extends BaseQuickAdapter<WalletTransactionEntity, BaseHolder> {
    public WalletTransactionAdapter(int layoutResId, @Nullable List<WalletTransactionEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, WalletTransactionEntity item) {
        helper.setText(R.id.item_wallet_transaction_time, TribeDateUtils.dateFormat(new Date(item.time * 1000)))
                .setText(R.id.item_wallet_transaction_amount, item.amount + "")
                .setText(R.id.item_wallet_transaction_name, item.account + "");
    }
}
