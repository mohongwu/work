package com.smec.appmanager.params;

/**
 * Created by xupeizuo on 2018/7/9.
 */

public class ModifyPassowordBean {

    private String username;
    private String old;
    private String password;
    private String makeSure;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMakeSure() {
        return makeSure;
    }

    public void setMakeSure(String makeSure) {
        this.makeSure = makeSure;
    }
}
