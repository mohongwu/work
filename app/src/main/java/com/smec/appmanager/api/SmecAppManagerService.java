package com.smec.appmanager.api;

import com.smec.appmanager.params.AmpRoleDto;
import com.smec.appmanager.params.AmpRoleName;
import com.smec.appmanager.params.LoginParams;
import com.smec.appmanager.params.ModifyPassowordBean;
import com.smec.appmanager.beans.AmpApkDownload;
import com.smec.appmanager.beans.AmpUserMsg;
import com.smec.appmanager.dto.AllApkDto;
import com.smec.appmanager.dto.UserlabelDto;
import com.smec.appmanager.manager.SmecRetrofit.HttpResult;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

import static com.smec.appmanager.api.SmecAppManagerApi.api.ADD_DOWNLOAD_RECORD;
import static com.smec.appmanager.api.SmecAppManagerApi.api.CHANGE_PASSWORD;
import static com.smec.appmanager.api.SmecAppManagerApi.api.GETNAME;
import static com.smec.appmanager.api.SmecAppManagerApi.api.GET_ALL_APKS;
import static com.smec.appmanager.api.SmecAppManagerApi.api.GET_USER_LABEL;
import static com.smec.appmanager.api.SmecAppManagerApi.api.LOGIN;

/**
 * Created by xupeizuo on 2018/5/15.
 */

public interface SmecAppManagerService {


    /**
     * 用户登录
     * @param loginParams
     * @return
     */
    @POST(LOGIN)
    Observable<HttpResult<AmpUserMsg>> userlogin(@Body LoginParams loginParams);


    /**
     * 获取人员分组信息
     * @return
     */
    @GET(GET_USER_LABEL)
    Observable<HttpResult<UserlabelDto>> getUserlabel();


    /**
     * 获取所有apk信息
     * @return
     */
    @GET(GET_ALL_APKS)
    Observable<HttpResult<ArrayList<AllApkDto>>> getAllApk();


    /**
     * 新增下载记录不影响流程
     * @param ampApkDownload
     * @return
     */
    @POST(ADD_DOWNLOAD_RECORD)
    Observable<HttpResult> newDownloadRecord(@Body AmpApkDownload ampApkDownload);

    /**
     * 修改密码
     * @param modifyPassowordBean
     * @return
     */
    @POST(CHANGE_PASSWORD)
    Observable<HttpResult> changePassword(@Body ModifyPassowordBean modifyPassowordBean);

    /**
     * 获取角色名
     * @param ampRoleDto
     * @return
     */
    @POST(GETNAME)
    Observable<HttpResult<ArrayList<AmpRoleName>>> getRoleNameByCode(@Body AmpRoleDto ampRoleDto);
}
