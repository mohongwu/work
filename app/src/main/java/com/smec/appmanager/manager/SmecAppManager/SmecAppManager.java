package com.smec.appmanager.manager.SmecAppManager;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * Created by xupeizuo on 2017/7/25.
 */

public class SmecAppManager {

    private static Stack<Activity> activityStack;
    private static SmecAppManager smecAppManager;

    private static byte[] syncObj = new byte[0];

    public static SmecAppManager getInstance(){
        if(smecAppManager == null){
            synchronized (syncObj){
                if(smecAppManager == null){
                    smecAppManager=new SmecAppManager();
                    activityStack=new Stack<>();
                }
            }
        }
        return smecAppManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.push(activity);
    }

    public void pullActivity(Activity activity){
        try {
            activityStack.remove(activity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {

        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        activity.finish();
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {

        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());

        } catch (Exception e) {
        }
    }

    /**
     * 结束指定的Activity
     */
    public void getActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 得到指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }
}
