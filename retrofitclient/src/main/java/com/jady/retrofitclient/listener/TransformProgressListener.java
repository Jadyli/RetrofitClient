package com.jady.retrofitclient.listener;

/**
 * 进度监听 提供给下载与上传进度，依赖主线程
 */
public interface TransformProgressListener {
    void onProgress(long contentRead, long contentLength, boolean completed);
}
