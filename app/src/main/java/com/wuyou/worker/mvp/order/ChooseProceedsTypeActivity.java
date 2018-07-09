package com.wuyou.worker.mvp.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.wuyou.worker.R;
import com.wuyou.worker.adapter.ChooseServiceConfirmAdapter;
import com.wuyou.worker.bean.entity.ServiceSortConfirmEntity;
import com.wuyou.worker.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/7/5.
 */

public class ChooseProceedsTypeActivity extends BaseActivity {


    @Override
    protected int getContentLayout() {
        return R.layout.activity_choose_proceeds_type;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText("选择收款方式");
    }


}
