package com.smec.appmanager.module.login;

import android.content.Context;
import com.smec.appmanager.params.LoginParams;
import com.smec.appmanager.base.SmecBasePresenter;
import com.smec.appmanager.beans.AmpUserMsg;
import com.smec.appmanager.manager.SmecRetrofit.BaseSubscriber;
import com.smec.appmanager.manager.SmecRetrofit.HttpResult;
import com.smec.appmanager.manager.SmecSession.SmecSession;
import com.smec.appmanager.manager.SmecSession.SmecUser;

import rx.Observable;

/**
 * Created by xupeizuo on 2018/5/15.
 */

public class SmecLoginPresenter extends SmecBasePresenter implements SmecLoginContract{


    public SmecLoginPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void login(final String username, final String password, final loginlistener loginlistener) {
        LoginParams loginParams=new LoginParams(username,password);
        Observable<HttpResult<AmpUserMsg>> observable=smecAppManagerService.userlogin(loginParams);
        schedulerThread(observable).subscribe(new BaseSubscriber<HttpResult<AmpUserMsg>>() {

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                loginlistener.loginFailure();
            }

            @Override
            public void onNext(HttpResult<AmpUserMsg> stringHttpResult) {
                SmecUser smecUser=new SmecUser();
                smecUser.setUserName(username);
                smecUser.setPassWord(password);
                if(stringHttpResult.getData()!=null){
                    smecUser.setToken(stringHttpResult.getData().getToken());
                    if(stringHttpResult.getData().getUserRole()!=null){
                        smecUser.setUserRole(stringHttpResult.getData().getUserRole());
                    }
                }
                _dealWithData(smecUser);
                loginlistener.loginSuccess();
            }
        });
    }

    @Override
    public void versionCheck() {

    }


    private void _dealWithData(SmecUser smecUser){
        realmManager.deleteRealmObject(SmecUser.class);
        smecUser.setRememberAccount(SmecSession.isRememberAccount());
        SmecSession.setToken(smecUser.getToken());
        SmecSession.setSmecUser(smecUser);
        realmManager.insert(SmecSession.getSmecUser());
    }
}
