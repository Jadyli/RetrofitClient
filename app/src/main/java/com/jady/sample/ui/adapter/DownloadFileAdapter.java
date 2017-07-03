package com.jady.sample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jady.retrofitclient.HttpManager;
import com.jady.retrofitclient.download.DownloadInfo;
import com.jady.sample.R;
import com.jady.sample.ui.widget.MultipleProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipingfa on 2017/6/12.
 */
public class DownloadFileAdapter extends RecyclerView.Adapter<DownloadFileAdapter.ViewHolder> {

    private Context mContext;
    private List<DownloadInfo> downloadInfoList = new ArrayList<>();

    public DownloadFileAdapter(Context context) {
        this.mContext = context;
    }

    public void setDownloadInfoList(List<DownloadInfo> downloadInfoList) {
        this.downloadInfoList.clear();
        this.downloadInfoList.addAll(downloadInfoList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.download_file_adapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.btnDownloadItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadInfo downloadInfo = downloadInfoList.get(position);
                HttpManager.getInstance().download(downloadInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return downloadInfoList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        protected MultipleProgressBar pbDownloadItem;
        protected Button btnDownloadItem;

        public ViewHolder(View itemView) {
            super(itemView);
            pbDownloadItem = (MultipleProgressBar) itemView.findViewById(R.id.pb_download_item);
            btnDownloadItem = (Button) itemView.findViewById(R.id.btn_download_item);
        }
    }

}
