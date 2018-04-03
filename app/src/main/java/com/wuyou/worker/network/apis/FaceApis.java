package com.wuyou.worker.network.apis;

import com.wuyou.worker.bean.entity.FaceResult;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by DELL on 2018/4/3.
 */

public interface FaceApis {
    @Multipart
    @POST("facepp/v3/compare")
    Observable<FaceResult> doCompare(@Query("api_key") String key, @Query("api_secret") String secret,
                                     @Part MultipartBody.Part idFile,
                                     @Part MultipartBody.Part faceFile);


}
