package com.wuyou.worker.network.apis;

import android.support.v4.util.ArrayMap;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;
import com.wuyou.worker.bean.ServeTimeBean;
import com.wuyou.worker.bean.entity.ContractDetailEntity;
import com.wuyou.worker.bean.entity.MerchantDetailEntity;
import com.wuyou.worker.bean.entity.OrderInfoEntity;
import com.wuyou.worker.bean.entity.OrderInfoListEntity;
import com.wuyou.worker.bean.entity.PartnerListEntity;
import com.wuyou.worker.bean.entity.WorkerListEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by solang on 2018/2/8.
 */

public interface OrderApis {
    /**
     * 获取订单列表
     *
     * @param map
     * @return
     */
    @GET("orders")
    Observable<BaseResponse<OrderInfoListEntity>> getOrders(
            @QueryMap SortedTreeMap<String, String> map);

    /**
     * 确认出发
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @PUT("order/confirm")
    Observable<BaseResponse> confirm(@FieldMap SortedTreeMap<String, String> map);

    /**
     * 确认完成
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @PUT("order/finish")
    Observable<BaseResponse> finish(@FieldMap SortedTreeMap<String, String> map);

    /**
     * 获取联盟订单列表
     *
     * @param merchant_id
     * @param status
     * @param start_id
     * @param flag
     * @param map
     * @return
     */
    @GET("union_orders/{merchant_id}/{status}/{start_id}/{flag}")
    Observable<BaseResponse<OrderInfoListEntity>> getAllianceOrders(
            @Path("merchant_id") String merchant_id, @Path("status") String status,
            @Path("start_id") String start_id, @Path("flag") String flag,
            @QueryMap SortedTreeMap<String, String> map);

    /**
     * 订单详情
     *
     * @param order_id
     * @param map
     * @return
     */
    @GET("order/{order_id}")
    Observable<BaseResponse<OrderInfoEntity>> getOrdersDetail(
            @Path("order_id") String order_id,
            @QueryMap SortedTreeMap<String, String> map);

    /**
     * 合伙人列表
     *
     * @param uid
     * @param map
     * @return
     */
    @GET("union/providers/{uid}")
    Observable<BaseResponse<PartnerListEntity>> getUnionPartner(
            @Path("uid") String uid, @QueryMap SortedTreeMap<String, String> map);

    /**
     * 员工列表
     *
     * @param uid
     * @param map
     * @return
     */
    @GET("workers/{uid}")
    Observable<BaseResponse<WorkerListEntity>> getWorkersInfo(
            @Path("uid") String uid, @QueryMap SortedTreeMap<String, String> map);

    @GET("union/shops/{uid}/{action}")
    Observable<BaseResponse<WorkerListEntity>> getDispatchMerchantInfo(
            @Path("uid") String uid, @Path("action") String action, @QueryMap SortedTreeMap<String, String> map);

    /**
     * 加入联盟时的商户列表
     *
     * @param uid
     * @param startId
     * @param flag
     * @param map
     * @return
     */
    @GET("union/prepare/{uid}/{start_id}/{flag}")
    Observable<BaseResponse<WorkerListEntity>> getPrepareMerchantInfo(
            @Path("uid") String uid, @Path("start_id") String startId, @Path("flag") String flag, @QueryMap SortedTreeMap<String, String> map);

    /**
     * 服务商详情
     *
     * @param uid
     * @param merchant_id
     * @param map
     * @return
     */
    @GET("detail/{uid}/{merchant_id}")
    Observable<BaseResponse<MerchantDetailEntity>> getMerchantDetail(
            @Path("uid") String uid, @Path("merchant_id") String merchant_id,
            @QueryMap SortedTreeMap<String, String> map);

    @GET("contract/{contract_id}")
    Observable<BaseResponse<ContractDetailEntity>> getContractDetail(
            @Path("contract_id") String contract_id,
            @QueryMap SortedTreeMap<String, String> map);

    /**
     * 加入联盟
     *
     * @param uid
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("union/{uid}")
    Observable<BaseResponse> signContract(
            @Path("uid") String uid, @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @POST("order/dispatch/{uid}")
    Observable<BaseResponse> dispatchOrder(
            @Path("uid") String uid, @FieldMap SortedTreeMap<String, String> map);

    @FormUrlEncoded
    @PUT("login/{uid}")
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

    @GET("service_times/{order_id}")
    Observable<BaseResponse<ArrayMap<String, List<ServeTimeBean>>>> getAvailableServeTime(@Path("order_id") String orderId, @QueryMap SortedTreeMap<String, String> map);

    @PUT("service_time")
    Observable<BaseResponse> updateServeTime(@QueryMap SortedTreeMap<String, String> map);
}
