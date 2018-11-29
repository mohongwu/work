package com.smec.appmanager.dto;

import com.smec.appmanager.beans.AmpApk;
import com.smec.appmanager.beans.AmpLabel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xupeizuo on 2018/6/28.
 */

public class AllApkDto implements Serializable{

    private AmpApk ampApk;
    private ArrayList<AmpLabel> ampLabels;

    public AmpApk getAmpApk() {
        return ampApk;
    }

    public void setAmpApk(AmpApk ampApk) {
        this.ampApk = ampApk;
    }

    public ArrayList<AmpLabel> getAmpLabels() {
        return ampLabels;
    }

    public void setAmpLabels(ArrayList<AmpLabel> ampLabels) {
        this.ampLabels = ampLabels;
    }

}
