package com.jady.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jady.sample.R;
import com.jady.sample.api.API;
import com.jady.sample.api.callback.CommonCallback;
import com.jady.sample.api.callback.ServerCallback;
import com.jady.sample.bean.ServerCallbackModel;
import com.jady.sample.bean.User;

/**
 * Created by lipingfa on 2017/6/16.
 */
public class BaseRequestFragment extends Fragment implements View.OnClickListener {
    protected View rootView;
    protected Button btnBaseRequestGet;
    protected TextView tvBaseRequestGet;

    public static BaseRequestFragment newInstance() {

        Bundle args = new Bundle();

        BaseRequestFragment fragment = new BaseRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.base_request_fra, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        btnBaseRequestGet = (Button) rootView.findViewById(R.id.btn_base_request_get);
        btnBaseRequestGet.setOnClickListener(BaseRequestFragment.this);
        tvBaseRequestGet = (TextView) rootView.findViewById(R.id.tv_base_request_get);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_base_request_get) {
            testGet();
        }
    }

    private void testGet() {
        //解析json
        API.testGet(new ServerCallback<ServerCallbackModel<User>, User>() {

            @Override
            public void onSuccess(User user) {
                showToast("用户名：" + user.getName());
            }

            @Override
            public void onFailure(String error_code, String error_message) {

            }
        });
        //不解析json
        API.testGet(new CommonCallback<String>() {

            @Override
            public void onSuccess(String json) {
                tvBaseRequestGet.setText(json);
            }

            @Override
            public void onFailure(String error_code, String error_message) {

            }
        });
    }

    private void showToast(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }
}

