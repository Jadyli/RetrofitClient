package com.jady.sample.api;

import android.support.annotation.IntRange;

import com.jady.retrofitclient.HttpManager;
import com.jady.retrofitclient.callback.HttpCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lipingfa on 2017/6/12.
 */
public class API {

    /**
     * 获取图片列表
     *
     * @param category
     * @param count
     * @param page
     * @param callback
     */
    public static void getImageList(@ImgClassify int category, int count, int page, HttpCallback callback) {
        Map<String, Object> parameters = new HashMap<>();
        if (category > 0) {
            parameters.put("id", category);
        }
        if (count > 0) {
            parameters.put("rows", count);
        }
        if (page > 0) {
            parameters.put("page", page);
        }
        HttpManager.getInstance().get(UrlConfig.IMG_LIST, parameters, callback);
    }

    /**
     * 获取最新的图片列表
     *
     * @param keyId
     * @param category
     * @param count
     */
    public static void getLatestImgList(@IntRange(from = 1, to = 1000) int keyId, int category, int count) {
        Map<String, Object> parameters = new HashMap<>();
        if (keyId > 0) {
            parameters.put("id", keyId);
        }
        if (category > 0) {
            parameters.put("classify", category);
        }
        if (count > 0) {
            parameters.put("rows", count);
        }
    }

    public static void testGet(HttpCallback callback) {
        HttpManager.getInstance().get(UrlConfig.USER_INFO, null, callback);
    }
}
