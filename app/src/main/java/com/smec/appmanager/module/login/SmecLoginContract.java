package com.smec.appmanager.module.login;

/**
 * Created by xupeizuo on 2018/5/15.
 */

public interface SmecLoginContract {

    /**
     * 用户登录
     */
    void login(String username, String password,loginlistener loginlistener);

    /**
     * 版本检查
     */
    void versionCheck();

    interface loginlistener{
        void loginSuccess();
        void loginFailure();
    }

    interface versionlistener{

    }

}
