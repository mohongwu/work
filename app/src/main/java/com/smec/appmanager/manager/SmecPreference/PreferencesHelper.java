package com.smec.appmanager.manager.SmecPreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.smec.appmanager.SmecApplication;

/**
 * Create by mohongwu on 2018/11/12
 */
public class PreferencesHelper {
    private static final boolean DEFAULT_NIGHT_MODE = false;
    private static final boolean DEFAULT_NO_IMAGE = false;
    private static final boolean DEFAULT_AUTO_SAVE = true;

    private static final boolean DEFAULT_LIKE_POINT = false;
    private static final boolean DEFAULT_VERSION_POINT = false;
    private static final boolean DEFAULT_MANAGER_POINT = false;

    public static final String SP_NO_IMAGE = "no_image";

    //private static final int DEFAULT_CURRENT_ITEM = Constants.TYPE_ZHIHU;

    private static final String SHAREDPREFERENCES_NAME = "my_sp";

    private final SharedPreferences mSPrefs;

    private static PreferencesHelper sInstance = new PreferencesHelper();


    public static PreferencesHelper getInstance() {
        return sInstance;
    }

    public PreferencesHelper() {
        mSPrefs = SmecApplication.getContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setNoImageState(boolean state) {
        mSPrefs.edit().putBoolean(SP_NO_IMAGE, state).apply();
    }

    public boolean getNoImageState() {
        return mSPrefs.getBoolean(SP_NO_IMAGE, DEFAULT_NO_IMAGE);
    }

    public void cleanPreference(){
        mSPrefs.edit().clear().commit();
    }
}
