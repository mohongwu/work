package com.smec.appmanager.beans;

/**
 * Created by xupeizuo on 2018/7/11.
 */

public class AmpApkDownload extends AmpBase{

    private int id;
    private int apkId;
    private String downloadBy;
    private String downloadByName;
    private String downloadTime;

    public AmpApkDownload(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApkId() {
        return apkId;
    }

    public void setApkId(int apkId) {
        this.apkId = apkId;
    }

    public String getDownloadBy() {
        return downloadBy;
    }

    public void setDownloadBy(String downloadBy) {
        this.downloadBy = downloadBy;
    }

    public String getDownloadByName() {
        return downloadByName;
    }

    public void setDownloadByName(String downloadByName) {
        this.downloadByName = downloadByName;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }
}
