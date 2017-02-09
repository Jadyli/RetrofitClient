package com.jady.retrofitclient.interceptor;

import android.content.Context;

import com.jady.retrofitclient.NetworkUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 离线时，20秒内会使用离线缓存
 * Created by jady on 2016/12/8.
 */
public class OffLineIntercept implements Interceptor {

    private Context mContext;

    public OffLineIntercept(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtils.isNetworkConnected(mContext)) {
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(1, TimeUnit.DAYS)
                    .build();
            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build();
        }
        return chain.proceed(request);
    }
}
