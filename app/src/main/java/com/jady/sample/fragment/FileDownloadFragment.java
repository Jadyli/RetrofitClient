package com.jady.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jady.sample.R;

/**
 * Created by lipingfa on 2017/6/27.
 */
public class FileDownloadFragment extends Fragment {

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
        return rootView;
    }

}
