package com.wuyou.worker.mvp.circle;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.PartnerListRvAdapter;
import com.wuyou.worker.bean.entity.PartnerEntity;
import com.wuyou.worker.bean.entity.PartnerListEntity;
import com.wuyou.worker.bean.entity.WorkerListEntity;
import com.wuyou.worker.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by solang on 2018/2/5.
 */

public class PartnerFragment extends BaseFragment<CircleContract.View, CircleContract.Presenter> implements CircleContract.View {
    @BindView(R.id.sl_list_layout)
    StatusLayout statusLayout;
    @BindView(R.id.rv_orders)
    RecyclerView recyclerView;
    List<PartnerEntity> data = new ArrayList();
    PartnerListRvAdapter adapter;
    String id;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_joined;
    }

    @Override
    protected CircleContract.Presenter getPresenter() {
        return new CirclePresenter();
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        adapter = new PartnerListRvAdapter(getActivity(), R.layout.item_chose_artisan, data);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
//            Intent intent = new Intent(getActivity(), ServiceProviderDetailActivity.class);
//            intent.putExtra(Constant.MERCHANT_ID, adapter.getItem(position).id);
//            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void loadData() {
        statusLayout.showProgressView();
        fetchDatas();
    }

    private void fetchDatas() {
        mPresenter.getPartner();
    }

    @Override
    public void showError(String message, int res) {
        statusLayout.showErrorView(message);
    }

    @Override
    public void getSuccess(WorkerListEntity data) {

    }

    @Override
    public void getPartnerSuccess(PartnerListEntity data) {
        adapter.setNewData(data.list);
        statusLayout.showContentView();

        if (adapter.getData().size() == 0) {
            statusLayout.showEmptyView("没有合伙人");
        }
    }

    @Override
    public void getMore(WorkerListEntity data) {

    }

    @Override
    public void loadMoreError(int code) {

    }
}
