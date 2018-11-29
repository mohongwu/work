package com.smec.appmanager.base;
import android.content.Context;


import com.smec.appmanager.SmecApplication;
import com.smec.appmanager.api.SmecAppManagerApi;
import com.smec.appmanager.api.SmecAppManagerService;
import com.smec.appmanager.manager.SmecRealm.RealmManager;
import com.smec.appmanager.manager.SmecRetrofit.ApiException;
import com.smec.appmanager.manager.SmecRetrofit.RetrofitServiceManager;
import com.smec.appmanager.utils.NetworkState;

import java.io.Serializable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by xupeizuo on 2017/7/20.
 */

public class SmecBasePresenter implements Serializable{

    protected Context mContext;
    protected RealmManager realmManager;
    protected SmecAppManagerService smecAppManagerService;

    public SmecBasePresenter(Context mContext) {
        this.mContext = mContext;
        realmManager= RealmManager.getInstance();
        smecAppManagerService= RetrofitServiceManager.getSmecAppManagerService();
    }

    /**
     * 调度线程，绑定生命周期，判断有无网络连接
     * @param observable
     * @param <T>
     * @return
     */
    protected <T> Observable<T> schedulerThread(Observable<T> observable){
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if(!NetworkState.networkConnected(SmecApplication.getContext())){
                            throw new ApiException("0","无网络连接");
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
