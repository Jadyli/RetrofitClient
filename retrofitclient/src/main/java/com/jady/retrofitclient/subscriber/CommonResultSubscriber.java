package com.jady.retrofitclient.subscriber;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jady.retrofitclient.callback.HttpCallback;
import com.jady.retrofitclient.callback.LoadingCallback;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by jady on 2016/12/8.
 */
public class CommonResultSubscriber<T extends ResponseBody> extends Subscriber<T> {

    public static final String TAG = "CommonResultSubscriber";

    private Context mContext;
    private boolean showLoadingDailog = false;
    private String loadingMsg;

    private LoadingCallback loadingCallback;
    private HttpCallback httpCallback;

    /**
     * 不带加载框回调
     *
     * @param mContext
     * @param callback
     */
    public CommonResultSubscriber(Context mContext, HttpCallback callback) {
        this.mContext = mContext;
        this.httpCallback = callback;
    }

    /**
     * 带加载框回调
     *
     * @param mContext
     * @param callback
     * @param loadingMsg
     */
    public CommonResultSubscriber(Context mContext, HttpCallback callback, String loadingMsg) {
        this.mContext = mContext;
        this.httpCallback = callback;
        this.loadingMsg = loadingMsg;

        if (!TextUtils.isEmpty(loadingMsg)) {
            try {
                loadingCallback = (LoadingCallback) mContext;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (loadingCallback != null) {
                showLoadingDailog = true;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (showLoadingDailog && loadingCallback != null) {
            loadingCallback.onStartLoading(loadingMsg);
        }
    }

    @Override
    public void onCompleted() {
        if (showLoadingDailog) {
            loadingCallback.onFinishLoading();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (showLoadingDailog && loadingCallback != null) {
            loadingCallback.onFinishLoading();
        }
        e.printStackTrace();
        if (httpCallback != null) {
            httpCallback.onFailed("failure", e.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
        if (t.contentLength() == 0) {
            return;
        }
        if (httpCallback != null) {
            boolean returnJson = false;
            Type genericityType = httpCallback.getGenericityType();
            if (genericityType instanceof Class) {
                switch (((Class) genericityType).getSimpleName()) {
                    case "Object":
                    case "String":
                        returnJson = true;
                        break;
                    default:
                        break;
                }
            }

            if (returnJson) {
                try {
                    httpCallback.onResolve(t.string());
                } catch (IOException e) {
                    httpCallback.onFailed("failure", e.getMessage());
                    e.printStackTrace();
                }
            } else {
                try {
                    this.httpCallback.onResolve((new Gson()).fromJson(t.string(), genericityType));
                } catch (Exception e) {
                    httpCallback.onFailed("failure", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
