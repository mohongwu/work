package com.smec.appmanager;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;

import com.smec.appmanager.base.SmecBaseActivity;
import com.smec.appmanager.base.SmecBasePresenter;
import com.smec.appmanager.beans.DownloadInfo;
import com.smec.appmanager.databinding.ActivityMainBinding;
import com.smec.appmanager.manager.SmecRealm.RealmManager;
import com.smec.appmanager.module.main.SmecHomePageFragment;
import com.smec.appmanager.module.mine.SmecMineFragment;
import com.smec.appmanager.utils.DownloadManager;
import com.smec.appmanager.utils.ToastyUtils;

import java.util.ArrayList;


public class MainActivity extends SmecBaseActivity<SmecBasePresenter> {

    private SmecHomePageFragment smecHomePageFragment;
    private SmecMineFragment smecMineFragment;
    private ActivityMainBinding activityMainBinding;
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private int currentPage=0;
    private boolean isExit=false;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding=DataBindingUtil.setContentView(this,R.layout.activity_main);
        activityMainBinding.setCurrentPage(currentPage);
        _initView();
    }

    @Override
    public SmecBasePresenter getPresenter() {
        return null;
    }

    private void _initView(){
        smecHomePageFragment=SmecHomePageFragment.newInstance();
        smecMineFragment=SmecMineFragment.newInstance();
        fragments.add(smecHomePageFragment);
        fragments.add(smecMineFragment);
        activityMainBinding.smecPages.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        activityMainBinding.smecPages.setCurrentItem(currentPage);
    }

    /**
     * 切换tab页
     * @param v
     */
    public void tabSwitch(View v){
        switch (v.getId()){
            case R.id.smec_home:
                currentPage=0;
                activityMainBinding.topBarLayout.getTvTextTitle().setText("主页");
                break;
            case R.id.smec_mine:
                currentPage=1;
                activityMainBinding.topBarLayout.getTvTextTitle().setText("我的");
                break;
            default:currentPage=0;
        }
        activityMainBinding.setCurrentPage(currentPage);
        activityMainBinding.smecPages.setCurrentItem(currentPage);
        activityMainBinding.invalidateAll();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                ToastyUtils.showNormalToast(this,getString(R.string.press_again_quit_android));
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                //mohongwu 保存数据
                RealmManager.getInstance().deleteAll(DownloadInfo.class);
                RealmManager.getInstance().insertOrUpdateBatch(DownloadManager.getInstance().getAllDownloadInfo());
                //mohongwu
                appManager.finishAllActivity();
                System.exit(0);
            }
        }
        return false;
    }
}
