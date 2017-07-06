package com.jady.sample;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.jady.sample.ui.adapter.MainVpAdapter;
import com.jady.sample.ui.fragment.BaseRequestFragment;
import com.jady.sample.ui.fragment.FileDownloadFragment;
import com.jady.sample.ui.fragment.FileUploadFragment;
import com.jady.sample.ui.fragment.MovieDemoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected TabLayout tlMain;
    protected ViewPager vpMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
        updateData();
    }

    private void initView() {
        tlMain = (TabLayout) findViewById(R.id.tl_main);
        vpMain = (ViewPager) findViewById(R.id.vp_main);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(BaseRequestFragment.newInstance());
        fragmentList.add(FileUploadFragment.newInstance());
        fragmentList.add(FileDownloadFragment.newInstance());
        fragmentList.add(MovieDemoFragment.newInstance());
        String[] titles = {"基本请求", "文件上传","文件下载","电影Demo"};
        MainVpAdapter adapter = new MainVpAdapter(getSupportFragmentManager(), fragmentList, titles);
        vpMain.setAdapter(adapter);
//        vpReportIncomeSetting.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlReportIncomeSetting));

        tlMain.setupWithViewPager(vpMain);
        tlMain.setTabMode(TabLayout.MODE_SCROLLABLE);
        tlMain.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.colorAccent));
        tlMain.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
    }

    private void updateData() {
//        API.getImageList(ImgClassify.ID1, 20, 1, new ServerCallback<ServerCallbackModel<List<ImageInfo>>, List<ImageInfo>>() {
//            @Override
//            public void onSuccess(List<ImageInfo> imageInfos) {
//                mainRvAdapter.setDownloadInfoList(imageInfos);
//            }
//
//            @Override
//            public void onFailure(String error_code, String error_message) {
//
//            }
//        });
    }
}
