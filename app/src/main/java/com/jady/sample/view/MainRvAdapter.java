package com.jady.sample.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jady.sample.R;
import com.jady.sample.bean.ImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipingfa on 2017/6/12.
 */
public class MainRvAdapter extends RecyclerView.Adapter<MainRvAdapter.ViewHolder> {

    private Context mContext;
    private List<ImageInfo> imageInfoList = new ArrayList<>();

    public MainRvAdapter(Context context) {
        this.mContext = context;
    }

    public void setImageInfoList(List<ImageInfo> imageInfoList) {
        this.imageInfoList.clear();
        this.imageInfoList.addAll(imageInfoList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageInfo imageInfo = imageInfoList.get(position);
        holder.ivHome.setUrl(imageInfo.getImg(),150,300);
        holder.tvHomeImageTitle.setText(imageInfo.getTitle());
    }

    @Override
    public int getItemCount() {
        return imageInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        protected FrescoImageView ivHome;
        protected TextView tvHomeImageTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ivHome = (FrescoImageView) itemView.findViewById(R.id.iv_home_item);
            tvHomeImageTitle = (TextView) itemView.findViewById(R.id.tv_home_welfare_des);
        }
    }

}
