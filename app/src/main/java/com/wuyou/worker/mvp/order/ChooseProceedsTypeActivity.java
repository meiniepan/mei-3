package com.wuyou.worker.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.QrEntity;
import com.wuyou.worker.bean.entity.ServiceSort2Entity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Solang on 2018/7/5.
 */

public class ChooseProceedsTypeActivity extends BaseActivity {


    @BindView(R.id.iv_alipay)
    ImageView ivAlipay;
    @BindView(R.id.ll_alipay)
    LinearLayout llAlipay;
    @BindView(R.id.iv_wechat)
    ImageView ivWechat;
    @BindView(R.id.ll_wechat)
    LinearLayout llWechat;
    String payWay = "1";
    String orderId;
    float total;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_choose_proceeds_type;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        total = getIntent().getFloatExtra(Constant.CHOSEN_SERVICE_TOTAL, 0F);
        setTitleText("选择收款方式");
    }

    @OnClick({R.id.ll_alipay, R.id.ll_wechat, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_alipay:
                payWay = "1";
                ivAlipay.setBackground(getResources().getDrawable(R.mipmap.icon_selected));
                ivWechat.setBackground(getResources().getDrawable(R.mipmap.icon_un_selected));
                break;
            case R.id.ll_wechat:
                payWay = "2";
                ivAlipay.setBackground(getResources().getDrawable(R.mipmap.icon_un_selected));
                ivWechat.setBackground(getResources().getDrawable(R.mipmap.icon_selected));
                break;
            case R.id.btn_next:
                goNext();
                break;
        }
    }

    private void goNext() {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getExtraPayQr(orderId, QueryMapBuilder.getIns().put("worker_id", CarefreeDaoSession.getInstance().getUserInfo().getWorker_id()).put("payment_channel", payWay).buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<QrEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<QrEntity> response) {
                        String qrString = response.data.qr_code;
                        Intent intent = new Intent(getCtx(), ProceedsQrActivity.class);
                        intent.putExtra(Constant.CHOSEN_SERVICE_TOTAL, total);
                        if (payWay.equals("1")) {
                            intent.putExtra(Constant.EXTRA_PAY_WAY, "支付宝");
                        } else {
                            intent.putExtra(Constant.EXTRA_PAY_WAY, "微信");
                        }

                        intent.putExtra(Constant.PROCEEDS_QR, qrString);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
