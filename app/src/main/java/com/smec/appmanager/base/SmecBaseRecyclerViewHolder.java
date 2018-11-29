package com.smec.appmanager.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.smec.appmanager.beans.DownloadInfo;
import com.smec.appmanager.databinding.ItemApkBinding;
import com.smec.appmanager.utils.DownloadManager;


public class SmecBaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding binding;
    private int viewType ;

    public SmecBaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }

    public ViewDataBinding getBinding() {
        return this.binding;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

}
