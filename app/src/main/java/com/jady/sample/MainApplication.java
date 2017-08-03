package com.jady.sample;

import android.app.Application;

import com.jady.retrofitclient.HttpManager;
import com.jady.sample.api.UrlConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lipingfa on 2017/6/12.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HttpManager.init(this, UrlConfig.BASE_URL);
        HttpManager.setOnGetHeadersListener(new HttpManager.OnGetHeadersListener() {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("access_token", "1234");
                return headers;
            }
        });
    }
}
