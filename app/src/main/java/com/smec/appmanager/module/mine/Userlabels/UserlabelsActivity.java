package com.smec.appmanager.module.mine.Userlabels;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smec.appmanager.params.AmpRoleName;
import com.smec.appmanager.R;
import com.smec.appmanager.base.SmecBaseActivity;
import com.smec.appmanager.beans.AmpLabel;
import com.smec.appmanager.databinding.ActivityUserLabelsBinding;
import com.smec.appmanager.manager.SmecSession.SmecSession;
import com.smec.appmanager.module.main.adapter.SmecAmplabelAdapter;
import com.smec.appmanager.module.mine.SmecMineContract;
import com.smec.appmanager.module.mine.SmecMinePresenter;
import com.smec.appmanager.utils.CommonUtils;
import com.smec.appmanager.widget.TopBarLayout;

import java.util.ArrayList;

/**
 * Created by xupeizuo on 2018/7/5.
 */

public class UserlabelsActivity extends SmecBaseActivity<SmecMinePresenter>{

    private SmecAmplabelAdapter smecAmplabelAdapter;
    private ActivityUserLabelsBinding activityUserLabelsBinding;
    private ProgressDialog progressDialog;
    @Override
    public SmecMinePresenter getPresenter() {
        return new SmecMinePresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserLabelsBinding=DataBindingUtil.setContentView(this, R.layout.activity_user_labels);
        _initView();
    }

    private void _initView(){
        activityUserLabelsBinding.topBarLayout.setTopBarListener(new TopBarLayout.TopBarListener() {
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
        activityUserLabelsBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                _getRoleName();
            }
        },100);
    }

    private void _getRoleName(){
        final ArrayList<AmpLabel> all=new ArrayList<>();
        if (!CommonUtils.notEmpty(SmecSession.getUserlabelDto().getList())){
            return;
        }
        all.addAll(SmecSession.getUserlabelDto().getList());
        if(progressDialog==null){
            progressDialog=new ProgressDialog(UserlabelsActivity.this);
            progressDialog.setMessage("正在获取用户角色");
        }
        progressDialog.show();
        mPresenter.getNamgeByCode(new SmecMineContract.Codelistener() {
            @Override
            public void fail() {
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                smecAmplabelAdapter=new SmecAmplabelAdapter(UserlabelsActivity.this, all,true);
                activityUserLabelsBinding.smecLabel.setAdapter(smecAmplabelAdapter);
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
                smecAmplabelAdapter=new SmecAmplabelAdapter(UserlabelsActivity.this, all,true);
                activityUserLabelsBinding.smecLabel.setAdapter(smecAmplabelAdapter);
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }
}
