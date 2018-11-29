package com.smec.appmanager.module.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.smec.appmanager.MainActivity;
import com.smec.appmanager.R;
import com.smec.appmanager.base.SmecBaseActivity;
import com.smec.appmanager.databinding.ActivityLoginBinding;
import com.smec.appmanager.manager.SmecSession.SmecSession;
import com.smec.appmanager.utils.CommonUtils;
import com.smec.appmanager.utils.ToastyUtils;

/**
 * Created by xupeizuo on 2018/5/15.
 */


public class SmecLoginActivity extends SmecBaseActivity<SmecLoginPresenter> implements SmecLoginContract{

    private SmecLoginViewModel smecLoginViewMode;
    private ActivityLoginBinding activityLoginBinding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding=DataBindingUtil.setContentView(this, R.layout.activity_login);
        smecLoginViewMode=new SmecLoginViewModel();
        activityLoginBinding.setViewModel(smecLoginViewMode);
        _initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _getDataFromSession();
        autologin();
    }

    /**
     * 初始化
     */
    private void _initView(){
        CommonUtils.hideImmManager(activityLoginBinding.getRoot());
    }

    /**
     * 从session中获取用户信息
     */
    private void _getDataFromSession(){
        smecLoginViewMode.setRememberFlag(SmecSession.getSmecUser().isRememberAccount());
        if(smecLoginViewMode.rememberFlag){
            smecLoginViewMode.setUsername(SmecSession.getSmecUser().getUserName());
            smecLoginViewMode.setPassword(SmecSession.getSmecUser().getPassWord());
        }else {
            smecLoginViewMode.setUsername(null);
            smecLoginViewMode.setPassword(null);
        }
        activityLoginBinding.invalidateAll();
    }

    /**
     * 自动登录
     */
    private void autologin(){
        if(smecLoginViewMode.rememberFlag && SmecSession.getSmecUser().getToken()!=null
                && !SmecSession.getSmecUser().getToken().equals("")){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public SmecLoginPresenter getPresenter() {
        return new SmecLoginPresenter(this);
    }

    @Override
    public void login(String username, String password,loginlistener loginlistener) {
        mPresenter.login(username,password,loginlistener);
    }

    @Override
    public void versionCheck() {

    }

    /**
     * 登录或记住密码
     * @param view
     */
    public void operator(View view){

        switch (view.getId()){
            case R.id.smec_login:
                if (TextUtils.isEmpty(smecLoginViewMode.getUsername()) && TextUtils.isEmpty(smecLoginViewMode.getPassword())) {
                    ToastyUtils.showNormalToast(this,"用户名和密码不能为空");
                    return;
                }
                if(progressDialog==null){
                    progressDialog=new ProgressDialog(SmecLoginActivity.this);
                    progressDialog.setMessage("正在登录");
                }
                progressDialog.show();

                login(smecLoginViewMode.getUsername(), smecLoginViewMode.getPassword(), new loginlistener() {
                    @Override
                    public void loginSuccess() {
                        if(progressDialog!=null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        startActivity(new Intent(SmecLoginActivity.this, MainActivity.class));
                    }

                    @Override
                    public void loginFailure() {
                        if(progressDialog!=null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.login_remember:
                if(smecLoginViewMode.rememberFlag){
                    smecLoginViewMode.setRememberFlag(false);
                    SmecSession.setRememberAccount(false);
                }else {
                    smecLoginViewMode.setRememberFlag(true);
                    SmecSession.setRememberAccount(true);
                }
                activityLoginBinding.invalidateAll();
                break;
        }
    }

}
