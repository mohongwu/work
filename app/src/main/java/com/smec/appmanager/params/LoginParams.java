package com.smec.appmanager.params;

/**
 * Created by xupeizuo on 2018/6/28.
 */

public class LoginParams {

    private String username;
    private String password;
    private String method;

    public LoginParams(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
