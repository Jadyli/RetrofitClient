package com.jady.retrofitclient.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件上传
 * Created by jady on 2016/12/8.
 */
public class UploadFileInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "multipart/form-data")
                .build();
        return chain.proceed(request);
    }

    public static UploadFileInterceptor create() {
        return new UploadFileInterceptor();
    }
}
