package com.jady.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jady.sample.MainApplication;
import com.jady.sample.R;
import com.jady.sample.api.API;
import com.jady.sample.api.callback.CommonCallback;
import com.jady.sample.api.callback.ServerCallback;
import com.jady.sample.bean.ServerCallbackModel;
import com.jady.sample.bean.User;
import com.jady.sample.bean.UserForLogin;

/**
 * Created by lipingfa on 2017/6/16.
 */
public class BaseRequestFragment extends Fragment implements View.OnClickListener {
    protected View rootView;
    protected Button btnBaseRequestGet;
    protected TextView tvBaseRequestGet;
    protected EditText etBaseRequestPostName;
    protected EditText etBaseRequestPostPassword;
    protected Button btnBaseRequestPost;
    protected TextView tvBaseRequestPost;
    protected Button btnBaseRequestPostBody;
    protected Button btnBaseRequestPut;
    protected TextView tvBaseRequestPut;
    protected Button btnBaseRequestDelete;
    protected TextView tvBaseRequestDelete;

    public static BaseRequestFragment newInstance() {

        Bundle args = new Bundle();

        BaseRequestFragment fragment = new BaseRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.base_request_fra, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        btnBaseRequestGet = (Button) rootView.findViewById(R.id.btn_base_request_get);
        btnBaseRequestGet.setOnClickListener(BaseRequestFragment.this);
        tvBaseRequestGet = (TextView) rootView.findViewById(R.id.tv_base_request_get);
        etBaseRequestPostName = (EditText) rootView.findViewById(R.id.et_base_request_post_name);
        etBaseRequestPostPassword = (EditText) rootView.findViewById(R.id.et_base_request_post_password);
        btnBaseRequestPost = (Button) rootView.findViewById(R.id.btn_base_request_post_map);
        btnBaseRequestPost.setOnClickListener(BaseRequestFragment.this);
        tvBaseRequestPost = (TextView) rootView.findViewById(R.id.tv_base_request_post);
        btnBaseRequestPostBody = (Button) rootView.findViewById(R.id.btn_base_request_post_body);
        btnBaseRequestPostBody.setOnClickListener(BaseRequestFragment.this);
        btnBaseRequestPut = (Button) rootView.findViewById(R.id.btn_base_request_put);
        btnBaseRequestPut.setOnClickListener(BaseRequestFragment.this);
        tvBaseRequestPut = (TextView) rootView.findViewById(R.id.tv_base_request_put);
        btnBaseRequestDelete = (Button) rootView.findViewById(R.id.btn_base_request_delete);
        btnBaseRequestDelete.setOnClickListener(BaseRequestFragment.this);
        tvBaseRequestDelete = (TextView) rootView.findViewById(R.id.tv_base_request_delete);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_base_request_get:
                testGet();
                break;
            case R.id.btn_base_request_post_map:
                testPostByMap();
                break;
            case R.id.btn_base_request_post_body:
                testPostByMapByBody();
                break;
            case R.id.btn_base_request_put:
                testPut();
                break;
            case R.id.btn_base_request_delete:
                testDelete();
                break;
        }
    }

    /**
     * get请求
     */
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

    /**
     * post请求
     */
    private void testPostByMap() {
        String name = etBaseRequestPostName.getText().toString().trim();
        String password = etBaseRequestPostPassword.getText().toString().trim();
        //解析json
        API.testPost(name, password, new ServerCallback<ServerCallbackModel<String>, String>() {

            @Override
            public void onSuccess(String accessToken) {
                showToast("accessToken:" + accessToken);
            }

            @Override
            public void onFailure(String error_code, String error_message) {

            }

            @Override
            public boolean enableShowToast() {
                return true;
            }
        });
        //不解析json
        API.testPost(name, password, new CommonCallback<String>() {

            @Override
            public void onSuccess(String data) {
                tvBaseRequestPost.setText(data);
            }

            @Override
            public void onFailure(String error_code, String error_message) {

            }
        });
    }

    private void testPostByMapByBody() {
        String name = etBaseRequestPostName.getText().toString().trim();
        String password = etBaseRequestPostPassword.getText().toString().trim();
        UserForLogin userForLogin = new UserForLogin(name, password);

        //解析json
        API.testPost(userForLogin, new ServerCallback<ServerCallbackModel<String>, String>() {

            @Override
            public void onSuccess(String accessToken) {
                showToast("accessToken:" + accessToken);
            }

            @Override
            public void onFailure(String error_code, String error_message) {

            }

            @Override
            public boolean enableShowToast() {
                return true;
            }
        });
        //不解析json
        API.testPost(userForLogin, new CommonCallback<String>() {

            @Override
            public void onSuccess(String data) {
                tvBaseRequestPost.setText(data);
            }

            @Override
            public void onFailure(String error_code, String error_message) {

            }
        });
    }

    /**
     * put请求，这里用来更新用户信息，本demo通过accessToken来验证用户，accessToken放在Http头里面。
     * http请求头通过回调动态获取，每次都求都会拿一次，用法见{@link MainApplication#onCreate()}
     */
    private void testPut() {
        String putContent = "jady1";
        //解析json
        API.testPut(putContent, new ServerCallback<ServerCallbackModel<Object>, Object>() {

            @Override
            public void onSuccess(Object o) {
                showToast("更新成功");
            }

            @Override
            public void onFailure(String error_code, String error_message) {
                showToast("更新失败,请检查token");
            }
        });
        //不解析json
        API.testPut(putContent, new CommonCallback<String>() {

            @Override
            public void onSuccess(String json) {
                tvBaseRequestPut.setText(json);
            }

            @Override
            public void onFailure(String error_code, String error_message) {

            }
        });
    }

    private void testDelete() {
        //解析json
        API.testDelete(1,new ServerCallback<ServerCallbackModel<Object>, Object>() {

            @Override
            public void onSuccess(Object o) {
                showToast("删除成功");
            }

            @Override
            public void onFailure(String error_code, String error_message) {
                showToast("删除失败,请检查token");
            }
        });
        //不解析json
        API.testDelete(1,new CommonCallback<String>() {

            @Override
            public void onSuccess(String json) {
                tvBaseRequestDelete.setText(json);
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

