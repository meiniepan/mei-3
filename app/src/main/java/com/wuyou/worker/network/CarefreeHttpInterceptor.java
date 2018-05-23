package com.wuyou.worker.network;

import android.text.TextUtils;
import android.util.Base64;

import com.gs.buluo.common.BaseApplication;
import com.gs.buluo.common.network.EncryptUtil;
import com.gs.buluo.common.utils.Utils;
import com.wuyou.worker.CarefreeDaoSession;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hjn on 2016/11/10.
 */
public class CarefreeHttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        Request.Builder builder = req.newBuilder();
        HttpUrl url = req.url();
        String query = url.encodedQuery();
        if (!TextUtils.isEmpty(query) && !query.contains("sign=")) {
            HttpUrl.Builder newBuilder = url.newBuilder();
            newBuilder.addQueryParameter("sign", EncryptUtil.getSha1(Base64.encode(query.getBytes(), Base64.NO_WRAP)).toUpperCase());
            url = newBuilder.build();
        }
        if (CarefreeDaoSession.getInstance().getUserInfo() != null) {
            builder.addHeader("Authorization", CarefreeDaoSession.getInstance().getUserInfo().getToken());
        }
        Request request = builder.addHeader("Accept", "application/json").url(url).addHeader("Content-Type", "application/json").
                addHeader("User-Agent", Utils.getDeviceInfo(BaseApplication.getInstance().getApplicationContext())).build();
        return chain.proceed(request);
    }
}
