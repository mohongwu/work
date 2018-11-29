package com.smec.appmanager.manager.SmecSession;

import com.smec.appmanager.dto.UserlabelDto;
import com.smec.appmanager.manager.SmecRealm.RealmManager;

/**
 * Created by xupeizuo on 2017/7/25.
 */

public final class SmecSession {

    private final String TAG=this.getClass().getSimpleName();
    public static byte[] synObj=new byte[0];

    private static SmecSession smecSession;
    private static SmecUser smecUser;
    private static String token;
    private static boolean rememberAccount;
    private static UserlabelDto userlabelDto;
    public static byte[] currentBMap;

    public static byte[] getCurrentBMap() {
        return currentBMap;
    }

    public static void setCurrentBMap(byte[] currentBMap) {
        SmecSession.currentBMap = currentBMap;
    }

    public static SmecSession initSmecSession(){
        if(smecSession == null){
            synchronized (synObj){
                if(smecSession == null){
                    smecSession.smecUser= (SmecUser) RealmManager.getInstance().findFirst(SmecUser.class);
                    if(smecUser == null){
                        smecUser=new SmecUser();
                        smecUser.setLinkpage(true);
                        rememberAccount=false;
                    }else {
                        token=smecUser.getToken();
                        rememberAccount=smecUser.isRememberAccount();
                    }
                }
            }
        }
        return smecSession;
    }

    public static SmecUser getSmecUser(){
        return smecUser;
    }

    public static void setSmecUser(SmecUser smecUser) {
        SmecSession.smecUser = smecUser;
    }

    public static boolean isRememberAccount() {
        return rememberAccount;
    }

    public static void setRememberAccount(boolean rememberAccount) {
        SmecSession.rememberAccount = rememberAccount;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        SmecSession.token = token;
    }

    public static UserlabelDto getUserlabelDto() {
        return userlabelDto ==null ? new UserlabelDto() :userlabelDto;
    }

    public static void setUserlabelDto(UserlabelDto userlabelDto) {
        SmecSession.userlabelDto = userlabelDto;
    }
}
