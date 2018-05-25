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
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .getUserInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .doOnNext(userInfoBaseResponse -> {
                    UserInfo newUserInfo = userInfoBaseResponse.data;
                    UserInfo userInfo = CarefreeDaoSession.getInstance().getUserInfo();
                    newUserInfo.setMid(userInfo.getMid());
                    newUserInfo.setToken(userInfo.getToken());
                    CarefreeDaoSession.getInstance().updateUserInfo(newUserInfo);
                })
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse) {
                        UserInfo data = userInfoBaseResponse.data;
                        minePhone.setText(data.getMobile());
                        mineName.setText(data.getWorker_name());
                        GlideUtils.loadImage(getContext(), CarefreeDaoSession.getAvatar(data), imageView, true);
                    }
                });
        CarefreeRetrofit.getInstance().createApi(MoneyApis.class).getWalletAccount(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<WalletInfoEntity> response) {
                        mineTotal.setText(CommonUtil.formatPrice(response.data.balance));
                    }
                });
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
