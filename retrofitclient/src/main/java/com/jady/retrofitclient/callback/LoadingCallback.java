package com.jady.retrofitclient.callback;

/**
 * 加载框回调
 * Created by jady on 2016/12/8.
 */
public interface LoadingCallback {
    /**
     * 开始显示加载框
     * @param msg
     */
    void onStartLoading(String msg);

    /**
     * 隐藏加载框
     */
    void onFinishLoading();
}
