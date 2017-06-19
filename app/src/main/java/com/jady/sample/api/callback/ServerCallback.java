package com.jady.sample.api.callback;

import android.widget.Toast;

import com.jady.retrofitclient.HttpManager;
import com.jady.retrofitclient.callback.HttpCallback;
import com.jady.sample.bean.ServerCallbackModel;

/**
 * @Description: Created by jadyli on 2017/5/4.
 */
public abstract class ServerCallback<T, V> extends HttpCallback<T> {
    @Override
    public void onResolve(T t) {
        if (t instanceof ServerCallbackModel) {
            ServerCallbackModel<V> callbackData = (ServerCallbackModel) t;
            V result = callbackData.getData();
            if (callbackData.isSuccess()) {
                this.onSuccess(result);
            } else {
                onFailed(callbackData.getErr_code(), callbackData.getMessage());
            }
        } else {
            onSuccess((V) t);
        }
    }

    @Override
    public void onFailed(String error_code, String error_message) {
        if (enableShowToast()) {
            Toast.makeText(HttpManager.mContext, error_message, Toast.LENGTH_SHORT).show();
        }
        onFailure(error_code, error_message);
    }

    public abstract void onSuccess(V data);

    public abstract void onFailure(String error_code, String error_message);

    public boolean enableShowToast() {
        return false;
    }
}
