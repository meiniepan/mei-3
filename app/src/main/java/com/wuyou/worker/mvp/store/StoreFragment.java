package com.wuyou.worker.mvp.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyou.worker.R;
import com.wuyou.worker.view.activity.OrderStatisticsActivity;
import com.wuyou.worker.view.activity.SettingActivity;
import com.wuyou.worker.view.activity.WorkerInfoActivity;
import com.wuyou.worker.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class StoreFragment extends BaseFragment {
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.mine_name)
    TextView mineName;
    @BindView(R.id.mine_phone)
    TextView minePhone;

    @Override
    protected int getContentLayout() {
        return R.layout.store_home;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    public void showError(String message, int res) {

    }

    @OnClick({R.id.mine_setting, R.id.mine_order_statistics, R.id.mine_info})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mine_setting:
                intent.setClass(mCtx, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_order_statistics:
                intent.setClass(mCtx, OrderStatisticsActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_info:
                intent.setClass(mCtx, WorkerInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
