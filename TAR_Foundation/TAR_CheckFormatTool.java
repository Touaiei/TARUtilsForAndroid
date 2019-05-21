package com.huanwei.TAR_UtilsForAndroid.TAR_Foundation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/12/12.
 */
/**
 *  格式检查
 */
public class TAR_CheckFormatTool {
    private static final int PASS_LOW_LIMIT = 6;
    private static final int PASS_HIGH_LIMIT = 16;

    /**
     *  检测手机号，返回boolean
     */
    public static boolean checkPhone(String mobiles) {
        Pattern p = Pattern.compile("^((14[5,7])|(13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证身份证号码是否合法
     */
    public static boolean validateIDCardNumber(String number) {
        return (number.length() == 15 && number.matches("^\\d{15}")) || (number.length() == 18 && (number.matches("^\\d{17}[x,X,\\d]")));
    }




    /**
     *  检测金额(保留小数点后两位)
     */
    public static boolean checkMoney(String money) {
        Pattern p = Pattern.compile("^[0-9]+(.[0-9]{2})?$");
        Matcher m = p.matcher(money);
        return m.matches();
    }


    /**
     * 验证密码是否合法　６－１６位
     */
    public static boolean validatePass(String password) {
        return password.length() >= PASS_LOW_LIMIT && password.length() <= PASS_HIGH_LIMIT;
    }

    /**
     * 判断是不是英文字母
     */
    public static boolean isECharacter(String codePoint) {
        return codePoint.matches("^[A-Za-z]$");
    }







}
