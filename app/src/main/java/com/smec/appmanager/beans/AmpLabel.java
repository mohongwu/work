package com.smec.appmanager.beans;

/**
 * Created by xupeizuo on 2018/6/28.
 */

public class AmpLabel extends AmpBase {

    private int id;
    private String name;
    private String ishave;
    private String roleCode;

    public AmpLabel() {
    }

    public AmpLabel(String name) {
        this.name = name;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getIshave() {
        return ishave;
    }

    public void setIshave(String ishave) {
        this.ishave = ishave;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
