package com.jady.sample.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jady.sample.R;
import com.jady.sample.api.API;
import com.jady.sample.api.callback.CommonCallback;
import com.jady.sample.bean.movie.MovieListInfo;
import com.jady.sample.bean.movie.Subjects;
import com.jady.sample.ui.adapter.MovieDemoRvAdapter;

import java.util.List;

/**
 * Created by lipingfa on 2017/7/6.
 */
public class MovieDemoFragment extends Fragment {
    protected View rootView;
    protected RecyclerView rvMovieDemoFra;
    protected SwipeRefreshLayout srlMovieDemoFra;
    private MovieDemoRvAdapter adapter;
    private boolean isLoadingMore = false;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;
    private int mMonvieStart = 0, mMovieCount = 10;

    public static MovieDemoFragment newInstance() {

        Bundle args = new Bundle();

        MovieDemoFragment fragment = new MovieDemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_demo_fra, container, false);
        initView(rootView);
        initEvent();
        srlMovieDemoFra.setRefreshing(true);
        updateData(false);
        return rootView;
    }

    private void initView(View rootView) {
        rvMovieDemoFra = (RecyclerView) rootView.findViewById(R.id.rv_movie_demo_fra);
        srlMovieDemoFra = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_movie_demo_fra);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvMovieDemoFra.setLayoutManager(linearLayoutManager);
        adapter = new MovieDemoRvAdapter(getActivity());
        rvMovieDemoFra.setAdapter(adapter);

        initSwipLaout();
    }

    private void initSwipLaout() {
        srlMovieDemoFra.setProgressBackgroundColorSchemeResource(android.R.color.white);
        srlMovieDemoFra.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srlMovieDemoFra.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
    }

    private void initEvent() {
        srlMovieDemoFra.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMonvieStart = 0;
                updateData(false);
            }
        });

        rvMovieDemoFra.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isLoadingMore) {
                    if (lastVisibleItem + 1 >= adapter.getItemCount() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        isLoadingMore = true;
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.changeMoreStatus(MovieDemoRvAdapter.LOADING_MORE);
                                updateData(true);
                                mMonvieStart += mMovieCount;
                            }
                        });
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void updateData(final boolean isLoadMore) {
        API.getMovieList(mMonvieStart, mMovieCount, "上海", new CommonCallback<MovieListInfo>() {

            @Override
            public void onSuccess(MovieListInfo data) {
                List<Subjects> subjects = data.getSubjects();
                if (subjects != null && subjects.size() > 0) {
                    if (isLoadMore) {
                        if (data.getTotal() <= adapter.getItemCount()) {
                            adapter.changeMoreStatus(MovieDemoRvAdapter.NO_MORE_DATA);
                        } else {
                            adapter.addMore(subjects);
                        }
                    } else {
                        mMonvieStart = mMovieCount;
                        if (srlMovieDemoFra.isRefreshing()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    srlMovieDemoFra.setRefreshing(false);
                                }
                            }, 1000);
                        }
                        adapter.setDataList(subjects);
                    }
                } else {
                    adapter.changeMoreStatus(MovieDemoRvAdapter.NO_MORE_DATA);
                }
                isLoadingMore = false;
            }

            @Override
            public void onFailure(String s, String s1) {
                srlMovieDemoFra.setRefreshing(false);
                isLoadingMore = false;
            }
        });
    }
}
