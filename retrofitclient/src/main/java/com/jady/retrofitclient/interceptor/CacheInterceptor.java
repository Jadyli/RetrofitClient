package com.jady.retrofitclient.interceptor;

import android.content.Context;

import com.jady.retrofitclient.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yuan on 2016/10/30.
 * Detail cache for http 如果离线缓存与在线缓存同时使用，在线的时候必须先将离线缓存清空
 */
/**
 * 离线时，20秒内会使用离线缓存
 * Created by jady on 2016/12/8.
 */
public class CacheInterceptor implements Interceptor {

    private final String TAG = "CacheInterceptor";

    private Context mC;

    public CacheInterceptor(Context mC) {
        this.mC = mC;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!NetworkUtils.isNetworkConnected(mC)) {//没网强制从缓存读取(必须得写，不然断网状态下，退出应用，或者等待一分钟后，就获取不到缓存）
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        Response responseLatest;
        if (NetworkUtils.isNetworkConnected(mC)) {
            int maxAge = 60; //有网失效一分钟
            responseLatest = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 6; // 没网失效6小时
            responseLatest = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return responseLatest;
    }


    public static CacheInterceptor create(Context mContext) {
        return new CacheInterceptor(mContext);
    }
}
