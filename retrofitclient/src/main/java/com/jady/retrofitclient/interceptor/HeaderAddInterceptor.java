package com.jady.retrofitclient.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求头
 * Created by jady on 2016/12/8.
 */
public class HeaderAddInterceptor implements Interceptor {

    private Map<String, String> headers = new HashMap<>();

    public HeaderAddInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }
        Request request = builder.build();
        return chain.proceed(request);
    }
}
