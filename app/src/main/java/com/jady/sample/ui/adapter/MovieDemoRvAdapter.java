package com.jady.sample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jady.sample.R;
import com.jady.sample.bean.movie.Casts;
import com.jady.sample.bean.movie.Directors;
import com.jady.sample.bean.movie.Subjects;
import com.jady.sample.support.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipingfa on 2017/6/12.
 */
public class MovieDemoRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Subjects> dataList = new ArrayList<>();

    //上拉加载更多
    public static final int NO_MORE_DATA = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    public static final int LOADING_HIDE = 2;
    //上拉加载更多状态-默认为0
    private int load_more_status = LOADING_HIDE;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private LayoutInflater mInflater;

    public MovieDemoRvAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setDataList(List<Subjects> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        load_more_status = LOADING_MORE;
        notifyDataSetChanged();
    }

    public void addMore(List<Subjects> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                View itemView = mInflater.inflate(R.layout.movie_demo_item_content, parent, false);
                return new ContentViewHolder(itemView);
            case TYPE_FOOTER:
                View footView = mInflater.inflate(R.layout.movie_demo_item_foot, parent, false);
                return new FootViewHolder(footView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            Subjects subjects = dataList.get(position);
            String imgUrl = subjects.getImages().getMedium();
            GlideApp.with(mContext).load(imgUrl).placeholder(R.drawable.img_default).dontAnimate().into(contentViewHolder.ivMovieDemoItemAvatar);
            //标题
            contentViewHolder.tvMovieDemoItemTitle.setText(subjects.getTitle());
            //导演
            StringBuilder sbDirectors = new StringBuilder();
            sbDirectors.append("导演：");
            if (subjects.getDirectors() != null && subjects.getDirectors().size() > 0) {
                for (Directors director : subjects.getDirectors()) {
                    sbDirectors.append(director.getName() + "\t");
                }
            }
            contentViewHolder.tvMovieDemoItemDirectors.setText(sbDirectors);
            //演员
            StringBuilder sbCasts = new StringBuilder();
            sbCasts.append("演员：");
            if (subjects.getCasts() != null && subjects.getCasts().size() > 0) {
                for (Casts casts : subjects.getCasts()) {
                    sbCasts.append(casts.getName() + "\t");
                }
            }
            contentViewHolder.tvMovieDemoItemCasts.setText(sbCasts);

            float average = subjects.getRating().getAverage();
            String rating = average + "分";
            if (average <= 0) {
                rating = "未评分";
            }
            contentViewHolder.tvMovieDemoItemRating.setText(rating);
        } else if (holder instanceof FootViewHolder) {
            final FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case NO_MORE_DATA:
                    footViewHolder.tvMovieDemoItemFoot.setVisibility(View.VISIBLE);
                    footViewHolder.tvMovieDemoItemFoot.setText("没有更多数据了...");
                    break;
                case LOADING_MORE:
                    footViewHolder.tvMovieDemoItemFoot.setVisibility(View.VISIBLE);
                    footViewHolder.tvMovieDemoItemFoot.setText("正在加载更多数据...");
//                    holder.pbHomeRvFoot.setVisibility(View.VISIBLE);
                    break;
                case LOADING_HIDE:
                    footViewHolder.tvMovieDemoItemFoot.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        protected ImageView ivMovieDemoItemAvatar;
        protected TextView tvMovieDemoItemTitle;
        protected TextView tvMovieDemoItemDirectors;
        protected TextView tvMovieDemoItemCasts;
        protected TextView tvMovieDemoItemRating;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ivMovieDemoItemAvatar = (ImageView) itemView.findViewById(R.id.iv_movie_demo_item_avatar);
            tvMovieDemoItemTitle = (TextView) itemView.findViewById(R.id.tv_movie_demo_item_title);
            tvMovieDemoItemDirectors = (TextView) itemView.findViewById(R.id.tv_movie_demo_item_directors);
            tvMovieDemoItemCasts = (TextView) itemView.findViewById(R.id.tv_movie_demo_item_casts);
            tvMovieDemoItemRating = (TextView) itemView.findViewById(R.id.tv_movie_demo_item_rating);
        }
    }

    /**
     * //没有更多数据了
     * NO_MORE_DATA=0;
     * //正在加载中
     * LOADING_MORE=1;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvMovieDemoItemFoot;

        public FootViewHolder(View itemView) {
            super(itemView);
            tvMovieDemoItemFoot = (TextView) itemView.findViewById(R.id.tv_movie_demo_item_foot);
        }
    }
}
