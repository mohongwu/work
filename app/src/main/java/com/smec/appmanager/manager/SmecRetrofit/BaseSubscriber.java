package com.smec.appmanager.manager.SmecRetrofit;


import android.util.Log;

import rx.Subscriber;

/**
 * Created by xupeizuo on 2017/4/26.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    public BaseSubscriber() {
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        ApiErrorHelper.handleError(e);
    }

}
