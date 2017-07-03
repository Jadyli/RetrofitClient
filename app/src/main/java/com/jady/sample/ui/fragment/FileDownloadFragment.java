package com.jady.sample.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jady.retrofitclient.download.DownloadInfo;
import com.jady.sample.R;
import com.jady.sample.ui.adapter.DownloadFileAdapter;
import com.jady.sample.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipingfa on 2017/6/27.
 */
public class FileDownloadFragment extends Fragment {

    protected View rootView;
    protected Button btnFraDownloadStartAll;
    protected Button btnFraDownloadStopAll;
    protected Button btnFraDownloadContinueAll;
    protected Button btnFraDownloadCancelAll;
    protected RecyclerView rvFraDownload;

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
        btnFraDownloadStopAll = (Button) rootView.findViewById(R.id.btn_fra_download_stop_all);
        btnFraDownloadContinueAll = (Button) rootView.findViewById(R.id.btn_fra_download_continue_all);
        btnFraDownloadCancelAll = (Button) rootView.findViewById(R.id.btn_fra_download_cancel_all);
        rvFraDownload = (RecyclerView) rootView.findViewById(R.id.rv_fra_download);

        DownloadFileAdapter adapter = new DownloadFileAdapter(getActivity());
        List<DownloadInfo> downloadInfoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            DownloadInfo downloadInfo = new DownloadInfo("https://pro-app-qn.fir.im/03a7c474e9846cac29d93cf9417eda9dbb2c1eee.apk?attname=%E5%8A%A0%E7%8F%AD%E7%AE%A1%E5%AE%B6_v3.0.0_qntest_com.qeeniao.mobile.recordincomej_0703_1635.apk_3.0.0.apk&e=1499075336&token=LOvmia8oXF4xnLh0IdH05XMYpH6ENHNpARlmPc-T:yJbpba26XIcfxNNn08YKTVbk6nY=",
                    FileUtils.getInternalDir(getActivity(), FileUtils.HTTP_PATH, true));
            downloadInfoList.add(downloadInfo);
        }
        adapter.setDownloadInfoList(downloadInfoList);
        rvFraDownload.setAdapter(adapter);
    }
}
