package com.smec.appmanager.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.smec.appmanager.SmecApplication;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Created by xupeizuo on 2017/7/3.
 */

public class CommonUtils {


    public static boolean notEmpty(List<?> T){
        return T!=null && !T.isEmpty();
    }

    public static byte[] getBytes(Bitmap bitmap){
        //实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图
        return baos.toByteArray();//创建分配字节数组
    }

    public static Bitmap getBitmap(byte[] data){
        return BitmapFactory.decodeByteArray(data, 0, data.length);//从字节数组解码位图
    }

    /**
     * 获取UUID
     * @return
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        return str+","+temp;
    }

    public static boolean isForeground(Context context)
    {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {

                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return false;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * 隐藏键盘
     * @param view
     */
    public static void hideImmManager(View view) {
        InputMethodManager imm = (InputMethodManager) SmecApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 改变视角距离, 贴近屏幕
     */
    public static void setCameraDistance(DisplayMetrics density, View view) {
        int distance = 10000;
        float scale = density.density * distance;
        view.setCameraDistance(scale);
    }
}
