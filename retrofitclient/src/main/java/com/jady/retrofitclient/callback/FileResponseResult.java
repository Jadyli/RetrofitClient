package com.jady.retrofitclient.callback;

/**
 * Created by jady on 2016/12/22.
 */
public abstract class FileResponseResult {

    public void onSuccess(Object o) {

    }

    public void onFailure(Throwable throwable, String content) {
    }

    public abstract void onExecuting(long progress, long total, boolean done);
}
