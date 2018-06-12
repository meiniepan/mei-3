package com.wuyou.worker.mvp.order;


import com.wuyou.worker.bean.entity.OrderInfoListEntity;
import com.wuyou.worker.mvp.BasePresenter;
import com.wuyou.worker.mvp.IBaseView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public interface OrderContract {
    interface View extends IBaseView {
        void getSuccess(OrderInfoListEntity data);
        void getMore(OrderInfoListEntity data);
        void loadMoreError(int code);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void getOrders(String merchant_id, String status);

        abstract void loadMore(String merchant_id, String status);
    }
}
