package com.smec.appmanager.module.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.smec.appmanager.R;
import com.smec.appmanager.manager.SmecDialog.SmecDialog;
import com.smec.appmanager.manager.SmecPreference.PreferencesHelper;
import com.smec.appmanager.manager.SmecRealm.RealmManager;
import com.smec.appmanager.manager.SmecSession.SmecSession;
import com.smec.appmanager.manager.SmecSession.SmecUser;
import com.smec.appmanager.module.login.SmecLoginActivity;
import com.smec.appmanager.module.mine.ModifyPassowrd.ModifyPasswordActivty;
import com.smec.appmanager.module.mine.SmecAboutActivity.SmecAboutActivity;
import com.smec.appmanager.module.mine.Userlabels.UserlabelsActivity;

/**
 * Created by xupeizuo on 2018/6/26.
 */

public class SmecMineViewModel {
    private SmecSession smecSession;
    private Context context;

    public SmecMineViewModel(Context context) {
        this.context = context;
    }

    public SmecSession getSmecSession() {
        return smecSession;
    }

    public void setSmecSession(SmecSession smecSession) {
        this.smecSession = smecSession;
    }

    public void mineOperator(View view){
        switch (view.getId()){
            case R.id.smec_user_labels:
                context.startActivity(new Intent(context, UserlabelsActivity.class));
                break;
            case R.id.smec_update_password:
                context.startActivity(new Intent(context, ModifyPasswordActivty.class));
                break;
            case R.id.smec_about:
                context.startActivity(new Intent(context, SmecAboutActivity.class));
            case R.id.smec_set:
                context.startActivity(new Intent(context, SmecSettingActivity.class));
                break;
            case R.id.smec_logout:
                logout(view);
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logout(final View view){
        final NormalDialog normalDialog= SmecDialog.getInstance(context,"退出登录吗?");
        normalDialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        normalDialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        SmecUser smecUser=new SmecUser();
                        smecUser.setLinkpage(true);
                        SmecSession.setSmecUser(smecUser);
                        RealmManager.getInstance().deleteRealmObject(SmecUser.class);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                normalDialog.dismiss();
                                //context.startActivity(new Intent(context, SmecLoginActivity.class));
                                PreferencesHelper.getInstance().cleanPreference();
                                ((Activity)context).finish();
                            }
                        },300);
                    }
                });
        normalDialog.show();
    }
}
