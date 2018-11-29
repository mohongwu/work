package com.smec.appmanager.utils;

/**
 * Created by xupeizuo on 2017/4/9.
 */

public final class KmpUtil {


    /**
     * KMP 匹配算法
     * @param T  主串
     * @param W  模式串
     * @return
     */
    public static boolean Kmp(String T,String W){
        if(W.equals("")){
            return true;
        }
        char [] tt=T.toCharArray();
        char [] ww=W.toCharArray();
        return KMP_Index(tt,ww);
    }

    /**
     * 获得字符串的next函数值
     *
     * @param t
     *            字符串
     * @return next函数值
     */
    public static int[] next(char[] t) {
        int[] next = new int[t.length];
        next[0] = -1;
        int i = 0;
        int j = -1;
        while (i < t.length - 1) {
            if (j == -1 || t[i] == t[j]) {
                i++;
                j++;
                if (t[i] != t[j]) {
                    next[i] = j;
                } else {
                    next[i] = next[j];
                }
            } else {
                j = next[j];
            }
        }
        return next;
    }

    /**
     * KMP匹配字符串
     *
     * @param tt
     *            主串
     * @param ww
     *            模式串
     * @return 若匹配成功，返回下标，否则返回-1
     */
    public static boolean KMP_Index(char[] tt, char[] ww) {
        int[] next = next(ww);
        int i = 0;
        int j = 0;
        while (i <= tt.length - 1 && j <= ww.length - 1) {
            if (j == -1 || tt[i] == ww[j]) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }
        if (j < ww.length) {
            return false;
        } else
            return true; // 返回模式串在主串中的头下标 i - t.length
    }
}
