package com.smec.appmanager.module.mine.ModifyPassowrd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.smec.appmanager.manager.SmecRealm.RealmManager;
import com.smec.appmanager.manager.SmecSession.SmecSession;
import com.smec.appmanager.manager.SmecSession.SmecUser;
import com.smec.appmanager.module.login.SmecLoginActivity;
import com.smec.appmanager.params.ModifyPassowordBean;
import com.smec.appmanager.R;
import com.smec.appmanager.base.SmecBaseActivity;
import com.smec.appmanager.databinding.ActivtyModifyPasswordBinding;
import com.smec.appmanager.module.mine.SmecMineContract;
import com.smec.appmanager.module.mine.SmecMinePresenter;
import com.smec.appmanager.utils.CommonUtils;
import com.smec.appmanager.utils.ToastyUtils;
import com.smec.appmanager.widget.TopBarLayout;

/**
 * Created by xupeizuo on 2018/7/5.
 */

public class ModifyPasswordActivty extends SmecBaseActivity<SmecMinePresenter> implements SmecMineContract {

    private ActivtyModifyPasswordBinding activtyModifyPasswordBinding;
    @Override
    public SmecMinePresenter getPresenter() {
        return new SmecMinePresenter(this);
    }
    private ModifyPassowordBean modifyPassowordBean;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activtyModifyPasswordBinding=DataBindingUtil.setContentView(this, R.layout.activty_modify_password);
        modifyPassowordBean=new ModifyPassowordBean();
        activtyModifyPasswordBinding.setViewModel(modifyPassowordBean);
        _initView();
    }

    private void _initView(){
        activtyModifyPasswordBinding.topBarLayout.setTopBarListener(new TopBarLayout.TopBarListener() {
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
        activtyModifyPasswordBinding.smecMakeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modifyPassowordBean.getOld()==null || modifyPassowordBean.getOld().trim().equals("")){
                    ToastyUtils.showNormalToast(ModifyPasswordActivty.this,"请输入原始密码");
                    return;
                }
                if(modifyPassowordBean.getPassword()==null || modifyPassowordBean.getPassword().trim().equals("")){
                    ToastyUtils.showNormalToast(ModifyPasswordActivty.this,"请输入新密码");
                    return;
                }
                if(modifyPassowordBean.getMakeSure()==null ||
                        modifyPassowordBean.getMakeSure().trim().equals("")||
                        !modifyPassowordBean.getMakeSure().equals(modifyPassowordBean.getPassword().trim())){
                    ToastyUtils.showNormalToast(ModifyPasswordActivty.this,"两次输入不一致");
                    return;
                }
                CommonUtils.hideImmManager(activtyModifyPasswordBinding.getRoot());
                if(progressDialog==null){
                    progressDialog=new ProgressDialog(ModifyPasswordActivty.this);
                    progressDialog.setMessage("正在修改密码...");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
                progressDialog.show();
                changePassowrdlistener(modifyPassowordBean, new Plistener() {
                    @Override
                    public void success() {
                        ToastyUtils.showNormalToast(ModifyPasswordActivty.this,"修改密码成功");
                        if(progressDialog!=null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        SmecUser smecUser=new SmecUser();
                        smecUser.setLinkpage(true);
                        SmecSession.setSmecUser(smecUser);
                        RealmManager.getInstance().deleteRealmObject(SmecUser.class);
                        Intent intent = new Intent(ModifyPasswordActivty.this, SmecLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                    @Override
                    public void fail() {
                        if(progressDialog!=null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void changePassowrdlistener(ModifyPassowordBean modifyPassowordBean,Plistener plistener) {
        mPresenter.changePassowrdlistener(modifyPassowordBean,plistener);
    }
}
