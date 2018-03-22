package com.wuyou.worker.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/3/22.
 */

public class FinishOrderActivity extends BaseActivity {

    @BindView(R.id.finish_order_account)
    TextView finishOrderAccount;
    @BindView(R.id.finish_order_extra)
    RadioButton finishOrderExtra;
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
    }

    public void finishOrder(View view) {
        //TODO  完成订单时 增加金额
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .finish(CarefreeApplication.getInstance().getUserInfo().getUid(), infoEntity.id, QueryMapBuilder.getIns().buildPost())
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
