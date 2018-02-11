package com.wuyou.worker.mvp.order;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.worker.bean.entity.OrderInfoListEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class OrderPresenter extends OrderContract.Presenter {
    String lastId;

    @Override
    void getOrders(String merchant_id, String status) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getOrders(merchant_id, status, "0", "1", QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderInfoListEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderInfoListEntity> userInfoBaseResponse) {
                        if (userInfoBaseResponse.data.list.size() > 0)
                            lastId = userInfoBaseResponse.data.list.get(userInfoBaseResponse.data.list.size() - 1).id;
                        if (isAttach()) mView.getSuccess(userInfoBaseResponse.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }

    @Override
    void getAllianceOrders(String merchant_id, String status) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getAllianceOrders(merchant_id, status, "0", "1", QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderInfoListEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderInfoListEntity> userInfoBaseResponse) {
                        if (userInfoBaseResponse.data.list.size() > 0)
                            lastId = userInfoBaseResponse.data.list.get(userInfoBaseResponse.data.list.size() - 1).id;
                        if (isAttach()) mView.getSuccess(userInfoBaseResponse.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError(e.getDisplayMessage(), e.getCode());
                    }
                });
    }

    @Override
    void loadMore(String merchant_id, String status) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getOrders(merchant_id, status, lastId, "2", QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderInfoListEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderInfoListEntity> userInfoBaseResponse) {
                        if (userInfoBaseResponse.data.list.size() > 0)
                            lastId = userInfoBaseResponse.data.list.get(userInfoBaseResponse.data.list.size() - 1).id;
                        if (isAttach()) mView.getMore(userInfoBaseResponse.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach())mView.loadMoreError(e.getCode());
                    }
                });
    }

    @Override
    void loadAllianceMore(String merchant_id, String status) {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getAllianceOrders(merchant_id, status, lastId, "2", QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderInfoListEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<OrderInfoListEntity> userInfoBaseResponse) {
                        if (userInfoBaseResponse.data.list.size() > 0)
                            lastId = userInfoBaseResponse.data.list.get(userInfoBaseResponse.data.list.size() - 1).id;
                        if (isAttach()) mView.getMore(userInfoBaseResponse.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach())mView.loadMoreError(e.getCode());
                    }
                });
    }


}
