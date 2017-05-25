package com.jady.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jady.retrofitclient.HttpManager;
import com.jady.retrofitclient.callback.HttpCallback;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected TextView tvResponse;
    protected Button btnTest;
    protected EditText etPhone;
    //基础路径
    private String baseUrl = "";
    //后缀
    private String suffix = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        HttpManager.init(this, baseUrl);
        HttpManager.initCache(getCacheDir() + "/http", 10 * 1024 * 1024);
        HttpManager.getInstance().setOnGetHeadersListener(new HttpManager.OnGetHeadersListener() {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("accessToken", "");
                return headers;
            }
        });
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_test) {
            String phone = etPhone.getText().toString().trim();
            Map<String, Object> parameters = new HashMap<>();
            Feed feed = new Feed();
            feed.setContent("ddd");
            feed.setImages("323");
            feed.setLocation("dd");

            //这个ServerCallback是针对我公司的服务的，不适应其他场景，
            // 所以不需要加，一般直接传回字节流，用Gson转为你自己的就行。
//            HttpManager.getInstance().post(suffix, parameters, new HttpCallback<ServerCallback<Object>>() {

            parameters.put("feed_id", 31);
            parameters.put("cannel", 0);
            HttpManager.getInstance().put(suffix, parameters, new HttpCallback() {

                @Override
                public void onResolve(Object o) {
                    System.out.println(o.toString());
                }

                @Override
                public void onFailed(String err_code, String message) {
                    System.out.println(message);
                }
            });
        }
    }

    private void initView() {
        tvResponse = (TextView) findViewById(R.id.tv_response);
        btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(MainActivity.this);
        etPhone = (EditText) findViewById(R.id.et_phone);
    }
}
