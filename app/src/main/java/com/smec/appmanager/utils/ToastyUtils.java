package com.smec.appmanager.utils;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by xupeizuo on 2017/8/1.
 * 避免同样的信息多次触发重复弹出的问题 2017/8/02/
 * @author xupeizuo
 */

public final class ToastyUtils {

    public static Toast mToast=null;
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showNormalToast(Context context,String s){
        if(mToast == null){
            oldMsg=s;
            mToast=Toast.makeText(context,s,Toast.LENGTH_SHORT);
            mToast.show();
            oneTime = System.currentTimeMillis();
        }else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    mToast.show();
                }
            } else {
                oldMsg = s;
                mToast=Toast.makeText(context,s,Toast.LENGTH_SHORT);
                mToast.show();
            }
            oneTime = twoTime;
        }
    }
}
