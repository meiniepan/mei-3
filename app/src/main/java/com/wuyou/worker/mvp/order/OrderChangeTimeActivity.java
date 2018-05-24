package com.wuyou.worker.mvp.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import com.wuyou.worker.R;
import com.wuyou.worker.bean.ServeTimeBean;
import com.wuyou.worker.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.LinkagePicker;

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

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_change_time;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    public void commitChangeTime(View view) {

    }

    private ArrayMap<String, List<ServeTimeBean>> timeMap =new ArrayMap<>();

    @OnClick(R.id.order_change_time_area)
    public void onViewClicked() {
        chooseServeTime();
    }

    private void chooseServeTime() {
        ArrayList list= new ArrayList();
        list.add("1234");
        list.add("1234");
        list.add("1234");
        list.add("1234");
        list.add("1234");
        list.add("1234");
        list.add("1234");
//        ArrayList<String> firstData = new ArrayList<>();
//        firstData.addAll(timeMap.keySet());
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {
            @Override
            public boolean isOnlyTwo() {
                return true;
            }

            @NonNull
            @Override
            public List<String> provideFirstData() {
                return list;
            }

            ArrayList<String> secondData;

            @NonNull
            @Override
            public List<String> provideSecondData(int firstIndex) {
//                secondData = new ArrayList<>();
//                List<ServeTimeBean> timeBeans = timeMap.get(firstData.get(firstIndex));
//                for (ServeTimeBean bean : timeBeans) {
//                    if (bean.status == 1) {
//                        secondData.add(bean.time + "(时间段已被选)");
//                    } else {
//                        secondData.add(bean.time);
//                    }
//                }
                return list;
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
//        picker.setLabel("小时制", "点");
//        picker.setSelectedIndex(0, 8);
        //picker.setSelectedItem("12", "9");
        picker.setContentPadding(10, 0);
        picker.setOnStringPickListener(new LinkagePicker.OnStringPickListener() {
            @Override
            public void onPicked(String first, String second, String third) {
                orderChangeTimeText.setText(first + "  " + second);
            }
        });
        picker.show();
    }
}
