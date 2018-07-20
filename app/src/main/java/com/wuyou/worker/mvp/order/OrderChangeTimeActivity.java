package com.wuyou.worker.mvp.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.bean.ServeTimeBean;
import com.wuyou.worker.bean.entity.AddressEntity;
import com.wuyou.worker.event.OrderChangeEvent;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.util.RxUtil;
import com.wuyou.worker.view.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.LinkagePicker;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2018/5/22.
 */

public class OrderChangeTimeActivity extends BaseActivity {
    @BindView(R.id.order_change_time_address)
    TextView orderChangeTimeAddress;
    @BindView(R.id.order_change_time_phone)
    TextView orderChangeTimePhone;
    @BindView(R.id.order_change_time_name)
    TextView orderChangeTimeName;
    @BindView(R.id.order_change_time_text)
    TextView orderChangeTimeText;
    private String serveDate;
    private String serveTime;
    private String orderId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_change_time;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        AddressEntity addressEntity = getIntent().getParcelableExtra(Constant.ADDRESS_BEAN);
        orderChangeTimeName.setText(addressEntity.name);
        orderChangeTimeAddress.setText(String.format("%s%s%s", addressEntity.city, addressEntity.district, addressEntity.area));
        orderChangeTimePhone.setText(addressEntity.mobile);
        getServeTime(orderId);
    }
    private void getServeTime(String orderId) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class).getAvailableServeTime(orderId, QueryMapBuilder.getIns().put("worker_id", CarefreeDaoSession.getInstance().getUserId()).buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ArrayMap<String, List<ServeTimeBean>>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayMap<String, List<ServeTimeBean>>> hashMapBaseResponse) {
                        timeMap = hashMapBaseResponse.data;
                    }
                });
    }


    public void commitChangeTime(View view) {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(OrderApis.class).updateServeTime(QueryMapBuilder.getIns().put("order_id", orderId)
                .put("service_date", serveDate).put("service_time", serveTime).buildPost())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        ToastUtils.ToastMessage(getCtx(), R.string.update_success);
                        EventBus.getDefault().post(new OrderChangeEvent(serveDate, serveTime));
                        finish();
                    }
                });
    }

    private ArrayMap<String, List<ServeTimeBean>> timeMap = new ArrayMap<>();

    @OnClick(R.id.order_change_time_area)
    public void onViewClicked() {
        chooseServeTime();
    }

    private void chooseServeTime() {
        if (timeMap == null) showLoadingDialog();
        if (timeMap.size() == 0) return;
        ArrayList<String> firstData = new ArrayList<>();
        firstData.addAll(timeMap.keySet());
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {
            @Override
            public boolean isOnlyTwo() {
                return true;
            }

            @NonNull
            @Override
            public List<String> provideFirstData() {
                return firstData;
            }

            ArrayList<String> secondData;

            @NonNull
            @Override
            public List<String> provideSecondData(int firstIndex) {
                secondData = new ArrayList<>();
                List<ServeTimeBean> timeBeans = timeMap.get(firstData.get(firstIndex));
                for (ServeTimeBean bean : timeBeans) {
                    if (bean.status != 0) {
                        secondData.add(bean.time);
                    }
                }
                if (secondData.size() == 0) {
                    secondData.add("当日已无可更改时间");
                }
                return secondData;
            }

            @Nullable
            @Override
            public List<String> provideThirdData(int firstIndex, int secondIndex) {
                return null;
            }
        };
        LinkagePicker picker = new LinkagePicker(this, provider);
        picker.setCycleDisable(true);
        picker.setUseWeight(true);
        picker.setContentPadding(10, 0);
        picker.setOnStringPickListener(new LinkagePicker.OnStringPickListener() {
            @Override
            public void onPicked(String first, String second, String third) {
                orderChangeTimeText.setText(first + "  " + second);
                serveDate = first;
                serveTime = second;
            }
        });
        picker.show();
    }
}
