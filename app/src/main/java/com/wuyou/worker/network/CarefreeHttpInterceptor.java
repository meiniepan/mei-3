package com.wuyou.worker.network;

import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.gs.buluo.common.BaseApplication;
import com.gs.buluo.common.network.EncryptUtil;
import com.gs.buluo.common.utils.Utils;
import com.wuyou.worker.CarefreeDaoSession;
import com.wuyou.worker.Constant;
import com.wuyou.worker.bean.entity.AuthTokenEntity;
import com.wuyou.worker.util.EncodeUtil;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hjn on 2016/11/10.
 */
public class CarefreeHttpInterceptor implements Interceptor {
    private String sign;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        Request.Builder builder = req.newBuilder();
        HttpUrl url = req.url();
        HttpUrl newBaseUrl = HttpUrl.parse(Constant.BASE_URL);
        HttpUrl newFullUrl = url.newBuilder()
                .host(newBaseUrl.host())
                .port(newBaseUrl.port())
                .build();
        String query = newFullUrl.encodedQuery();
        if (!TextUtils.isEmpty(query) && !query.contains("sign=")) {
            HttpUrl.Builder newBuilder = newFullUrl.newBuilder();
            sign = EncryptUtil.getSha1(Base64.encode(query.getBytes(), Base64.NO_WRAP)).toUpperCase();
            newBuilder.addQueryParameter("sign", sign);
            newFullUrl = newBuilder.build();
        }
        if (CarefreeDaoSession.getInstance().getUserInfo() != null) {
            builder.addHeader("Authorization", CarefreeDaoSession.getInstance().getUserInfo().getToken());
        }
        try {
            AddAuthToken(builder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Request request = builder.addHeader("Accept", "application/json").url(newFullUrl).addHeader("Content-Type", "application/json").
                addHeader("User-Agent", Utils.getDeviceInfo(BaseApplication.getInstance().getApplicationContext())).build();
        return chain.proceed(request);
    }
    private void AddAuthToken(Request.Builder builder) throws Exception {
        AuthTokenEntity e = new AuthTokenEntity();
        if (CarefreeDaoSession.getInstance().getUserInfo() != null)
            e.client_id = CarefreeDaoSession.getInstance().getUserInfo().getWorker_id();
        e.client = "worker";
        e.lng = 116.36968727736242d;
        e.lat = 39.89528271571901d;
        Gson gson = new Gson();
        String sInformation = gson.toJson(e);
        builder.addHeader("AuthToken", EncodeUtil.get3DES(sInformation, sign));
    }
}
