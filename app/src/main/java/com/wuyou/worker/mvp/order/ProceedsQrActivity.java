package com.wuyou.worker.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.IsPayedEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.util.zxing.encoding.QRCode;
import com.wuyou.worker.view.activity.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Solang on 2018/7/10.
 */

public class ProceedsQrActivity extends BaseActivity {
    @BindView(R.id.tv_proceeds_sum)
    TextView tvProceedsSum;
    @BindView(R.id.iv_proceeds_qr)
    ImageView ivProceedsQr;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    String qrString;
    float total;
    String payWay;
    String orderId;
    private Disposable mDisposable;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_proceeds_type;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        qrString = getIntent().getStringExtra(Constant.PROCEEDS_QR);
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        total = getIntent().getFloatExtra(Constant.CHOSEN_SERVICE_TOTAL, 0F);
        payWay = getIntent().getStringExtra(Constant.EXTRA_PAY_WAY);
        setTitleText("收款二维码");
        tvPayType.setText(payWay);
        tvProceedsSum.setText(CommonUtil.formatPrice(total));
        ivProceedsQr.setImageBitmap(QRCode.createQRCode(qrString));
        queryTimer();
    }

    private void queryTimer() {
        Observable.interval(2, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .flatMap(aLong ->
                        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                                .getIsPayed(orderId, QueryMapBuilder.getIns().put("worker_id", CarefreeDaoSession.getInstance().getUserInfo().getWorker_id()).put("payment_channel", payWay).buildGet()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<IsPayedEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onSuccess(BaseResponse<IsPayedEntity> response) {
                        if (response.data.is_paid.equals("1")) {
                            Intent intent = new Intent(getCtx(), PayDoneActivity.class);
                            intent.putExtra(Constant.ORDER_ID, orderId);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        super.onDestroy();
    }
}
