package com.jady.retrofitclient.subscriber;

import com.jady.retrofitclient.download.DownloadInfo;
import com.jady.retrofitclient.download.DownloadManager;
import com.jady.retrofitclient.listener.DownloadFileListener;
import com.jady.retrofitclient.listener.TransformProgressListener;

import java.lang.ref.SoftReference;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by jady on 2017/2/6.
 */
public class DownloadSubscriber<T> extends Subscriber<T> implements TransformProgressListener {

    private SoftReference<DownloadFileListener> downloadListener;
    private DownloadInfo downloadInfo;

    public DownloadSubscriber(DownloadInfo downloadInfo) {
        this.downloadListener = new SoftReference<DownloadFileListener>(downloadInfo.getListener());
        this.downloadInfo = downloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        this.downloadListener = new SoftReference<DownloadFileListener>(downloadInfo.getListener());
        this.downloadInfo = downloadInfo;
    }

    @Override
    public void onStart() {
        if (downloadListener.get() != null) {
            downloadListener.get().onStart();
        }
    }

    @Override
    public void onCompleted() {
        if (downloadListener.get() != null) {
            downloadListener.get().onComplete();
        }
        downloadInfo.setState(DownloadInfo.FINISH);
    }

    @Override
    public void onError(Throwable e) {
        if (downloadListener.get() != null) {
            downloadListener.get().onError(e);
        }
        DownloadManager.getInstance().remove(downloadInfo);
        downloadInfo.setState(DownloadInfo.ERROR);
    }

    @Override
    public void onNext(T t) {
        if (downloadListener.get() != null) {
            downloadListener.get().onNext(t);
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
        if (downloadListener.get() != null) {
            rx.Observable.just(progress).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            if (downloadInfo.getState() == DownloadInfo.PAUSE || downloadInfo.getState() == DownloadInfo.STOP)
                                return;
                            downloadInfo.setState(DownloadInfo.DOWNLOAD);
                            if (downloadListener.get() != null) {
                                downloadListener.get().updateProgress((float) aLong, downloadInfo.getContentLength(), completed);
                            }
                        }
                    });
        }
    }

    @Override
    public void onFailed(String msg) {

    }
}
