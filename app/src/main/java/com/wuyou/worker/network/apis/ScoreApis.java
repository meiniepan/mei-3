package com.wuyou.worker.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.worker.bean.entity.ResponseListEntity;
import com.wuyou.worker.bean.entity.ServiceSortEntity;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by hjn on 2018/2/6.
 */

public interface ScoreApis {
    @GET("received_points/{uid}")
    Observable<BaseResponse<ResponseListEntity<ServiceSortEntity>>> getScoreRecordList(@Path("uid") String uid,
                                                                                       @QueryMap SortedTreeMap<String, String> map);


    @FormUrlEncoded
    @POST("sign")
    Observable<BaseResponse<ServiceSortEntity>> signIn(
            @FieldMap SortedTreeMap<String, String> map);

    @GET("sign/list/{uid}")
    Observable<BaseResponse<ResponseListEntity<ServiceSortEntity>>> getSignInRecord(@Path("uid") String uid, @QueryMap SortedTreeMap<String, String> map);
}
