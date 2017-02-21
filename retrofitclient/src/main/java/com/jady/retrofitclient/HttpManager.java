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
import com.jady.retrofitclient.interceptor.CacheInterceptor;
import com.jady.retrofitclient.interceptor.UploadFileInterceptor;
import com.jady.retrofitclient.listener.DownloadFileListener;
import com.jady.retrofitclient.listener.TransformProgressListener;
import com.jady.retrofitclient.upload.FileUploadEnetity;

import java.util.Map;

import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by jady on 2016/12/6.
 */
public class HttpManager {
    public static Context mContext;
    private static volatile HttpManager httpManager;
    private static int HANDER_DELAYED_TIME = 500;
    private Map<String, String> headers;
    private static String baseUrl;
    private static OnGetHeadersListener onGetHeadersListener;

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
    }

    /**
     * 给Retrofit添加拦截器，设置链接前缀
     *
     * @param baseUrl
     * @return
     */
    public RetrofitClient.Builder getRetrofitBuilder(String baseUrl) {
        RetrofitClient.Builder builder = new RetrofitClient.Builder()
                .addCacheInterceptor(CacheInterceptor.create(mContext))
                .addRxJavaCallAdapterInterceptor(RxJavaCallAdapterFactory.create())
                .isLog(true);
        if (onGetHeadersListener != null) {
            Map<String, String> headers = onGetHeadersListener.getHeaders();
            if (headers != null) {
                setHeaders(headers);
                builder.addHeader(headers);
            }
        } else if (headers != null) {
            builder.addHeader(headers);
        }
        if (!TextUtils.isEmpty(baseUrl)) {
            builder.baseUrl(baseUrl);
        }
        return builder;
    }

    /**
     * 初始化头部信息，添加一些共同的参数
     *
     * @return
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
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
     * 发送Get请求
     *
     * @param url        请求相对地址，地址共同部分前缀在{@link #getRetrofitBuilder(String)}中设置
     * @param parameters 请求参数
     * @param callback   网络回调
     */
    public void getFullPath(String url, Map<String, Object> parameters, HttpCallback callback) {
        getRetrofitBuilder(null).build().getFullPath(mContext, url, parameters, callback);
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
     * 发送Post请求
     *
     * @param url        请求相对地址，地址共同部分前缀在{@link #getRetrofitBuilder(String)}中设置
     * @param parameters 请求参数
     * @param callback   网络回调
     */
    public void postFullPath(String url, Map<String, Object> parameters, HttpCallback callback) {
        getRetrofitBuilder(null).build().postFullPath(mContext, url, parameters, callback);
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
