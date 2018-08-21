package com.wuyou.worker.network.apis;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.worker.bean.UserInfo;
import com.wuyou.worker.bean.entity.LogoEntity;
import com.wuyou.worker.bean.entity.UpdateEntity;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by hjn91 on 2018/1/30.
 */

public interface UserApis {
    @GET("login/captcha")
    Observable<BaseResponse<UserInfo>> getVerifyCode(
            @QueryMap SortedTreeMap<String, String> map);

    @GET("profile/{uid}")
    Observable<BaseResponse<UserInfo>> getUserInfo(
            @Path("uid") String uid, @QueryMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("login")
    Observable<BaseResponse<UserInfo>> doLogin(
            @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("logout/{uid}")
    Observable<BaseResponse> doLogout(
            @Path("uid") String uid, @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("profile/{uid}")
    Observable<BaseResponse> updateUserInfo(
            @Path("uid") String uid, @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("password/edit/{uid}")
    Observable<BaseResponse> updatePwd(
            @Path("uid") String uid, @FieldMap SortedTreeMap<String, String> map);

    @Multipart
    @POST("avatar/{uid}")
    Observable<BaseResponse<LogoEntity>> updateAvatar(
            @Path("uid")String uid,
            @Part MultipartBody.Part file,
            @QueryMap SortedTreeMap<String, String> map);

    @GET("mobile/captcha")
    Observable<BaseResponse> getCaptchaCode(
            @QueryMap SortedTreeMap<String, String> map);

    @GET("client/update")
    Observable<BaseResponse<UpdateEntity>> checkUpdate(
            @QueryMap SortedTreeMap<String, String> map);

}
