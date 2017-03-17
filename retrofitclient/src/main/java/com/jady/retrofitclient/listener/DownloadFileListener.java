package com.jady.retrofitclient.listener;

/**
 * Created by jady on 2017/2/6.
 */
public abstract class DownloadFileListener<T> {
    /**
     * 单个下载成功后回调
     *
     * @param t
     */
    public abstract void onNext(T t);

    public void onStart(){};

    /**
     * 批量下载完成
     */
    public abstract void onComplete();

    public abstract void updateProgress(long contentRead, long contentLength, boolean completed);

    public abstract void onError(Throwable e);

    public void onPause() {

    }

    public void onStop() {

    }
}
