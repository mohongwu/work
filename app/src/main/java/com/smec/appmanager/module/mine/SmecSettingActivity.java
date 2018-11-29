package com.smec.appmanager.module.mine;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.smec.appmanager.R;
import com.smec.appmanager.base.SmecBaseActivity;
import com.smec.appmanager.beans.DownloadInfo;
import com.smec.appmanager.manager.SmecPreference.PreferencesHelper;
import com.smec.appmanager.utils.FileUtils;
import com.smec.appmanager.utils.ToastyUtils;
import com.smec.appmanager.widget.TopBarLayout;

import java.io.File;
import java.math.BigDecimal;

/**
 * Create by mohongwu on 2018/11/12
 */
public class SmecSettingActivity extends SmecBaseActivity<SmecMinePresenter> implements CompoundButton.OnCheckedChangeListener{
    private TopBarLayout topBarLayout;
    private TextView tvSettingClear;
    AppCompatCheckBox cbSettingImage;

    private File cacheFile;
    private File downApkFile;

    @Override
    public SmecMinePresenter getPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cacheFile = new File(getCacheDir(), "HttpCache");
        downApkFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + DownloadInfo.APPMANAGER + File.separator + DownloadInfo.DOWNLOAD);
        _initView();
    }

    private void _initView(){
        tvSettingClear = findViewById(R.id.tv_setting_clear);
        cbSettingImage = findViewById(R.id.cb_setting_image);
        topBarLayout = findViewById(R.id.topBarLayout);


        tvSettingClear.setText(getFormatSize(FileUtils.sizeOfDirectory(cacheFile)));
        cbSettingImage.setChecked(PreferencesHelper.getInstance().getNoImageState());
        cbSettingImage.setOnCheckedChangeListener(this);
        topBarLayout.setTopBarListener(new TopBarLayout.TopBarListener() {
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_setting_image:
                PreferencesHelper.getInstance().setNoImageState(isChecked);
                break;
            /*case R.id.cb_setting_cache:
                mPresenter.setAutoCacheState(b);
                break;*/
        }
    }

    public void doClear(View view) {
        if (cacheFile.exists()){
            FileUtils.deleteDir(cacheFile);
        }
        if (downApkFile.exists()){
            FileUtils.deleteDir(downApkFile);
        }
        if (!cacheFile.exists()){
            boolean is = cacheFile.mkdir();
        }
        if (!downApkFile.exists()){
            boolean is = downApkFile.mkdir();
            if (!is){
                ToastyUtils.showNormalToast(this,"文件夹无法创建，请关闭文件管理器！");
            }
        }
        tvSettingClear.setText(getFormatSize(FileUtils.sizeOfDirectory(cacheFile)));
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

}
