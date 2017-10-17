package com.jady.sample.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jady.retrofitclient.download.DownloadInfo;
import com.jady.retrofitclient.download.DownloadManager;
import com.jady.sample.R;
import com.jady.sample.support.utils.FileUtils;
import com.jady.sample.ui.adapter.DownloadFileAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipingfa on 2017/6/27.
 */
public class FileDownloadFragment extends Fragment implements View.OnClickListener {

    protected View rootView;
    protected Button btnFraDownloadStartAll;
    protected Button btnFraDownloadStopAll;
    protected Button btnFraDownloadContinueAll;
    protected Button btnFraDownloadCancelAll;
    protected RecyclerView rvFraDownload;
    private List<DownloadInfo> downloadInfoList;
    private DownloadFileAdapter adapter;

    public static FileDownloadFragment newInstance() {

        Bundle args = new Bundle();

        FileDownloadFragment fragment = new FileDownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.download_file_fra, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        btnFraDownloadStartAll = (Button) rootView.findViewById(R.id.btn_fra_download_start_all);
        btnFraDownloadStartAll.setOnClickListener(FileDownloadFragment.this);
        btnFraDownloadStopAll = (Button) rootView.findViewById(R.id.btn_fra_download_pause_all);
        btnFraDownloadStopAll.setOnClickListener(FileDownloadFragment.this);
        btnFraDownloadContinueAll = (Button) rootView.findViewById(R.id.btn_fra_download_stop_all);
        btnFraDownloadContinueAll.setOnClickListener(FileDownloadFragment.this);
        btnFraDownloadCancelAll = (Button) rootView.findViewById(R.id.btn_fra_download_cancel_all);
        btnFraDownloadCancelAll.setOnClickListener(FileDownloadFragment.this);
        rvFraDownload = (RecyclerView) rootView.findViewById(R.id.rv_fra_download);

        adapter = new DownloadFileAdapter(getActivity());
        downloadInfoList = new ArrayList<>();
        String[] urls = {
                "http://oitnotj58.bkt.clouddn.com/apk/v2.0/%E5%8A%A0%E7%8F%AD%E7%AE%A1%E5%AE%B6_v2.0.0_invitefriends_com.qeeniao.mobile.recordincomej_0426_2129.apk",
                "http://oitnotj58.bkt.clouddn.com/%E5%8A%A0%E7%8F%AD%E7%AE%A1%E5%AE%B6_v1.3.0_qeeniao_com.qeeniao.mobile.recordincomej_0504_1038.apk",
                "http://oitnotj58.bkt.clouddn.com/%E5%8A%A0%E7%8F%AD%E7%AE%A1%E5%AE%B6_v1.3.0%E7%BA%BF%E4%B8%8A%E5%8F%AF%E7%99%BB%E5%BD%95%E7%89%88.apk",
                "http://oitnotj58.bkt.clouddn.com/QQ.png",
                "http://oitnotj58.bkt.clouddn.com/QQspace.png"};
        for (int i = 0; i < urls.length; i++) {
            DownloadInfo downloadInfo = new DownloadInfo(urls[i],
                    FileUtils.getInternalDir(getActivity(), FileUtils.HTTP_PATH, true) + File.pathSeparator + "第" + i + "条");
            downloadInfoList.add(downloadInfo);
        }
        adapter.setDownloadInfoList(downloadInfoList);
        rvFraDownload.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFraDownload.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_fra_download_start_all) {
            DownloadManager.getInstance().addAll(downloadInfoList);
            DownloadManager.getInstance().startAll();
        } else if (view.getId() == R.id.btn_fra_download_pause_all) {
            DownloadManager.getInstance().pauseAll();
        } else if (view.getId() == R.id.btn_fra_download_stop_all) {
            DownloadManager.getInstance().stopAll();
        } else if (view.getId() == R.id.btn_fra_download_cancel_all) {
            DownloadManager.getInstance().removeAll();
        }
        adapter.notifyDataSetChanged();
    }
}
