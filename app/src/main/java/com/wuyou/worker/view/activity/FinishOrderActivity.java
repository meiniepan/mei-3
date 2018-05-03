package com.wuyou.worker.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.event.OrderFinishEvent;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/3/22.
 */

public class FinishOrderActivity extends BaseActivity {

    @BindView(R.id.finish_order_account)
    TextView finishOrderAccount;
    @BindView(R.id.finish_order_extra)
    Switch finishOrderExtra;
    @BindView(R.id.finish_order_extra_fee)
    EditText finishOrderExtraFee;
    @BindView(R.id.finish_order_fee_area)
    LinearLayout finishOrderFeeArea;
    private OrderInfoEntity infoEntity;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_finish_order;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        infoEntity = getIntent().getParcelableExtra(Constant.ORDER_INFO);
        finishOrderAccount.setText(infoEntity.price);
        finishOrderExtra.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                finishOrderFeeArea.setVisibility(View.VISIBLE);
            } else {
                finishOrderFeeArea.setVisibility(View.GONE);
            }
        });
        CommonUtil.setEdDecimal(finishOrderExtraFee, 2);
    }

    public void finishOrder(View view) {
        if (finishOrderExtra.isChecked() && finishOrderExtraFee.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), "请填写附加金额");
            return;
        }
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .finish(QueryMapBuilder.getIns().put("worker_id", CarefreeApplication.getInstance().getUserInfo().getWorker_id())
                        .put("order_id", infoEntity.order_id)
                        .put("second_payment", finishOrderExtraFee.getText().toString().trim()).buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ToastUtils.ToastMessage(getCtx(), "操作成功！");
                        EventBus.getDefault().post(new OrderFinishEvent(infoEntity.position));
                        finish();
                    }
                });
    }
}
