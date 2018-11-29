package com.smec.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.smec.appmanager.utils.DownloadManager;

/**
 * Create by mohongwu on 2018/11/8
 */
public class AppInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        String packageName = intent.getData().getSchemeSpecificPart();
        if (context.getApplicationInfo().packageName.equals(packageName)){
            //Log.i("mohongwu","Uri: "+intent.getData());
            return;
        }
        if( (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED))
        || (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED))){
            //Log.i("mohongwu",packageName);
           // Toast.makeText(context, "安装成功"+packageName, Toast.LENGTH_LONG).show();
            DownloadManager.getInstance().deleteApkFileByName(packageName);
        }
    }
}
