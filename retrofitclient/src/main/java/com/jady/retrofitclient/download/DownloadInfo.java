package com.jady.retrofitclient.download;

import com.jady.retrofitclient.listener.DownloadFileListener;
import com.jady.retrofitclient.request.CommonRequest;

/**
 * Created by jady on 2017/2/6.
 */
public class DownloadInfo {

    public static final int START = 0;
    public static final int DOWNLOAD = 1;
    public static final int PAUSE = 2;
    public static final int STOP = 3;
    public static final int ERROR = 4;
    public static final int FINISH = 5;

    private int id = 0;

    private String savePath;

    private long contentLength;

    private long readLength;

    private int state;

    private String url;

    private DownloadFileListener listener;
    private CommonRequest request;

    public DownloadInfo(String url, String savePath) {
        this.url = url;
        this.savePath = savePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DownloadFileListener getListener() {
        return listener;
    }

    public void setListener(DownloadFileListener listener) {
        this.listener = listener;
    }

    public CommonRequest getRequest() {
        return request;
    }

    public void setRequest(CommonRequest request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "savePath='" + savePath + '\'' +
                ", contentLength=" + contentLength +
                ", readLength=" + readLength +
                ", state=" + state +
                ", url='" + url + '\'' +
                '}';
    }
}
