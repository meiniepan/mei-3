package com.wuyou.worker.mvp.order;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.util.CommonUtil;
import com.wuyou.worker.util.zxing.encoding.QRCode;
import com.wuyou.worker.view.activity.BaseActivity;

import butterknife.BindView;

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

    @Override
    protected int getContentLayout() {
        return R.layout.activity_proceeds_type;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        qrString = getIntent().getStringExtra(Constant.PROCEEDS_QR);
        total = getIntent().getFloatExtra(Constant.CHOSEN_SERVICE_TOTAL, 0F);
        payWay = getIntent().getStringExtra(Constant.EXTRA_PAY_WAY);
        setTitleText("收款二维码");
        tvPayType.setText(payWay);
        tvProceedsSum.setText(CommonUtil.formatPrice(total));
        ivProceedsQr.setImageBitmap(QRCode.createQRCode(qrString));
    }

}
