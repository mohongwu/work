package com.smec.appmanager.module.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.appmanager.GlideApp;
import com.smec.appmanager.R;
import com.smec.appmanager.api.SmecAmpConstant;
import com.smec.appmanager.base.SmecBaseRecyclerViewHolder;
import com.smec.appmanager.beans.DownloadInfo;
import com.smec.appmanager.databinding.ItemApkBinding;
import com.smec.appmanager.dto.AllApkDto;
import com.smec.appmanager.manager.SmecPreference.PreferencesHelper;
import com.smec.appmanager.manager.SmecRetrofit.SmecAutoUpdate;
import com.smec.appmanager.manager.SmecSession.SmecSession;
import com.smec.appmanager.module.main.ampdetails.SmecAmpDetailsActivity;
import com.smec.appmanager.module.main.SmecHomePageContract;
import com.smec.appmanager.utils.CommonUtils;
import com.smec.appmanager.utils.DownloadManager;
import com.smec.appmanager.utils.ThreadPoolExecutorUtil;
import com.smec.appmanager.utils.ToastyUtils;
import com.smec.appmanager.widget.ProgressButton;

import java.io.File;
import java.util.ArrayList;

import static com.smec.appmanager.api.SmecAmpConstant._appOperator;
import static com.smec.appmanager.api.SmecAmpConstant.checkAppStatus;

/**
 * Created by xupeizuo on 2018/6/26.
 */

public class HomePageAdapter extends RecyclerView.Adapter<SmecBaseRecyclerViewHolder>{

    private Context mContext;
    private ArrayList<AllApkDto> list=new ArrayList<>();

    public HomePageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public SmecBaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemApkBinding itemApkBinding=DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_apk,parent,false);
        SmecBaseRecyclerViewHolder smecBaseRecyclerViewHolder=new SmecBaseRecyclerViewHolder(itemApkBinding.getRoot());
        smecBaseRecyclerViewHolder.setBinding(itemApkBinding);
        smecBaseRecyclerViewHolder.setViewType(viewType);
        return smecBaseRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(SmecBaseRecyclerViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final ItemApkBinding itemApkBinding=(ItemApkBinding)holder.getBinding();
        final AllApkDto allApkDto=list.get(position);
        String status=checkAppStatus(mContext,allApkDto,itemApkBinding.smecOperator);
        //Log.i("mohongwu","onBindViewHolder getStatus: "+status);
        allApkDto.getAmpApk().setStatus(status);
        itemApkBinding.setViewModel(list.get(position));
        /*itemApkBinding.smecIcon.setImageURL(list.get(position).getAmpApk().getIcon(),
                allApkDto.getAmpApk().getBitmap(), new SmecHomePageContract.Imagelistener() {
                    @Override
                    public void failure( ) {

                    }

                    @Override
                    public void success(Bitmap bitmap) {
                        allApkDto.getAmpApk().setBitmap(bitmap);
                    }
                });*/
        if (!PreferencesHelper.getInstance().getNoImageState()){
            GlideApp.with(mContext).load(list.get(position).getAmpApk().getIcon()).into(itemApkBinding.smecIcon);
        }

       /* itemApkBinding.smecOperator.setOnProgressButtonClickListener(new ProgressButton.OnProgressButtonClickListener() {
            @Override
            public void onClickListener() {
                _appOperator(mContext,allApkDto,itemApkBinding.smecOperator,null);
            }
        });*/

       itemApkBinding.smecOperator.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!SmecAmpConstant.isUsePause){
                   _appOperator(mContext,allApkDto,(ProgressButton)v,null);
               }else {
                   DownloadInfo info = DownloadManager.getInstance().getDownloadInfo(allApkDto.getAmpApk());

                   if ((info == null) || (info.size == 0)){
                       //Log.i("mohongwu","OnClick : "+allApkDto.getAmpApk().getPackageName());
                       _appOperator(mContext,allApkDto,(ProgressButton)v,null);
                   }else {
                       //Log.i("mohongwu","OnClick : "+info.currentState +"，path: "+info.path);
                       if (DownloadManager.STATE_DOWNLOAD == info.currentState){
                           DownloadManager.getInstance().pause(allApkDto.getAmpApk());
                       }else if(info.currentState == DownloadManager.STATE_PAUSE){
                            //DownloadManager.getInstance().download(allApkDto.getAmpApk());
                           _appOperator(mContext,allApkDto,(ProgressButton)v,null);
                       }else if (info.currentState == DownloadManager.STATE_INSTALL){
                           //已下载到本地的apk，应检查存在性
                           File file = new File(info.path);
                           if (!file.exists()){
                               info.size = 0;
                               ToastyUtils.showNormalToast(mContext,"apk文件已被删除，请重新下载！");
                               DownloadManager.getInstance().deleteDownloadInfo(info.packageName);
                               //notifyDataSetChanged();
                               ((ProgressButton)v).setProgress(0);
                               return;
                           }
                           SmecAutoUpdate.installApk((Activity) mContext,file);
                       }
                   }

               }

           }
       });
        itemApkBinding.smecApkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, SmecAmpDetailsActivity.class);
                /*if(allApkDto.getAmpApk().getBitmap()!=null){
                    SmecSession.setCurrentBMap(CommonUtils.getBytes(allApkDto.getAmpApk().getBitmap()));
                }*/
                intent.putExtra("AllApkDto",allApkDto);
                mContext.startActivity(intent);
            }
        });
        //mohongwu
        if (SmecAmpConstant.isUsePause) {
            DownloadManager.getInstance().registerObserver(allApkDto.getAmpApk(), itemApkBinding.smecOperator);
            DownloadInfo info = DownloadManager.getInstance().getDownloadInfo(allApkDto.getAmpApk());
            //Log.i("mohongwu","Observer: "+DownloadManager.getInstance().getObservers().size());
            if (info != null) {
                //判断本地下载的apk版本和服务器比较是否为最新
                if (!info.getVersionName().equals(allApkDto.getAmpApk().getVersionName())) {
                    DownloadManager.getInstance().deleteDownloadInfo(allApkDto.getAmpApk().getVersionName());
                } else {
                    if (info.currentState == DownloadManager.STATE_DOWNLOAD) {
                        itemApkBinding.smecOperator.setText("下载中");
                    } else if (info.currentState == DownloadManager.STATE_PAUSE) {
                        if (info.currentPos > 0) {
                            itemApkBinding.smecOperator.setText("暂停");
                            itemApkBinding.smecOperator.setMax(info.size);
                            itemApkBinding.smecOperator.setProgress(info.currentPos);
                        } else {
                            itemApkBinding.smecOperator.setText("等待");
                        }
                    } else if (info.currentState == DownloadManager.STATE_INSTALL) {
                        itemApkBinding.smecOperator.setText("安装");
                        itemApkBinding.smecOperator.setMax(info.size);
                        itemApkBinding.smecOperator.setProgress(info.currentPos);
                        info.currentState = DownloadManager.STATE_INSTALL;
                    }
                }
            }
        }
        //mohongwu

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<AllApkDto> list) {
       if(CommonUtils.notEmpty(list)){
           this.list=list;
       }else {
           this.list=new ArrayList<>();
       }
        notifyDataSetChanged();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SmecBaseRecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }
}
