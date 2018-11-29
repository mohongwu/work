package com.smec.appmanager.module.login;

/**
 * Created by xupeizuo on 2018/5/15.
 */

public class SmecLoginViewModel {

    public String username;
    public String password;
    public boolean rememberFlag=false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberFlag() {
        return rememberFlag;
    }

    public void setRememberFlag(boolean rememberFlag) {
        this.rememberFlag = rememberFlag;
    }
}
