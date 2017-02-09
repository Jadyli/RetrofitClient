package com.jady.retrofitclient.callback;

import android.text.TextUtils;

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

    public void onResolve(T t) {
        if (t instanceof ServerCallback) {
            if (((ServerCallback) t).isSuccess()) {
                onSuccess(t);
            } else {
                if (enableShowToast()) {
                    onFailed(((ServerCallback) t).getErr_code(), ((ServerCallback) t).getMessage());
                } else {
                    onFailure(((ServerCallback) t).getErr_code(), ((ServerCallback) t).getMessage());
                }
            }
        } else {
            onSuccess(t);
        }
    }

    public void onFailed(String err_code, String message) {
        if (!TextUtils.isEmpty(message)) {
            //you can show a toast here with a overall variant
        }
        onFailure(err_code,message);
    }

    /**
     * 是否允许自动弹窗显示服务器错误信息
     *
     * @return
     */
    public boolean enableShowToast() {
        return true;
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(String err_code, String message);

    public Type getType() {
        return type;
    }
}
