package com.jady.sample.api;

import android.support.annotation.IntRange;

import com.jady.retrofitclient.HttpManager;
import com.jady.retrofitclient.callback.HttpCallback;
import com.jady.retrofitclient.listener.TransformProgressListener;
import com.jady.sample.bean.UserForLogin;

import java.util.HashMap;
import java.util.List;
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
        HttpManager.get(UrlConfig.IMG_LIST, parameters, callback);
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
        HashMap<String, String> headers = new HashMap<>();
        headers.put("aaa", "adafd");
        HttpManager.setTmpBaseUrl("http://192.168.0.127:8080/retrofitclientserver/");
        HttpManager.addTmpHeaders(headers);
        HttpManager.get(UrlConfig.USER_INFO, null, callback);
    }

    public static void testPost(String name, String password, HttpCallback callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("password", password);
        HttpManager.post(UrlConfig.USER_LOGIN, parameters, callback);
    }

    public static void testPost(UserForLogin userForLogin, HttpCallback callback) {
        HttpManager.postByBody(UrlConfig.USER_LOGIN_BY_BODY, userForLogin, callback);
    }

    public static void testPut(String putContent, HttpCallback callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", putContent);
//        parameters.put("age", 18);
        HttpManager.put(UrlConfig.USER_UPDATE, parameters, callback);
    }

    public static void testDelete(int feedId, HttpCallback callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("feed_id", feedId);
        HttpManager.deleteByBody(UrlConfig.FEED_DELETE, parameters, callback);
    }

    public static void testSingleFileUpload(String url, String filePath, String fileDes, TransformProgressListener iProgress) {
        HttpManager.uploadFile(url, filePath, fileDes, iProgress);
    }

    public static void testMultipleFileUpload(String url, List<String> filePathList, TransformProgressListener iProgress) {
        HttpManager.uploadFiles(url, filePathList, iProgress);
    }
}
