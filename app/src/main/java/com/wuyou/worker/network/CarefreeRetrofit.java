package com.wuyou.worker.network;

import com.gs.buluo.common.network.CustomGsonFactory;
import com.gs.buluo.common.network.HttpInterceptor;
import com.gs.buluo.common.network.LogInterceptor;
import com.wuyou.worker.Constant;

import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by admin on 2016/11/1.
 */
public class CarefreeRetrofit {

    private static CarefreeRetrofit instance;
    private Map<Class, Object> apis = new HashMap<>();
    private final Retrofit retrofit;

    private CarefreeRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(new HttpInterceptor());
        builder.interceptors().add(new LogInterceptor());
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(builder.build())
                .addConverterFactory(CustomGsonFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public synchronized static CarefreeRetrofit getInstance() {
        if (null == instance) {
            instance = new CarefreeRetrofit();
        }
        return instance;
    }

    public <T> T createApi(Class<T> service) {
        if (!apis.containsKey(service)) {
            T instance = retrofit.create(service);
            apis.put(service, instance);
        }

        return (T) apis.get(service);
    }

    /**
     * 线程切换
     *
     * @return
     */
    public static <T> FlowableTransformer<T, T> getScheduler() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
