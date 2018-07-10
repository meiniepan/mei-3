package com.wuyou.worker.mvp.order;

import com.wuyou.worker.bean.entity.ChosenServiceEntity;

/**
 * Created by Solang on 2018/7/10.
 */

public interface AddReduceNumListener {
    void addNum(ChosenServiceEntity entity);
    void reduceNum(ChosenServiceEntity entity);
}
