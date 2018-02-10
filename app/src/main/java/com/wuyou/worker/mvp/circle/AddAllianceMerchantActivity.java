package com.wuyou.worker.mvp.circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.DispatchMerchantListRvAdapter;
import com.wuyou.worker.bean.entity.PartnerListEntity;
import com.wuyou.worker.bean.entity.WorkerEntity;
import com.wuyou.worker.bean.entity.WorkerListEntity;
import com.wuyou.worker.util.MyRecyclerViewScrollListener;
import com.wuyou.worker.view.activity.BaseActivity;
import com.wuyou.worker.view.activity.ServiceProviderDetailActivity;
import com.wuyou.worker.view.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.worker.view.widget.recyclerHelper.NewRefreshRecyclerView;
import com.wuyou.worker.view.widget.recyclerHelper.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by solang on 2018/2/10.
 */

public class AddAllianceMerchantActivity extends BaseActivity<CircleContract.View,CircleContract.Presenter>implements CircleContract.View {
    @BindView(R.id.rv_orders)
    NewRefreshRecyclerView recyclerView;
    @BindView(R.id.sl_list_layout)
    StatusLayout statusLayout;
    @BindView(R.id.rl_to_top)
    View toTop;
    DispatchMerchantListRvAdapter adapter;
    List<WorkerEntity> data = new ArrayList();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_alliance_merchant;
    }

    @Override
    protected CircleContract.Presenter getPresenter() {
        return new CirclePresenter();
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        statusLayout.showProgressView();
        fetchDatas();
        adapter = new DispatchMerchantListRvAdapter(this, R.layout.item_chose_merchant, data);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Intent intent = new Intent(this, ServiceProviderDetailActivity.class);
            intent.putExtra(Constant.MERCHANT_ID, adapter.getItem(position).id);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        final MyRecyclerViewScrollListener scrollListener = new MyRecyclerViewScrollListener(this, toTop);
        recyclerView.getRecyclerView().addOnScrollListener(scrollListener);
        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getCtx()));
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMore();
            }
        }, recyclerView.getRecyclerView());
        recyclerView.setRefreshAction(new OnRefreshListener() {
            @Override
            public void onAction() {
                scrollListener.setRefresh();
                adapter.clearData();
                fetchDatas();
            }
        });
        toTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.getRecyclerView().smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void showError(String message, int res) {
        recyclerView.setRefreshFinished();
        statusLayout.showErrorView(message);
    }

    @Override
    public void getSuccess(WorkerListEntity data) {
        recyclerView.setRefreshFinished();
        adapter.setNewData(data.list);
        statusLayout.showContentView();
        if (data.has_more.equals("0")) {
            adapter.loadMoreEnd(true);
        }
        if (adapter.getData().size() == 0) {
            statusLayout.showEmptyView("没有订单");
        }
    }

    @Override
    public void getPartnerSuccess(PartnerListEntity data) {

    }

    @Override
    public void getMore(WorkerListEntity data) {
        adapter.addData(data.list);
        if (data.has_more.equals("0")) {
            adapter.loadMoreEnd(true);
        }
    }


    @Override
    public void loadMoreError(int code) {
        adapter.loadMoreFail();
    }






    private void fetchDatas() {
        mPresenter.getPrepareMerchants();
    }
}
