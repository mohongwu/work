package com.smec.appmanager.api;

import com.smec.appmanager.BuildConfig;

/**
 * Created by xupeizuo on 2018/5/15.
 */

public interface SmecAppManagerApi {
    String BASE_URL= BuildConfig.SMEC_DOMAIN;

    enum AppStatus{

        INSTALL("安装"),OPEN("打开"),UPDATE("更新"),DOWNLOAD("下载"),DOWNLOADING("下载中"),PASUE("暂停"),INSRALLING("安装中"),AGAIN("重试"),PASSWORD("密码");

        public String info;

        AppStatus(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }

    interface api{

        String BASE = "/smec-app-platform";

        /**
         * 用户登录
         */
        String LOGIN=BASE + "/amp/login/auth";

        /**
         * 获取人员的分组信息
         */
        String GET_USER_LABEL=BASE + "/amp/label/getUserlabel";

        /**
         * 获取所有apk信息
         */
        String GET_ALL_APKS=BASE + "/amp/apk/allDetails";

        /**
         * 新增下载记录
         */
        String ADD_DOWNLOAD_RECORD=BASE + "/amp/download/add";

        /**
         * 修改密码
         */
        String CHANGE_PASSWORD=BASE + "/amp/employee/changePassword";

        /**
         * 获取用户角色名
         */
        String GETNAME=BASE + "/amp/manager/roleCode";
    }
}
