package com.tar.ronghuibaoforandroid.TARUtilsForAndroid;

import com.alibaba.fastjson.JSONObject;

import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by TAR on 2018/4/3.
 */

/**
 * 电话号码验证码类
 */

public class TARPhoneNumberVerification
{
    /**
     * 判断手机号运营商
     * 入参：手机号
     * return param
     * code (int):200 手机号符合要求  code:500手机号不合要求
     * type  (int): 1:YD 2:LT 3:DX
     */
    public static String getPhoneNumberOperator(String phoneNumber) {
        String YD = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[23478]{1})|([4]{1}[7]{1})|([7]{1}[8]{1}))[0-9]{8}$";
        String LT = "^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([8]{1}[56]{1})|([4]{1}[5]{1})|([7]{1}[6]{1}))[0-9]{8}$";
        String DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[09]{1})|([7]{1}[37]{1}))[0-9]{8}$";
        /*
        *   1、移动号段有134,135,136,137, 138,139,147,150,151, 152,157,158,159,178,182,183,184,187,188。
            2、联通号段有130，131，132，155，156，185，186，145，176。
            3、电信号段有133，153，177.173，180，181，189。
        * */
        JSONObject jsonObject=new JSONObject();
        // 判断手机号码是否是11位
        if (phoneNumber.length() == 11) {
            // 判断手机号码是否符合中国移动的号码规则
            if (phoneNumber.matches(YD)) {
                jsonObject.put("code",200);
                jsonObject.put("type",1);
            }
            // 判断手机号码是否符合中国联通的号码规则
            else if (phoneNumber.matches(LT)) {
                jsonObject.put("code",200);
                jsonObject.put("type",2);
            }
            // 判断手机号码是否符合中国电信的号码规则
            else if (phoneNumber.matches(DX)) {
                jsonObject.put("code",200);
                jsonObject.put("type",3);
            }
            // 都不合适 未知
            else {
                jsonObject.put("code",500);
            }
        }
        // 不是11位
        else {
            jsonObject.put("code",500);
        }
        return jsonObject.toString();
    }









    public static String getMobileAttribution(String Mobile){
        String str = "";
        str = TARPhoneNumberVerification.getMobileNoTrack(Mobile);
        if(str != null && !"".equals(str)){
            str = str.substring(str.indexOf("：")+1);
            String strArry [] = new String[]{};
            strArry = str.split(" ");
            str = strArry[0];
        }
        return str;
    }

    private static String getSoapRequest(String mobileCode) {

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + " "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" + " "
                + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "\n"
                + "<soap:Body>" + "\n"
                + "<getMobileCodeInfo" + " " + "xmlns=\"http://WebXml.com.cn/\">" + "\n"
                + "<mobileCode>" + mobileCode + "</mobileCode>" + "\n"
                + "<userID></userID>" + "\n"
                + "</getMobileCodeInfo>" + "\n"
                + "</soap:Body>" + "\n"
                + "</soap:Envelope>"
        );
        return sb.toString();

    }

    private static InputStream getSoapInputStream(String mobileCode) {
        try {
            String soap = getSoapRequest(mobileCode);
            if (soap == null)
                return null;
            URL url = new URL("http://www.webxml.com.cn/WebServices/MobileCodeWS.asmx");
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
            conn.setRequestProperty("SOAPAction", "http://WebXml.com.cn/getMobileCodeInfo");
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(soap);
            osw.flush();
            osw.close();
            osw.close();
            InputStream is = conn.getInputStream();
            return is;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getMobileNoTrack(String mobileCode) {
        try {
            org.w3c.dom.Document document = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            InputStream is = getSoapInputStream(mobileCode);
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(is);
            NodeList nl = document.getElementsByTagName("getMobileCodeInfoResult");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < nl.getLength(); i++) {
                org.w3c.dom.Node n = nl.item(i);
                if (n.getFirstChild().getNodeValue().equals("手机号码错误")) {
                    sb = new StringBuffer("#");
                    System.out.println("手机号码输入有误");
                    break;
                }
                sb.append(n.getFirstChild().getNodeValue() + "\n");
            }
            is.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}