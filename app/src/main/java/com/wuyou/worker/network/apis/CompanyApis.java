package com.wuyou.worker.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.wuyou.worker.bean.Company;
import com.wuyou.worker.bean.UserInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public interface CompanyApis {
    @GET("companies")
    Observable<List<Company>> getCompaniesList(
            @Query("communityId") String communityId);

    @POST("persons/{id}/company_bind_request")
    Observable<BaseResponse<UserInfo>> bindCompany(
            @Path("id") String id);

}
