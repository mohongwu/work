package com.smec.appmanager.dto;

import com.smec.appmanager.beans.AmpLabel;

import java.util.ArrayList;

/**
 * Created by xupeizuo on 2018/6/28.
 */

public class UserlabelDto {

    private boolean admin =false;
    private ArrayList<AmpLabel> list;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public ArrayList<AmpLabel> getList() {
        return list;
    }

    public void setList(ArrayList<AmpLabel> list) {
        this.list = list;
    }

}

