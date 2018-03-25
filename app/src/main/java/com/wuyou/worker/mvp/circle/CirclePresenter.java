package com.wuyou.worker.mvp.circle;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.wuyou.worker.CarefreeApplication;
import com.wuyou.worker.bean.entity.PartnerListEntity;
import com.wuyou.worker.bean.entity.WorkerListEntity;
import com.wuyou.worker.network.CarefreeRetrofit;
import com.wuyou.worker.network.apis.OrderApis;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class CirclePresenter extends CircleContract.Presenter {
    String lastId;


    @Override
    void getSponsorAlliance() {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getDispatchMerchantInfo(CarefreeApplication.getInstance().getUserInfo().getWorker_id(), "launch", QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WorkerListEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<WorkerListEntity> response) {
                        if (isAttach()) mView.getSuccess(response.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError(e.getDisplayMessage(),e.getCode());
                    }
                });
    }

    @Override
    void getJoinedAlliance() {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getDispatchMerchantInfo(CarefreeApplication.getInstance().getUserInfo().getWorker_id(), "join", QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WorkerListEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<WorkerListEntity> response) {
                        if (isAttach()) mView.getSuccess(response.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError(e.getDisplayMessage(),e.getCode());
                    }
                });
    }

    @Override
    void getPartner() {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getUnionPartner(CarefreeApplication.getInstance().getUserInfo().getWorker_id(), QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<PartnerListEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<PartnerListEntity> response) {
                        if (isAttach()) mView.getPartnerSuccess(response.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError(e.getDisplayMessage(),e.getCode());
                    }
                });
    }

    @Override
    void getPrepareMerchants() {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getPrepareMerchantInfo(CarefreeApplication.getInstance().getUserInfo().getWorker_id(), "0","1", QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WorkerListEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<WorkerListEntity> response) {
                        if (response.data.list.size() > 0)
                            lastId = response.data.list.get(response.data.list.size() - 1).id;
                        if (isAttach()) mView.getSuccess(response.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.showError(e.getDisplayMessage(),e.getCode());
                    }
                });
    }

    @Override
    void loadMore() {
        CarefreeRetrofit.getInstance().createApi(OrderApis.class)
                .getPrepareMerchantInfo(CarefreeApplication.getInstance().getUserInfo().getWorker_id(), lastId,"2", QueryMapBuilder.getIns().buildGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WorkerListEntity>>() {
                    @Override
                    public void onSuccess(BaseResponse<WorkerListEntity> response) {
                        if (response.data.list.size() > 0)
                            lastId = response.data.list.get(response.data.list.size() - 1).id;
                        if (isAttach()) mView.getMore(response.data);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        if (isAttach()) mView.loadMoreError(e.getCode());
                    }
                });
    }
}
