package com.smec.appmanager.beans;

/**
 * Created by xupeizuo on 2018/7/12.
 */

public class AmpUserMsg {

    private String token;
    private String userId;
    private String userName;
    private String userRole;
    private String isStationOnline;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getIsStationOnline() {
        return isStationOnline;
    }

    public void setIsStationOnline(String isStationOnline) {
        this.isStationOnline = isStationOnline;
    }
}
