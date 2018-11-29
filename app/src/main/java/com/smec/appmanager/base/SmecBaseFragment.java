package com.smec.appmanager.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xupeizuo on 2017/7/20.
 */

public abstract class SmecBaseFragment<T> extends Fragment {

    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter= getPersenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected abstract T getPersenter();

    /**
     * 调度线程
     * @param observable
     * @param <T>
     * @return
     */
    protected <T> Observable<T> schedulerThread(Observable<T> observable){
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
