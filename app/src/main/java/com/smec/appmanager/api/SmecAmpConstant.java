package com.smec.appmanager.api;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.smec.appmanager.R;
import com.smec.appmanager.SmecApplication;
import com.smec.appmanager.beans.AmpApk;
import com.smec.appmanager.beans.AmpLabel;
import com.smec.appmanager.beans.DownloadInfo;
import com.smec.appmanager.dialog.CheckPassword;
import com.smec.appmanager.dto.AllApkDto;
import com.smec.appmanager.manager.SmecApkTool.bean.AppInfo;
import com.smec.appmanager.manager.SmecRetrofit.SmecAutoUpdate;
import com.smec.appmanager.manager.SmecSession.SmecSession;
import com.smec.appmanager.module.main.SmecHomePagePresenter;
import com.smec.appmanager.utils.CommonUtils;
import com.smec.appmanager.utils.DownloadManager;
import com.smec.appmanager.utils.ThreadPoolExecutorUtil;
import com.smec.appmanager.utils.ToastyUtils;
import com.smec.appmanager.widget.ProgressButton;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import static com.smec.appmanager.SmecApplication.getContext;

/**
 * Created by xupeizuo on 2017/8/30.
 */

public final class SmecAmpConstant implements Serializable{

    /**
     * 下拉刷新头部
     */
    private static SinaRefreshView sinaRefreshView;
    public final static boolean isUsePause =true;

    public static SinaRefreshView getSinaRefreshView() {
        sinaRefreshView=new SinaRefreshView(getContext());
        sinaRefreshView.setArrowResource(R.drawable.smec_refresh);
        sinaRefreshView.setTextColor(getContext().getResources().getColor(R.color.text_black));
        return sinaRefreshView;
    }


    /**
     * 检查apk状态
     * @param allApkDto
     * @return
     */
    public static String checkAppStatus(Context context,AllApkDto allApkDto,ProgressButton view){
        AmpApk ampApk=allApkDto.getAmpApk();
        if(SmecAppManagerApi.AppStatus.DOWNLOADING.getInfo().equals(ampApk.getStatus())){
            view.setText(ampApk.getStatus());
            view.setBackgroundColor(context.getResources().getColor(R.color.green));
            return ampApk.getStatus();
        }

        if(ampApk.getPackageName() == null || ampApk.getPackageName().equals("")){
            view.setBackgroundColor(context.getResources().getColor(R.color.top_bar));
            view.setText(SmecAppManagerApi.AppStatus.INSTALL.getInfo());
            return SmecAppManagerApi.AppStatus.INSTALL.getInfo();
        }
        ArrayList<AppInfo> appInfos= SmecApplication.getAppInfolist();
        if(CommonUtils.notEmpty(appInfos)){
            for(int i=0;i<appInfos.size();i++){
                AppInfo appInfo=appInfos.get(i);
                if(ampApk.getPackageName().equals(appInfo.getAppName())){
                    if(ampApk.getVersionName().equals(appInfo.getVersionName())){
                        view.setText(SmecAppManagerApi.AppStatus.OPEN.getInfo());
                        view.setBackgroundColor(context.getResources().getColor(R.color.green));
                        return SmecAppManagerApi.AppStatus.OPEN.getInfo();
                    }else {
                        return  _setUpdate(context,allApkDto,SmecAppManagerApi.AppStatus.UPDATE.getInfo(),view);
                    }
                }
            }
        }
        return  _setUpdate(context,allApkDto,SmecAppManagerApi.AppStatus.DOWNLOAD.getInfo(),view);
    }

    /**
     * 设置更新以及安装
     * @param status
     * @param view
     */
    public static String _setUpdate(Context context,AllApkDto allApkDto,String status,ProgressButton view){
        String permission=_checkPermission(allApkDto);
        if("T".equals(permission)){
            view.setBackgroundColor(context.getResources().getColor(R.color.top_bar));
        }else if("F".equals(permission)){
            view.setBackgroundColor(context.getResources().getColor(R.color.login_font));
        }else {
            view.setBackgroundColor(context.getResources().getColor(R.color.top_bar));
            view.setText(SmecAppManagerApi.AppStatus.PASSWORD.getInfo());
            return SmecAppManagerApi.AppStatus.PASSWORD.getInfo();
        }
        view.setText(status);
        return status;
    }


    /**
     * 检查是否有下载权限
     * @param allApkDto
     */
    public static String _checkPermission(AllApkDto allApkDto){
        if(SmecSession.getUserlabelDto().isAdmin()){//如果是管理员则全部可以下载
            return "T";
        }
        if("T".equals(allApkDto.getAmpApk().getIsPliot())){//如果该apk是试运行的 分组用户以及知道密码的用户可以下载
            //1 先判断是否是分组用户 分组用户不需要输入密码
            if(_checklabel(allApkDto,true)||isRole(allApkDto)){
                return "T";
            }else {//2 不是分组用户则需要输入密码 弹框提示输入密码
                return "";
            }
        }
        return (_checklabel(allApkDto,false)||isRole(allApkDto)) ? "T" : "F"; //如果不是管理员又不是试运行的apk 则需要分组用户或者角色用户才能下载
    }

    /**
     * 判断是否有角色权限
     * @param allApkDto
     * @return
     */
    public static boolean isRole(AllApkDto allApkDto){
        if(!CommonUtils.notEmpty(allApkDto.getAmpLabels())){
            return true;//没有分组及角色信息可以全部下载
        }
        if(SmecSession.getSmecUser().getUserRole()!=null){
            String [] roles=SmecSession.getSmecUser().getUserRole().split(",");
            boolean isRole=false;
            ArrayList<AmpLabel> ampLabels=allApkDto.getAmpLabels();
            for(int i=0;i<roles.length;i++){
                String r=roles[i];
                for(AmpLabel a:ampLabels){
                    if(r.equals(a.getRoleCode())){
                        isRole=true;
                        break;
                    }
                }
            }
            return isRole;
        }
        return false;//用户没有权限 则不可以下载
    }

    /**
     * 检查是否是分组用户
     * @param allApkDto
     * @return
     */
    public static boolean _checklabel(AllApkDto allApkDto,boolean isT){
        boolean isHave=false;
        ArrayList<AmpLabel> apklabel=allApkDto.getAmpLabels();//apk 需要的分组信息;
        ArrayList<AmpLabel> userlabel= SmecSession.getUserlabelDto().getList();//用户所在的分组信息

        if(!CommonUtils.notEmpty(apklabel)){//如果apk没有分组信息
            if(isT){
                return false;//如果是试运行则需要输入密码
            }else {
                return true;//如果不是则可以直接下载
            }
        }
        if(!CommonUtils.notEmpty(userlabel)){//如果用户没有分组信息 则全部不可以下载
            return false;
        }
        for(int i=0;i<userlabel.size();i++){
            int uId=userlabel.get(i).getId();
            for(int j=0;j<apklabel.size();j++){
                int aId=apklabel.get(j).getId();
                if(uId == aId){
                    isHave=true;
                    break;
                }
            }
        }
        return isHave;
    }

    /**
     * 根据不同的apk状态进行不同的操作
     * @param allApkDto
     */
    public static void _appOperator(final Context context, AllApkDto allApkDto, final ProgressButton view, CheckPassword checkPassword){
        final AmpApk ampApk=allApkDto.getAmpApk();
        Log.i("mohongwu","_appOperator getStatus: "+ampApk.getStatus());
        switch (ampApk.getStatus()){
            case "打开":
                ToastyUtils.showNormalToast(context,"正在尝试打开应用");
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(SmecApplication.checkApp(ampApk.getPackageName())){
                            Intent intent = SmecApplication.getContext().getPackageManager().getLaunchIntentForPackage(ampApk.getPackageName());
                            context.startActivity(intent);
                        }else {
                            ToastyUtils.showNormalToast(context,"未检测到已安装的app,请刷新重试!");
                        }
                    }
                },700);
                break;
            case "下载":
                _switchPermission(context,allApkDto, view,checkPassword);
                break;
            case "更新":
                _switchPermission(context,allApkDto, view,checkPassword);
                break;
            case "密码":
                if(checkPassword == null){
                    checkPassword=new CheckPassword(context, R.style.project_back, new CheckPassword.Passwordlistener() {
                        @Override
                        public void cancle(Dialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void makeSure(String password, Dialog dialog) {
                            if(password!=null && ampApk.getPassword()!=null && password.equals(ampApk.getPassword())){
                                dialog.dismiss();
                                _downloadApk(context,ampApk,view,new ProgressDialog(context));
                            }else {
                                ToastyUtils.showNormalToast(context,"密码错误,请重新输入!");
                            }
                        }
                    });
                }
                checkPassword.show();
                break;
            case "下载中":
                Log.e("mohongwu","下载中");
                if (isUsePause){
                    _switchPermission(context,allApkDto, view,checkPassword);
                }
                break;
            case "安装中":
                Log.e("mohongwu","安装中");
                break;
        }
    }

    /**
     * 检查对应权限
     * @param allApkDto
     * @param view
     */
    public static void _switchPermission(final Context context, AllApkDto allApkDto, final ProgressButton view, CheckPassword checkPassword){
        final AmpApk ampApk=allApkDto.getAmpApk();
        if("T".equals(_checkPermission(allApkDto))){
            _downloadApk(context,ampApk,view, new ProgressDialog(context));
        }else if("F".equals(_checkPermission(allApkDto))){
            ToastyUtils.showNormalToast(context,"没有权限下载该应用,请联系应用负责人");
        }else {
            if(checkPassword == null){
                checkPassword=new CheckPassword(context, R.style.project_back, new CheckPassword.Passwordlistener() {
                    @Override
                    public void cancle(Dialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void makeSure(String password, Dialog dialog) {
                        if(password!=null && ampApk.getPassword()!=null && password.equals(ampApk.getPassword())){
                            dialog.dismiss();
                            _downloadApk(context,ampApk,view,new ProgressDialog(context));
                        }else {
                            ToastyUtils.showNormalToast(context,"密码错误,请重新输入!");
                        }
                    }
                });
            }
            checkPassword.show();
        }
    }

    /**
     * 开始更新下载apk
     * @param ampApk
     * @param view
     */
    public static void _downloadApk(final Context context, final AmpApk ampApk, final ProgressButton view, final ProgressDialog progressDialog) {

        /**
         * 添加下载记录
         */
        SmecHomePagePresenter presenter = new SmecHomePagePresenter(SmecApplication.getContext());
        presenter.newDownloadRecord(ampApk.getId());


        if (isUsePause) {
            DownloadManager.getInstance().download(ampApk);
        } else {

            progressDialog.setMessage("正在下载...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            view.setText(SmecAppManagerApi.AppStatus.DOWNLOADING.getInfo());//正在下载
            ampApk.setStatus(SmecAppManagerApi.AppStatus.DOWNLOADING.getInfo());
        SmecAutoUpdate.initAutoUpdate(ampApk.getUrl());
        ThreadPoolExecutorUtil.getFixedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (view.getProgress() == 0) {
                    File file = SmecAutoUpdate.startDownload(view);


                    progressDialog.dismiss();
                    view.setText(SmecAppManagerApi.AppStatus.INSRALLING.getInfo());//安装中
                    ampApk.setStatus(SmecAppManagerApi.AppStatus.INSRALLING.getInfo());
                    view.setBackgroundColor(context.getResources().getColor(R.color.green));
                    try {
                        SmecAutoUpdate.installApk((Activity) context, file);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastyUtils.showNormalToast(context, "文件下载出错,请刷新重试!");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastyUtils.showNormalToast(context, "未知错误,请重试!");
                            }
                        });
                    }

                } else {
                    view.setProgress(0);
                }
            }
        });
    }
    }
}
