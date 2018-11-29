package com.smec.appmanager;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.chenenyu.router.Router;
import com.smec.appmanager.api.SmecAppManagerApi;
import com.smec.appmanager.beans.DownloadInfo;
import com.smec.appmanager.manager.SmecApkTool.SmecApkTool;
import com.smec.appmanager.manager.SmecApkTool.bean.AppInfo;
import com.smec.appmanager.manager.SmecRealm.RealmManager;
import com.smec.appmanager.manager.SmecRetrofit.BaseSubscriber;
import com.smec.appmanager.manager.SmecRetrofit.RetrofitServiceManager;
import com.smec.appmanager.manager.SmecSession.SmecSession;
import com.smec.appmanager.utils.DownloadManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by xupeizuo on 2018/5/7.
 */

public class SmecApplication extends Application {

    private static Context mContext;
    private static SmecSession smecSession;
    private static ArrayList<AppInfo> appInfolist;
    private static PackageInfo mPackageInfo ;


    @Override
    public void onCreate() {
        super.onCreate();
        //Router.initialize(this);
        mContext=getApplicationContext();
        RetrofitServiceManager.init(mContext, SmecAppManagerApi.BASE_URL);
        RealmManager.initRealm(mContext);
        smecSession=SmecSession.initSmecSession();
        mPackageInfo=getPackageInfo(mContext);
        //mohongwu
        initDownInfo();
        //
    }

    /**
     * 扫描已安装应用
     */
    public static void initScanInstalledApp(){
        Observable.create(new Observable.OnSubscribe<ArrayList<AppInfo>>() {
            @Override
            public void call(Subscriber<? super ArrayList<AppInfo>> subscriber) {
                subscriber.onNext(SmecApkTool.scanInstalledApp(mContext.getPackageManager()));
            }
        })
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<ArrayList<AppInfo>>() {
            @Override
            public void onNext(ArrayList<AppInfo> appInfos) {
                appInfolist=appInfos;
            }
        });
    }

    /**
     * 检查app是否已安装
     * @param packageName
     * @return
     */
    public static boolean checkApp(String packageName){
        PackageInfo packageInfo = null;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 初始化包名信息
     * @param context
     * @return
     */
    private PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    public static PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public static ArrayList<AppInfo> getAppInfolist() {
        return appInfolist;
    }

    public static SmecSession getSmecSession() {
        return smecSession;
    }

    public static Context getContext() {
        return mContext;
    }

    void initDownInfo(){
        Realm realm = RealmManager.getInstance().getRealm();
        RealmResults<DownloadInfo> realmObjects = realm.where(DownloadInfo.class).findAll();
        List<DownloadInfo> infos = realm.copyFromRealm(realmObjects);
        DownloadManager.getInstance().initDownInfo(infos);
    }
}
