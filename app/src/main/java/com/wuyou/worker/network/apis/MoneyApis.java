package com.wuyou.worker.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.worker.bean.ListResponse;
import com.wuyou.worker.bean.entity.WalletInfoEntity;
import com.wuyou.worker.bean.entity.WalletTransactionEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by DELL on 2018/3/25.
 */

public interface MoneyApis {
    @GET("coin/balance/{worker_id}")
    Observable<BaseResponse<WalletInfoEntity>> getWalletAccount(
            @Path("worker_id") String uid,
            @QueryMap SortedTreeMap<String, String> map);

    @GET("coin/transactions/{worker_id}")
    Observable<BaseResponse<ListResponse<WalletTransactionEntity>>> getWalletTransaction(
            @Path("worker_id") String uid,
            @QueryMap SortedTreeMap<String, String> map);
}
