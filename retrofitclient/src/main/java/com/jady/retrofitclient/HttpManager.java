package com.jady.retrofitclient;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.jady.retrofitclient.callback.FileResponseResult;
import com.jady.retrofitclient.callback.HttpCallback;
import com.jady.retrofitclient.download.DownloadInfo;
import com.jady.retrofitclient.download.DownloadManager;
import com.jady.retrofitclient.interceptor.OffLineIntercept;
import com.jady.retrofitclient.interceptor.UploadFileInterceptor;
import com.jady.retrofitclient.listener.DownloadFileListener;
import com.jady.retrofitclient.listener.TransformProgressListener;
import com.jady.retrofitclient.upload.FileUploadEnetity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by jady on 2016/12/6.
 */
public class HttpManager {
    public static Context mContext;
    private volatile static HttpManager httpManager;
    private static int HANDER_DELAYED_TIME = 500;
    /**
     * 这个headers是每次请求动态更新的，用完需要清掉
     */
    private Map<String, String> tmpHeaders;
    private static String baseUrl = "";
    private static OnGetHeadersListener onGetHeadersListener;
    private static String cacheDirPath;
    private static long maxCacheSize;

    private HttpManager() {
    }

    public static HttpManager getInstance() {
        if (mContext == null) {
            Log.e("HttpManager", "please call the method of init() first");
        }
        if (httpManager == null) {
            synchronized (HttpManager.class) {
                if (httpManager == null) {
                    httpManager = new HttpManager();
                }
            }
        }
        return httpManager;
    }

    public interface OnGetHeadersListener {
        Map<String, String> getHeaders();
    }

    public void setOnGetHeadersListener(OnGetHeadersListener onGetHeadersListener) {
        HttpManager.onGetHeadersListener = onGetHeadersListener;
    }

    /**
     * 初始化信息
     *
     * @param context 上下文
     * @param baseUrl URL前缀
     */
    public static void init(Context context, String baseUrl) {
        HttpManager.mContext = context;
        setBaseUrl(baseUrl);
        initCache(context.getCacheDir() + "/http", 10 * 1024 * 1024);
    }

    /**
     * 设置缓存路径和最大缓存大小
     *
     * @param cacheDirPath 缓存路径
     * @param maxSize      最大缓存大小
     */
    public static void initCache(String cacheDirPath, long maxSize) {
        if (TextUtils.isEmpty(cacheDirPath) || maxSize <= 0) {
            return;
        }
        if (!new File(cacheDirPath).exists()) {
            new File(cacheDirPath).mkdirs();
        }

        HttpManager.cacheDirPath = cacheDirPath;
        HttpManager.maxCacheSize = maxSize;
    }

    /**
     * 给Retrofit添加拦截器，设置链接前缀
     *
     * @param baseUrl
     * @return
     */
    public RetrofitClient.Builder getRetrofitBuilder(String baseUrl) {
        RetrofitClient.Builder builder = new RetrofitClient.Builder()
//                .addCacheInterceptor(CacheInterceptor.create(mContext))
//                .addGsonConverterInterceptor(GsonConverterFactory.create())
                .addRxJavaCallAdapterInterceptor(RxJavaCallAdapterFactory.create())
//                .addRequestJsonInterceptor(RequestJsonInterceptor.create())
                .addOffLineIntercept(OffLineIntercept.create(mContext))
                .isLog(true);

        handleHeaders(builder);

        if (!TextUtils.isEmpty(baseUrl)) {
            builder.baseUrl(baseUrl);
        }
        return builder;
    }

    private void handleHeaders(RetrofitClient.Builder builder) {
        Map<String, String> headerMap = new HashMap<>();
        if (onGetHeadersListener != null) {
            Map<String, String> listenerHeaders = onGetHeadersListener.getHeaders();
            if (listenerHeaders != null) {
                headerMap.putAll(listenerHeaders);
            }
        }
        //动态添加的headers的优先级要高于固定的onGetHeadersListener中的
        if (this.tmpHeaders != null && this.tmpHeaders.size() > 0) {
            headerMap.putAll(this.tmpHeaders);
            //这个headers是每次请求动态更新的，所以用完需要清掉
            this.tmpHeaders.clear();
        }
        if (headerMap.get("Content-Type") == null) {
            headerMap.put("Content-Type", "application/json; charset=utf-8");
        }
        if (headerMap.get("Accept") == null) {
            headerMap.put("Accept", "application/json");
        }
        builder.addHeader(headerMap);
    }

    /**
     * 初始化头部信息，添加一些共同的参数
     *
     * @return
     */
    public HttpManager addHeaders(Map<String, String> headers) {
        this.tmpHeaders = headers;
        return this;
    }

    public Map<String, String> getTmpHeaders() {
        return tmpHeaders;
    }

    /**
     * 设置共同URL前缀
     *
     * @param baseUrl
     */
    public static void setBaseUrl(String baseUrl) {
        HttpManager.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 发送Get请求
     *
     * @param url        请求相对地址，地址共同部分前缀在{@link #getRetrofitBuilder(String)}中设置
     * @param parameters 请求参数
     * @param callback   网络回调
     */
    public void get(String url, Map<String, Object> parameters, HttpCallback callback) {
        getRetrofitBuilder(baseUrl).build().get(mContext, url, parameters, callback);
    }

    /**
     * 同步Get
     *
     * @param url
     * @param parameters
     * @param callback
     */
    public void syncGet(String url, Map<String, Object> parameters, HttpCallback callback) {
        getRetrofitBuilder(baseUrl).build().syncGet(mContext, url, parameters, callback);
    }

    /**
     * 发送Get请求
     *
     * @param url        请求相对地址，地址共同部分前缀在{@link #getRetrofitBuilder(String)}中设置
     * @param parameters 请求参数
     * @param callback   网络回调
     */
    public void getFullPath(String url, Map<String, Object> parameters, HttpCallback callback) {
        getRetrofitBuilder("").build().getFullPath(mContext, url, parameters, callback);
    }

    /**
     * 发送Post请求
     *
     * @param url        请求相对地址，地址共同部分前缀在{@link #getRetrofitBuilder(String)}中设置
     * @param parameters 请求参数
     * @param callback   网络回调
     */
    public void post(String url, Map<String, Object> parameters, HttpCallback callback) {
        getRetrofitBuilder(baseUrl).build().post(mContext, url, parameters, callback);
    }

    /**
     * 发送Put请求
     *
     * @param url      请求相对地址，地址共同部分前缀在{@link #getRetrofitBuilder(String)}中设置
     * @param body     请求体
     * @param callback 网络回调
     */
    public <T> void putByBody(String url, T body, HttpCallback callback) {
        getRetrofitBuilder(baseUrl).build().putByBody(mContext, url, body, callback);
    }

    /**
     * 发送Put请求
     *
     * @param url        请求相对地址，地址共同部分前缀在{@link #getRetrofitBuilder(String)}中设置
     * @param parameters 请求参数
     * @param callback   网络回调
     */
    public void put(String url, Map<String, Object> parameters, HttpCallback callback) {
        getRetrofitBuilder(baseUrl).build().put(mContext, url, parameters, callback);
    }

    /**
     * 发送DELETE请求
     *
     * @param url      请求相对地址，地址共同部分前缀在{@link #getRetrofitBuilder(String)}中设置
     * @param callback 网络回调
     */
    public void delete(String url, HttpCallback callback) {
        getRetrofitBuilder(baseUrl).build().delete(mContext, url, callback);
    }

    /**
     * 发送DELETE请求
     *
     * @param url      请求相对地址，地址共同部分前缀在{@link #getRetrofitBuilder(String)}中设置
     * @param body     请求参数
     * @param callback 网络回调
     */
    public <T> void deleteByBody(String url, T body, HttpCallback callback) {
        getRetrofitBuilder(baseUrl).build().deleteByBody(mContext, url, body, callback);
    }

    /**
     * 同步Post
     *
     * @param url
     * @param parameters
     * @param callback
     */
    public void syncPost(String url, Map<String, Object> parameters, HttpCallback callback) {
        getRetrofitBuilder(baseUrl).build().syncPost(mContext, url, parameters, callback);
    }

    /**
     * 注意，此方法传到服务器的是一个json串
     *
     * @param url
     * @param body
     * @param callback
     * @param <T>
     */
    public <T> void postByBody(String url, T body, HttpCallback callback) {
        getRetrofitBuilder(baseUrl).build().postByBody(mContext, url, body, callback);
    }

//    public void postNotEncoded(String url, Map<String, Object> parameters, HttpCallback callback) {
//        getRetrofitBuilder(baseUrl).addDecodeParameterInteceptor(RecodeParameterInteceptor.create()).build().post(mContext, url, parameters, callback);
//    }

    /**
     * 发送Post请求
     *
     * @param url        请求相对地址，地址共同部分前缀在{@link #getRetrofitBuilder(String)}中设置
     * @param parameters 请求参数
     * @param callback   网络回调
     */
    public void postFullPath(String url, Map<String, Object> parameters, HttpCallback callback) {
        getRetrofitBuilder("").build().postFullPath(mContext, url, parameters, callback);
    }

//    /**
//     * 发送Post请求
//     *
//     * @param url        请求相对地址，地址共同部分前缀在{@link #getRetrofitBuilder()}中设置
//     * @param parameters 请求参数
//     */
//    public Observable post(String url, Map<String, Object> parameters) {
//        RetrofitClient retrofitManager = RetrofitClient.getInstance(mContext);
//        CommonRequest request = retrofitManager.getRequest();
//        return request.doPost(url, parameters)
//                .compose(retrofitManager.getSchedulerTransformer());
//    }

    public void uploadFile(FileUploadEnetity uploadEnetity, final TransformProgressListener iProgress) {
        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                iProgress.onProgress(msg.arg1, msg.arg2, msg.arg1 >= msg.arg2);
            }
        };

        FileResponseResult fileResponseResult = new FileResponseResult() {
            @Override
            public void onExecuting(long progress, long total, boolean done) {
                Message message = new Message();
                message.arg1 = (int) progress;
                message.arg2 = (int) total;
                handler.sendMessageDelayed(message, HANDER_DELAYED_TIME);
            }
        };

        getRetrofitBuilder(baseUrl)
                .addUploadFileInterceptor(UploadFileInterceptor.create())
                .build()
                .upLoadFiles(uploadEnetity, fileResponseResult);
    }

    public void download(String url, String savePath, DownloadFileListener listener) {
        DownloadInfo info = new DownloadInfo(url, savePath);
        info.setState(DownloadInfo.START);
        info.setListener(listener);
        DownloadManager downloadManager = DownloadManager.getInstance();

        downloadManager.startDown(info);
    }

    public void download(DownloadInfo info) {
        DownloadManager downloadManager = DownloadManager.getInstance();
        downloadManager.startDown(info);
    }

    public DownloadManager getDownloadManager() {
        return DownloadManager.getInstance();
    }
}
