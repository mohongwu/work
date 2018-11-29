package com.smec.appmanager.manager.SmecApkTool;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.smec.appmanager.manager.SmecApkTool.bean.AppInfo;
import com.smec.appmanager.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeizuo on 2018/6/28.
 */

public class SmecApkTool {


    public static ArrayList<AppInfo> scanInstalledApp(PackageManager packageManager){
        ArrayList<AppInfo> list=new ArrayList<>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            if(CommonUtils.notEmpty(packageInfos)){
                for(int i=0;i<packageInfos.size();i++){
                    PackageInfo packageInfo = packageInfos.get(i);
                    //过滤系统app
                    if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0){
                        continue;
                    }
                    if(packageInfo.applicationInfo.loadIcon(packageManager) == null){
                        continue;
                    }
                    AppInfo appInfo = new AppInfo();
                    appInfo.setAppName(packageInfo.packageName);
                    appInfo.setVersionName(packageInfo.versionName);
                    list.add(appInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
