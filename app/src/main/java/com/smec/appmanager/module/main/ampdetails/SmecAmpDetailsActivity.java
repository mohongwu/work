package com.smec.appmanager.module.main.ampdetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.smec.appmanager.GlideApp;
import com.smec.appmanager.api.SmecAmpConstant;
import com.smec.appmanager.beans.DownloadInfo;
import com.smec.appmanager.manager.SmecPreference.PreferencesHelper;
import com.smec.appmanager.manager.SmecRetrofit.SmecAutoUpdate;
import com.smec.appmanager.params.AmpRoleName;
import com.smec.appmanager.R;
import com.smec.appmanager.SmecApplication;
import com.smec.appmanager.base.SmecBaseActivity;
import com.smec.appmanager.beans.AmpLabel;
import com.smec.appmanager.databinding.ActivityDetailsBinding;
import com.smec.appmanager.dto.AllApkDto;
import com.smec.appmanager.manager.SmecSession.SmecSession;
import com.smec.appmanager.module.main.adapter.SmecAmplabelAdapter;
import com.smec.appmanager.module.mine.SmecMineContract;
import com.smec.appmanager.utils.CommonUtils;
import com.smec.appmanager.utils.DownloadManager;
import com.smec.appmanager.utils.ToastyUtils;
import com.smec.appmanager.widget.ProgressButton;
import com.smec.appmanager.widget.TopBarLayout;

import java.io.File;
import java.util.ArrayList;

import static com.smec.appmanager.api.SmecAmpConstant._appOperator;
import static com.smec.appmanager.api.SmecAmpConstant.checkAppStatus;

/**
 * Created by xupeizuo on 2018/7/4.
 */

public class SmecAmpDetailsActivity extends SmecBaseActivity<SmecAmpDetailsPresenter>{

    private ActivityDetailsBinding activityDetailsBinding;
    private SmecAmpDetailsViewModel smecAmpDetailsViewModel;
    private AllApkDto allApkDto;
    private SmecAmplabelAdapter smecAmplabelAdapter;
    private ProgressDialog progressDialog;

    @Override
    public SmecAmpDetailsPresenter getPresenter() {
        return new SmecAmpDetailsPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailsBinding=DataBindingUtil.setContentView(this, R.layout.activity_details);
        smecAmpDetailsViewModel=new SmecAmpDetailsViewModel();
        _getDataFromIntent();
        _initView();
        activityDetailsBinding.setViewModel(smecAmpDetailsViewModel);
    }

    /**
     * 从intent中获取数据
     */
    private void _getDataFromIntent(){
        allApkDto=(AllApkDto)getIntent().getSerializableExtra("AllApkDto");
        smecAmpDetailsViewModel.setAllApkDto(allApkDto);
    }

    public AllApkDto getApkMsg(){
        return allApkDto;
    }

    /**
     * 初始化
     */
    private void _initView(){

        /*if(SmecSession.getCurrentBMap()!=null){
            activityDetailsBinding.smecIcon.setImageBitmap(
                    CommonUtils.getBitmap(SmecSession.getCurrentBMap()));
        }*/
        if (!PreferencesHelper.getInstance().getNoImageState()) {
            GlideApp.with(this).load(allApkDto.getAmpApk().getIcon()).into(activityDetailsBinding.smecIcon);
        }
        activityDetailsBinding.topBarLayout.setTopBarListener(new TopBarLayout.TopBarListener() {
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
        if(allApkDto.getAmpApk().getDescription()!=null){
            activityDetailsBinding.smecContent.setText(allApkDto.getAmpApk().getDescription());
        }
        /*activityDetailsBinding.smecOperator.setOnProgressButtonClickListener(new ProgressButton.OnProgressButtonClickListener() {
            @Override
            public void onClickListener() {
                _appOperator(SmecAmpDetailsActivity.this,allApkDto,activityDetailsBinding.smecOperator,null);
            }
        });*/
        activityDetailsBinding.smecOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SmecAmpConstant.isUsePause){
                    _appOperator(SmecAmpDetailsActivity.this,allApkDto,(ProgressButton)v,null);
                }else {
                    DownloadInfo info = DownloadManager.getInstance().getDownloadInfo(allApkDto.getAmpApk());

                    if ((info == null) || (info.size == 0)){
                        //Log.i("mohongwu","OnClick : "+allApkDto.getAmpApk().getPackageName());
                        _appOperator(SmecAmpDetailsActivity.this,allApkDto,(ProgressButton)v,null);
                    }else {
                        //Log.i("mohongwu","OnClick : "+info.currentState +"，path: "+info.path);
                        if (DownloadManager.STATE_DOWNLOAD == info.currentState){
                            DownloadManager.getInstance().pause(allApkDto.getAmpApk());
                        }else if(info.currentState == DownloadManager.STATE_PAUSE){
                            //DownloadManager.getInstance().download(allApkDto.getAmpApk());
                            _appOperator(SmecAmpDetailsActivity.this,allApkDto,(ProgressButton)v,null);
                        }else if (info.currentState == DownloadManager.STATE_INSTALL){
                            //已下载到本地的apk，应检查存在性
                            File file = new File(info.path);
                            if (!file.exists()){
                                info.size = 0;
                                ToastyUtils.showNormalToast(SmecAmpDetailsActivity.this,"apk文件已被删除，请重新下载！");
                                DownloadManager.getInstance().deleteDownloadInfo(info.packageName);
                                activityDetailsBinding.smecOperator.setProgress(0);
                                //activityDetailsBinding.smecOperator.setText("下载");
                                return;
                            }
                            SmecAutoUpdate.installApk(SmecAmpDetailsActivity.this,file);
                        }
                    }

                }

            }

        });
        //mohongwu
        if (SmecAmpConstant.isUsePause){
            DownloadManager.getInstance().registerObserver(allApkDto.getAmpApk(),activityDetailsBinding.smecOperator);
        }
        //mohongwu
        activityDetailsBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                _getRoleName();
            }
        },200);
    }

    private void _getRoleName(){
        final ArrayList<AmpLabel> all=new ArrayList<>();
        all.addAll(SmecSession.getUserlabelDto().getList());
        if(progressDialog==null){
            progressDialog=new ProgressDialog(SmecAmpDetailsActivity.this);
            progressDialog.setMessage("正在获取详细信息...");
        }
        progressDialog.show();
        mPresenter.getNamgeByCode(new SmecMineContract.Codelistener() {
            @Override
            public void fail() {
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                smecAmplabelAdapter=new SmecAmplabelAdapter(SmecAmpDetailsActivity.this,allApkDto.getAmpLabels(),all);
                activityDetailsBinding.smecLabel.setAdapter(smecAmplabelAdapter);
            }

            @Override
            public void success(ArrayList<AmpRoleName> list) {
                ArrayList<AmpLabel> ampLabels=new ArrayList<AmpLabel>();
                if(CommonUtils.notEmpty(list)){
                    for(AmpRoleName ampRoleName:list){
                        AmpLabel ampLabel=new AmpLabel(ampRoleName.getName());
                        ampLabels.add(ampLabel);
                    }
                }
                all.addAll(ampLabels);
                smecAmplabelAdapter=new SmecAmplabelAdapter(SmecAmpDetailsActivity.this,allApkDto.getAmpLabels(),all);
                activityDetailsBinding.smecLabel.setAdapter(smecAmplabelAdapter);
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        _initViewStatus();
    }

    /**
     * 初始化应用状态
     */
    private void _initViewStatus(){
        SmecApplication.initScanInstalledApp();
        String s=checkAppStatus(SmecAmpDetailsActivity.this,allApkDto,activityDetailsBinding.smecOperator);
        //mohongwu
        if (SmecAmpConstant.isUsePause){
            //DownloadManager.getInstance().registerObserver(allApkDto.getAmpApk(),activityDetailsBinding.smecOperator);
            DownloadInfo info = DownloadManager.getInstance().getDownloadInfo(allApkDto.getAmpApk());

            if (info != null) {
                Log.i("mohongwu", "DownloadInfo : " + info.currentPos);
                //判断本地下载的apk版本和服务器比较是否为最新
                if (!info.getVersionName().equals(allApkDto.getAmpApk().getVersionName())) {
                    DownloadManager.getInstance().deleteDownloadInfo(allApkDto.getAmpApk().getVersionName());
                } else {
                    if (info.currentState == DownloadManager.STATE_DOWNLOAD) {
                        activityDetailsBinding.smecOperator.setText("下载中");
                    } else if (info.currentState == DownloadManager.STATE_PAUSE) {
                        if (info.currentPos > 0) {
                            activityDetailsBinding.smecOperator.setText("暂停");
                            activityDetailsBinding.smecOperator.setMax(info.size);
                            activityDetailsBinding.smecOperator.setProgress(info.currentPos);
                        } else {
                            activityDetailsBinding.smecOperator.setText("等待");
                        }
                    } else if (info.currentState == DownloadManager.STATE_INSTALL) {
                        activityDetailsBinding.smecOperator.setText("安装");
                        activityDetailsBinding.smecOperator.setMax(info.size);
                        activityDetailsBinding.smecOperator.setProgress(info.currentPos);
                        //activityDetailsBinding.smecOperator.setBackgroundColor(getResources().getColor(R.color.top_bar));
                        info.currentState = DownloadManager.STATE_INSTALL;
                    }
                }
            }
        }
        //mohongwu
        //activityDetailsBinding.invalidateAll();
    }
}
