package com.smec.appmanager.module.mine.SmecAboutActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smec.appmanager.R;
import com.smec.appmanager.SmecApplication;
import com.smec.appmanager.base.SmecBaseActivity;
import com.smec.appmanager.base.SmecBasePresenter;
import com.smec.appmanager.databinding.ActivityAboutBinding;
import com.smec.appmanager.widget.TopBarLayout;

/**
 * Created by xupeizuo on 2018/7/11.
 */

public class SmecAboutActivity extends SmecBaseActivity<SmecBasePresenter>{

    private ActivityAboutBinding activityAboutBinding;

    @Override
    public SmecBasePresenter getPresenter() {
        return new SmecBasePresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutBinding=DataBindingUtil.setContentView(this, R.layout.activity_about);
        String versionName= SmecApplication.getPackageInfo().versionName;
        activityAboutBinding.setVersionName(versionName);
        _initView();
    }

    private void _initView(){
        activityAboutBinding.topBarLayout.setTopBarListener(new TopBarLayout.TopBarListener() {
            @Override
            public void setOnLeftClickListener() {
                finish();
            }

            @Override
            public void setOnRight1ClickListener() {

            }

            @Override
            public void setOnRight2ClickListener() {

            }
        });
    }
}
