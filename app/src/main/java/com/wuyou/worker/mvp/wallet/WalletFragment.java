package com.wuyou.worker.mvp.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.WalletInfoEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.MoneyApis;
import com.wuyou.worker.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class WalletFragment extends BaseFragment {


    @BindView(R.id.wallet_account)
    TextView walletAccount;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_wallet;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    public void fetchData() {
        CarefreeRetrofit.getInstance().createApi(MoneyApis.class).getWalletAccount(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<WalletInfoEntity> response) {
                        setData(response.data);
                    }
                });
    }

    @Override
    public void showError(String message, int res) {

    }

    @OnClick({R.id.wallet_recharge, R.id.wallet_withdraw, R.id.wallet_detail})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.wallet_recharge:
            case R.id.wallet_withdraw:
                ToastUtils.ToastMessage(mCtx, R.string.no_function);
                break;
            case R.id.wallet_detail:
                intent.setClass(mCtx, WalletTransactionActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void setData(WalletInfoEntity data) {
        walletAccount.setText(data.balance + "");
    }
}
