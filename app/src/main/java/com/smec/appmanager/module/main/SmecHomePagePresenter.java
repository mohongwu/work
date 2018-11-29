package com.smec.appmanager.module.main;

import android.content.Context;
import android.util.Log;

import com.smec.appmanager.base.SmecBasePresenter;
import com.smec.appmanager.beans.AmpApkDownload;
import com.smec.appmanager.beans.AmpLabel;
import com.smec.appmanager.dto.AllApkDto;
import com.smec.appmanager.dto.UserlabelDto;
import com.smec.appmanager.manager.SmecRetrofit.BaseSubscriber;
import com.smec.appmanager.manager.SmecRetrofit.HttpResult;
import com.smec.appmanager.manager.SmecSession.SmecSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by xupeizuo on 2018/6/26.
 */

public class SmecHomePagePresenter extends SmecBasePresenter implements SmecHomePageContract{

    public SmecHomePagePresenter(Context mContext) {
        super(mContext);
    }

    /**
     * 获取所有apk列表
     * @param apkslistener
     */
    @Override
    public void getAllApks(final Apkslistener apkslistener) {
        Observable<HttpResult<ArrayList<AllApkDto>>> observable=smecAppManagerService.getAllApk();
        Observable<HttpResult<UserlabelDto>> observable1=smecAppManagerService.getUserlabel();

        Observable<Map> mapObservable= Observable.zip(observable, observable1, new Func2<HttpResult<ArrayList<AllApkDto>>, HttpResult<UserlabelDto>, Map>() {
            @Override
            public Map call(HttpResult<ArrayList<AllApkDto>> arrayListHttpResult, HttpResult<UserlabelDto> userlabelDtoHttpResult) {
                Map<String,Object> map=new HashMap<String, Object>();
                map.put("apks",arrayListHttpResult.getData());
                map.put("userlabel",userlabelDtoHttpResult.getData());
                return map;
            }
        });

        schedulerThread(mapObservable).subscribe(new BaseSubscriber<Map>() {

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                apkslistener.failure();
            }

            @Override
            public void onNext(Map map) {
                if(map!=null && !map.isEmpty()){
                    ArrayList<AllApkDto> list=(ArrayList<AllApkDto>)map.get("apks");
                    UserlabelDto userlabel=(UserlabelDto)map.get("userlabel");
                    SmecSession.setUserlabelDto(userlabel);
                    apkslistener.success(list);
                }
            }
        });

    }

    /**
     * 新增下载记录
     * @param id
     */
    public void newDownloadRecord(int id){
        AmpApkDownload ampApkDownload=new AmpApkDownload(id);
        Observable<HttpResult> observable=smecAppManagerService.newDownloadRecord(ampApkDownload);
        schedulerThread(observable).subscribe(new BaseSubscriber<HttpResult>() {

            @Override
            public void onError(Throwable e) {
                Log.e("新增记录失败",e.getMessage());
            }

            @Override
            public void onNext(HttpResult httpResult) {
                Log.e("新增记录成功",httpResult.getMessage());
            }
        });
    }
}
