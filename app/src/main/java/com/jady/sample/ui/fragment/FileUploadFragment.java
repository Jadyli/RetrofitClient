package com.jady.sample.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jady.retrofitclient.listener.TransformProgressListener;
import com.jady.sample.R;
import com.jady.sample.api.API;
import com.jady.sample.api.UrlConfig;
import com.jady.sample.utils.FileUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lipingfa on 2017/6/21.
 */
public class FileUploadFragment extends Fragment implements View.OnClickListener {
    protected View rootView;
    protected Button btnSingleFileUpload;
    protected TextView tvFile1Path;
    protected TextView tvFile2Path;
    protected Button btnMultipleFileUpload;
    private String mPath1, mPath2;

    public static FileUploadFragment newInstance() {

        Bundle args = new Bundle();

        FileUploadFragment fragment = new FileUploadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.upload_file_fra, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_single_file_upload) {
            chooseFile(1000);
        } else if (view.getId() == R.id.tv_file1_path) {
            chooseFile(1001);
        } else if (view.getId() == R.id.tv_file2_path) {
            chooseFile(1002);
        } else if (view.getId() == R.id.btn_multiple_file_upload) {
            if (TextUtils.isEmpty(mPath1) || TextUtils.isEmpty(mPath2)) {
                Toast.makeText(getActivity(), "请选择文件", Toast.LENGTH_SHORT).show();
                return;
            }
            List<String> pathList = new ArrayList<>();
            pathList.add(mPath1);
            pathList.add(mPath2);
            uploadMultipleFile(pathList);
        }
    }

    private void chooseFile(final int requestCode) {
        new RxPermissions(getActivity()).request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (granted) {
                    FileUtils.showFileChooser(FileUploadFragment.this, requestCode);
                } else {
                    Toast.makeText(getActivity(), "获取存储权限失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView(View rootView) {
        btnSingleFileUpload = (Button) rootView.findViewById(R.id.btn_single_file_upload);
        btnSingleFileUpload.setOnClickListener(FileUploadFragment.this);
        tvFile1Path = (TextView) rootView.findViewById(R.id.tv_file1_path);
        tvFile1Path.setOnClickListener(FileUploadFragment.this);
        tvFile2Path = (TextView) rootView.findViewById(R.id.tv_file2_path);
        tvFile2Path.setOnClickListener(FileUploadFragment.this);
        btnMultipleFileUpload = (Button) rootView.findViewById(R.id.btn_multiple_file_upload);
        btnMultipleFileUpload.setOnClickListener(FileUploadFragment.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String filePath = FileUtils.getUriPath(getActivity(), uri);
            switch (requestCode) {
                case 1000:
                    uploadSingleFile(filePath);
                    break;
                case 1001:
                    mPath1 = filePath;
                    tvFile1Path.setText(mPath1);
                    break;
                case 1002:
                    mPath2 = filePath;
                    tvFile2Path.setText(mPath2);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadSingleFile(String path) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        API.testSingleFileUpload(UrlConfig.SINGLE_FILE_UPLOAD, path, "上传的文件", new TransformProgressListener() {
            @Override
            public void onProgress(long contentRead, long contentLength, boolean completed) {
//                int progress = (int) (contentRead / contentLength);
//                Log.d(getClass().getSimpleName(), "upload progress:" + progress);
//                progressDialog.setProgress(progress);
                if (completed) {
                    progressDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    private void uploadMultipleFile(List<String> pathList) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        API.testMultipleFileUpload(UrlConfig.MULTIPLE_FILE_UPLOAD, pathList, new TransformProgressListener() {
            @Override
            public void onProgress(long contentRead, long contentLength, boolean completed) {
                progressDialog.setProgress((int) (contentRead / contentLength));
                if (completed) {
                    progressDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }
}
