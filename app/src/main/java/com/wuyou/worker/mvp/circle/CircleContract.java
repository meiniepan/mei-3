package com.wuyou.worker.mvp.circle;


import com.wuyou.worker.bean.entity.PartnerListEntity;
import com.wuyou.worker.bean.entity.WorkerListEntity;
import com.wuyou.worker.mvp.BasePresenter;
import com.wuyou.worker.mvp.IBaseView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public interface CircleContract {
    interface View extends IBaseView {
        void getSuccess(WorkerListEntity data);
        void getPartnerSuccess(PartnerListEntity data);
        void getMore(WorkerListEntity data);
        void loadMoreError(int code);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void getSponsorAlliance();
        abstract void getJoinedAlliance();
        abstract void getPartner();
        abstract void getPrepareMerchants();
        abstract void loadMore();
    }
}
