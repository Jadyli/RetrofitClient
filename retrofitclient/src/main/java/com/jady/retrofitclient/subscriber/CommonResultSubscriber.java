package com.jady.retrofitclient.subscriber;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jady.retrofitclient.callback.HttpCallback;
import com.jady.retrofitclient.callback.LoadingCallback;

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
            httpCallback.onFailed("", "服务器错误:" + e.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
        if (t.contentLength() == 0) {
            return;
        }
        if (httpCallback != null && httpCallback.getType() != null) {
            try {
                httpCallback.onResolve(new Gson().fromJson(t.charStream(), httpCallback.getType()));
            } catch (Exception e) {
                httpCallback.onFailed("", "服务器返回结果解析错误");
                e.printStackTrace();
            }
        }
//        Gson gson = new Gson();
//        TypeAdapter<?> adapter = gson.getAdapter(httpCallback.getType());
//        JsonReader jsonReader = gson.newJsonReader(t.charStream());
//        try {
//            httpCallback.onNext(adapter.read(jsonReader));
//        } catch (IOException e) {
//            httpCallback.onFailure(e, e.getMessage());
//            e.printStackTrace();
//        }
    }
}
