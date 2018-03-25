package com.wuyou.worker.mvp.wallet;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.WalletTransactionAdapter;
import com.wuyou.worker.bean.ListResponse;
import com.wuyou.worker.bean.entity.WalletTransactionEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.MoneyApis;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.view.activity.BaseActivity;
import com.wuyou.worker.view.widget.recyclerHelper.NewRefreshRecyclerView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/3/25.
 */

public class WalletTransactionActivity extends BaseActivity {
    @BindView(R.id.wallet_transaction_list)
    NewRefreshRecyclerView recyclerView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_wallet_transaction;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getRecyclerView().addItemDecoration(CommonUtil.getRecyclerDivider(this));
        getData();
        recyclerView.setRefreshAction(() -> getData());
    }

    private void getData() {
        CarefreeRetrofit.getInstance().createApi(MoneyApis.class)
                .getWalletTransaction(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ListResponse<WalletTransactionEntity>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ListResponse<WalletTransactionEntity>> listResponseBaseResponse) {
                        setData(listResponseBaseResponse.data.list);
                    }
                });
    }

    private void setData(List<WalletTransactionEntity> data) {
        WalletTransactionAdapter adapter = new WalletTransactionAdapter(R.layout.item_wallet_transaction, data);
        recyclerView.setAdapter(adapter);
    }
}
