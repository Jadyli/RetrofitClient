package com.jady.retrofitclient.upload;

import android.content.Context;
import android.text.TextUtils;

import com.jady.retrofitclient.callback.FileResponseResult;
import com.jady.retrofitclient.callback.LoadingCallback;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by jady on 2016/12/8.
 */

public class UploadSubscriber<T extends ResponseBody> extends Subscriber<T> {

    private Context mContext;
    private boolean showLoadingDailog = false;
    private String loadingMsg;

    private LoadingCallback loadingCallback;
    private FileResponseResult callback;

    /**
     * 不带加载框回调
     *
     * @param mContext
     * @param callback
     */
    public UploadSubscriber(Context mContext, FileResponseResult callback) {
        this.mContext = mContext;
        this.callback = callback;
    }

    /**
     * 带加载框回调
     *
     * @param mContext
     * @param callback
     * @param loadingMsg
     */
    public UploadSubscriber(Context mContext, FileResponseResult callback, String loadingMsg) {
        this.mContext = mContext;
        this.callback = callback;
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
        if (showLoadingDailog) {
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
        if (showLoadingDailog) {
            loadingCallback.onFinishLoading();
        }
        callback.onFailure(e, "服务器错误");
    }

    @Override
    public void onNext(T t) {
        if (t.contentLength() == 0) {
            return;
        }
        try {
            String jsonString = new String(t.bytes());
            callback.onSuccess(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailure(e, e.getMessage());
        }
//        Gson gson = new Gson();
//        TypeAdapter<?> adapter = gson.getAdapter(callback.getGenericityType());
//        JsonReader jsonReader = gson.newJsonReader(t.charStream());
//        try {
//            callback.onNext(adapter.read(jsonReader));
//        } catch (IOException e) {
//            callback.onFailure(e, e.getMessage());
//            e.printStackTrace();
//        }
    }
}
