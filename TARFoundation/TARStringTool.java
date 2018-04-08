package com.tar.ronghuibaoforandroid.TARUtilsForAndroid.TARFoundation;

import com.tar.ronghuibaoforandroid.TARUtilsForAndroid.TARPhoneNumberVerification;

/**
 * Created by TAR on 2018/1/15.
 */

/**
 * 字符串工具类
 */
public class TARStringTool {

    public static boolean isEmptyWithString(String str)
    {
        if (str instanceof String){
        }else {
            return true;
        }
        if (str == null || str.length()<1) {
            return true;
        }
        return false;
    }










    /**
     * 判断手机号运营商
     * 入参：手机号
     * return param
     * code (int):200 手机号符合要求  code:500手机号不合要求
     * type  (int): 1:YD 2:LT 3:DX
     */
    public static String getPhoneNumberOperator(String phoneNumber) {
        return TARPhoneNumberVerification.getPhoneNumberOperator(phoneNumber);
    }
}
