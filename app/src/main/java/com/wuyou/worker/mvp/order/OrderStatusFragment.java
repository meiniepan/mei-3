package com.wuyou.worker.mvp.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.common.widget.recyclerHelper.RefreshRecyclerView;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.OrderStatusAdapter;
import com.wuyou.worker.bean.entity.OrderDetailInfoEntity;
import com.wuyou.worker.bean.entity.OrderInfoListEntity;
import com.wuyou.worker.event.OrderChangeEvent;
import com.wuyou.worker.util.MyRecyclerViewScrollListener;
import com.wuyou.worker.view.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by solang on 2018/1/31.
 */

public class OrderStatusFragment extends BaseFragment<OrderContract.View, OrderContract.Presenter> implements OrderContract.View {
    @BindView(R.id.rv_orders)
    RefreshRecyclerView recyclerView;
    @BindView(R.id.rl_to_top)
    View toTop;
    OrderStatusAdapter adapter;
    List<OrderDetailInfoEntity> data = new ArrayList();
    private int orderState;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_status_order;
    }

    @Override
    protected OrderContract.Presenter getPresenter() {
        return new OrderPresenter();
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        recyclerView.getRecyclerView().setErrorAction(v -> {
            recyclerView.showProgressView();
            fetchDatas();
        });
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        final MyRecyclerViewScrollListener scrollListener = new MyRecyclerViewScrollListener(getActivity(), toTop);

        adapter = new OrderStatusAdapter(scrollListener, this, R.layout.item_order_status, data);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra(Constant.ORDER_ID, adapter.getItem(position).order_id);
            intent.putExtra(Constant.DIVIDE_ORDER_FROM, 1);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

//        recyclerView.getRecyclerView().addOnScrollListener(scrollListener);
        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnLoadMoreListener(() -> mPresenter.loadMore(CarefreeApplication.getInstance().getUserInfo().getWorker_id(), orderState+""), recyclerView.getRecyclerView());
        recyclerView.setRefreshAction(() -> {
//            scrollListener.setRefresh();
            fetchDatas();
        });
//        toTop.setOnClickListener(v -> recyclerView.getRecyclerView().smoothScrollToPosition(0));
    }

    @Override
    public void showError(String message, int res) {
        recyclerView.getRefreshLayout().setEnabled(false);
        recyclerView.setRefreshFinished();
        recyclerView.showErrorView(message);
    }

    @Override
    public void getSuccess(OrderInfoListEntity data) {
        recyclerView.getRefreshLayout().setEnabled(true);
        recyclerView.setRefreshFinished();
        adapter.setNewData(data.list);
        recyclerView.showContentView();
        if (data.has_more.equals("0")) {
            adapter.loadMoreEnd(true);
        }
        if (adapter.getData().size() == 0) {
            recyclerView.showEmptyView(getString(R.string.order_empty));
        }
    }

    @Override
    public void getMore(OrderInfoListEntity data) {
        adapter.addData(data.list);
        if (data.has_more.equals("0")) {
            adapter.loadMoreEnd(true);
        }
    }

    @Override
    public void loadMoreError(int code) {
        adapter.loadMoreFail();
    }


    @Override
    public void loadData() {
        recyclerView.showProgressView();
        fetchDatas();
    }

    private void fetchDatas() {
        orderState = getArguments().getInt("h");
        mPresenter.getOrders(CarefreeApplication.getInstance().getUserInfo().getWorker_id(), orderState+"");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderFinished(OrderChangeEvent event) {
       loadData();
    }
}
