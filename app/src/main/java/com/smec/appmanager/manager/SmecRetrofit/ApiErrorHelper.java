package com.smec.appmanager.manager.SmecRetrofit;

import android.widget.Toast;
import com.smec.appmanager.SmecApplication;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by xupeizuo on 2017/4/26.
 * 遵循单一职责原则SRP
 */

public final class ApiErrorHelper {


    /**
     * 分解错误信息
     * @param e
     */
    public static void handleError(Throwable e) {
        String msg="网络连接失败，数据异常";
        if (e instanceof HttpException) {
            msg="服务暂不可用";
        } else if (e instanceof IOException) {
            msg="连接失败";
        } else if (e instanceof ApiException) {
            ApiException apiException=(ApiException) e;
            msg=apiException.getMessage();
        }
        showMsg(msg);
    }

    /**
     * 处理错误信息
     * @param msg
     */
    public static void showMsg(String msg){
        Toast.makeText(SmecApplication.getContext(),msg,Toast.LENGTH_SHORT).show();
    }

}
