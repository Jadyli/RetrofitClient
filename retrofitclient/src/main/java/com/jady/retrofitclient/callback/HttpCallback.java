package com.jady.retrofitclient.callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by jady on 2016/12/8.
 */
public abstract class HttpCallback<T> {

    public static final String TAG = "HttpCallback";

    private Type type;

    public HttpCallback() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            this.type = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else {
            this.type = Object.class;
        }
    }

    public abstract void onResolve(T t);

    public abstract void onFailure(String err_code, String message);

    public Type getType() {
        return type;
    }
}
