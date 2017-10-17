package com.jady.retrofitclient.subscriber;

import com.jady.retrofitclient.download.DownloadInfo;
import com.jady.retrofitclient.download.DownloadManager;
import com.jady.retrofitclient.listener.DownloadFileListener;
import com.jady.retrofitclient.listener.TransformProgressListener;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by jady on 2017/2/6.
 */
public class DownloadSubscriber<T> extends Subscriber<T> implements TransformProgressListener {

    private DownloadFileListener downloadListener;
    private DownloadInfo downloadInfo;

    public DownloadSubscriber(DownloadInfo downloadInfo) {
        this.downloadListener = downloadInfo.getListener();
        this.downloadInfo = downloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        this.downloadListener = downloadInfo.getListener();
        this.downloadInfo = downloadInfo;
    }

    @Override
    public void onStart() {
        if (downloadListener != null) {
            downloadListener.onStart();
        }
    }

    @Override
    public void onCompleted() {
        if (downloadListener != null) {
            downloadListener.onComplete();
        }
        downloadInfo.setState(DownloadInfo.FINISH);
    }

    @Override
    public void onError(Throwable e) {
        if (downloadListener != null) {
            downloadListener.onError(e);
        }
        DownloadManager.getInstance().remove(downloadInfo);
        downloadInfo.setState(DownloadInfo.ERROR);
    }

    @Override
    public void onNext(T t) {
        if (downloadListener != null) {
            downloadListener.onNext(t);
        }
    }

    @Override
    public void onProgress(long progress, long total, final boolean completed) {
        if (downloadInfo.getContentLength() > total) {
            progress = downloadInfo.getContentLength() - total + progress;
        } else {
            downloadInfo.setContentLength(total);
        }
        downloadInfo.setReadLength(progress);
        if (downloadListener != null) {
            rx.Observable.just(progress).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            if (downloadInfo.getState() == DownloadInfo.PAUSE || downloadInfo.getState() == DownloadInfo.STOP)
                                return;
                            downloadInfo.setState(DownloadInfo.DOWNLOAD);
                            if (downloadListener != null) {
                                downloadListener.updateProgress((float) aLong, downloadInfo.getContentLength(), completed);
                            }
                        }
                    });
        }
    }

    @Override
    public void onFailed(String msg) {

    }
}
