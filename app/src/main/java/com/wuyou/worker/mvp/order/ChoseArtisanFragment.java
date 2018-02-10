package com.wuyou.worker.mvp.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.gs.buluo.common.widget.StatusLayout;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.Constant;
import com.wuyou.worker.R;
import com.wuyou.worker.adapter.WorkersRvAdapter;
import com.wuyou.worker.bean.entity.WorkerEntity;
import com.wuyou.worker.bean.entity.WorkerListEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;
import com.wuyou.worker.view.activity.MainActivity;
import com.wuyou.worker.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by solang on 2018/1/31.
 */

public class ChoseArtisanFragment extends BaseFragment {
    @BindView(R.id.sl_list_layout)
    StatusLayout statusLayout;
    @BindView(R.id.rv_orders)
    RecyclerView recyclerView;
    List<WorkerEntity> data = new ArrayList();
    WorkersRvAdapter adapter;
    String orderId;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_chose_artisan;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        orderId = getActivity().getIntent().getStringExtra(Constant.ORDER_ID);
        adapter = new WorkersRvAdapter(getActivity(), R.layout.item_chose_artisan, data);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            showAlert(adapter.getItem(position).name,adapter.getItem(position).id);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        statusLayout.showProgressView();
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getWorkersInfo(CarefreeApplication.getInstance().getUserInfo().getUid(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WorkerListEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<WorkerListEntity> response) {
                        adapter.setNewData(response.data.list);
                        statusLayout.showContentView();
                        if (adapter.getData().size() == 0) {
                            statusLayout.showEmptyView("没有名单");
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        statusLayout.showErrorView(e.getDisplayMessage());
                    }
                });
    }

    private void showAlert(String name,String serverId) {
        CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(getContext());
        builder.setTitle("是否分单给服务者").setMessage(name);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                        .dispatchOrder(CarefreeApplication.getInstance().getUserInfo().getUid(),
                                QueryMapBuilder.getIns().put("order_id", orderId)
                                        .put("receiver_id", serverId)
                                        .put("type", "1")
                                        .buildPost())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseSubscriber<BaseResponse>() {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ToastUtils.ToastMessage(getContext(), "完成");
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                getActivity().startActivity(intent);
                            }

                        });

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    public void showError(String message, int res) {

    }
}
