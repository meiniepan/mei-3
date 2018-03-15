package com.wuyou.worker.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.ContractContentRvAdapter;
import com.wuyou.worker.bean.entity.ContractContentEntity;
import com.wuyou.worker.bean.entity.ContractDetailEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by solang on 2018/2/5.
 */

public class ContractDetailActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_times)
    TextView tvTimes;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    String id;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_contract_detail;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        id = getIntent().getStringExtra(Constant.CONTRACT_ID);
        initData();
    }

    private void initData() {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getContractDetail(id, QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ContractDetailEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<ContractDetailEntity> response) {
                        initUI(response.data);
                    }

                });
    }

    private void initUI(ContractDetailEntity data) {
        String b_time = TribeDateUtils.dateFormat5(new Date(data.start_at * 1000));
        String e_time = TribeDateUtils.dateFormat5(new Date(data.end_at * 1000));
        tvName.setText(data.name);
        tvTime.setText(b_time + " 至 " + e_time);
        tvTimes.setText(data.times);
        tvRule.setText(data.divided);
        if (data.content != null) {
            initContentList(data.content);
        }
    }

    private void initContentList(List<ContractContentEntity> content) {
        ContractContentRvAdapter adapter = new ContractContentRvAdapter(R.layout.item_contract_content,content);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(adapter);
    }


}
