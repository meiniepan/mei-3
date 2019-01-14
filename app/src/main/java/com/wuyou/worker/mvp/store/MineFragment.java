package com.wuyou.worker.mvp.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.UserInfo;
import com.wuyou.worker.bean.entity.WalletInfoEntity;
import com.wuyou.worker.mvp.info.WorkerInfoActivity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.MoneyApis;
import com.wuyou.worker.network.apis.UserApis;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.util.GlideUtils;
import com.wuyou.worker.util.RxUtil;
import com.wuyou.worker.view.activity.IdentifyActivity;
import com.wuyou.worker.view.activity.SettingActivity;
import com.wuyou.worker.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.mine_name)
    TextView mineName;
    @BindView(R.id.mine_phone)
    TextView minePhone;
    @BindView(R.id.mine_total_money)
    TextView mineTotal;

    @Override
    protected int getContentLayout() {
        return R.layout.store_home;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getBalanceAndInfo();
    }

    private void getBalanceAndInfo() {
        GlideUtils.loadImageNoHolder(getContext(), CarefreeDaoSession.getAvatar(CarefreeDaoSession.getInstance().getUserInfo()), imageView, true);
        mineName.setText(CarefreeDaoSession.getInstance().getUserInfo().getName());
        minePhone.setText(CommonUtil.getPhoneWithStar(CarefreeDaoSession.getInstance().getUserInfo().getMobile()));
        mineTotal.setText(CommonUtil.formatPrice(Float.valueOf(CarefreeDaoSession.getInstance().getUserInfo().getAmount())));
    }

    @Override
    public void showError(String message, int res) {

    }

    @OnClick({R.id.mine_setting, R.id.mine_order_statistics, R.id.mine_info, R.id.mine_auth, R.id.mine_introduction, R.id.mine_score})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mine_setting:
                intent.setClass(mCtx, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_order_statistics:
                ToastUtils.ToastMessage(mCtx, R.string.no_function);
//                intent.setClass(mCtx, OrderStatisticsActivity.class);
//                startActivity(intent);
                break;
            case R.id.mine_info:
                intent.setClass(mCtx, WorkerInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_auth:
                intent.setClass(mCtx, IdentifyActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_introduction:
            case R.id.mine_score:
                break;
        }
    }
}
