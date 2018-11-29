package com.smec.appmanager.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.smec.appmanager.manager.SmecAppManager.SmecAppManager;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by xupeizuo on 2017/7/20.
 */

public abstract class SmecBaseActivity<T> extends RxAppCompatActivity {

    protected T mPresenter;
    protected SmecAppManager appManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mPresenter=getPresenter();
        appManager= SmecAppManager.getInstance();
        appManager.addActivity(this);
        _initStatus();
    }

    public abstract T getPresenter();

    protected void _initStatus(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //appManager.finishActivity();
    }
}
