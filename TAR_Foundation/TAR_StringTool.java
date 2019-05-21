package com.huanwei.TAR_UtilsForAndroid.TAR_Foundation;

import android.util.Base64;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/12/12.
 */
/**
 * 字符串处理类.
 */
public class TAR_StringTool {
    /**
     * 截取字符串 小数点后两位，多删少补
     */
    public static String formateRate(String rateStr) {
        if (rateStr.indexOf(".") != -1) {
            // 获取小数点的位置
            int num = 0;
            num = rateStr.indexOf(".");
            // 获取小数点后面的数字 是否有两位 不足两位补足两位
            String dianAfter = rateStr.substring(0, num + 1);
            String afterData = rateStr.replace(dianAfter, "");
            if (afterData.length() < 2) {
                afterData = afterData + "0";
            }
            return rateStr.substring(0, num) + "." + afterData.substring(0, 2);
        } else {
            return rateStr + ".00";

        }
    }


    /**
     * 字符串进行Base64编码
     * @param str
     */
    public static String StringToBase64(String str) {
        String encodedString = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        return encodedString;
    }

    /**
     * 字符串进行Base64解码
     * @param encodedString
     * @return
     */
    public static String Base64ToString(String encodedString) {
        String decodedString = new String(Base64.decode(encodedString, Base64.DEFAULT));
        return decodedString;
    }



    /**
     * 获取16进制随机数
     * @param len
     * @return
     * @throws
     */
    public static String getRandomHexString(int len){
        try {
            StringBuffer result = new StringBuffer();
            for(int i=0;i<len;i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            return result.toString().toUpperCase();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将list转换为带有 ， 的字符串
     * @param list
     * @return
     */
    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     *  判断一个字符串是否为数量类型 （包含 正负数、整数小数）
     * */
    public static boolean isNumbers(String str){
        //采用正则表达式的方式来判断一个字符串是否为数字，这种方式判断面比较全
        //可以判断正负、整数小数
        //?:0或1个, *:0或多个, +:1或多个
        boolean strResult = str.matches("-?[0-9]+.?[0-9]*");
        if(strResult == true) {
            System.out.println("Is Number!");
        } else {
            System.out.println("Is not Number!");
        }
        return strResult;
    }

}
