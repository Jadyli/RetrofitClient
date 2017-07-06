package com.jady.retrofitclient.callback;

/**
 * Created by jady on 2016/12/22.
 */
public interface FileResponseResult {

    void onSuccess();

    void onFailure(Throwable throwable, String content);
}
