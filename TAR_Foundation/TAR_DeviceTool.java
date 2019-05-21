package com.huanwei.TAR_UtilsForAndroid.TAR_Foundation;

/**
 * Created by Administrator on 2019/3/21.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

/**
 * 设备工具类（android）
 * */
public class TAR_DeviceTool {
    /**
     * 获取android设备的序列号
     * return 设备的序列号
     */
    public static String getDeviceSerialNumber() {
        String SerialNumber = android.os.Build.SERIAL;
        return SerialNumber;
    }

    /**
     * 获取android设备的Mac地址
     * return Mac地址
     * --注意必须在AndroidManifest.xml配置访问WIFI的权限--<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static String getDeviceMacAddress() {
        /*
        获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，
 不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，
 这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。
         */
        // String macAddress= "";
        // WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
        // WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // macAddress = wifiInfo.getMacAddress();
        // return macAddress;
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    /**
     * 得到全局唯一UUID
     */
    private String uuid;
    public String getUUID(Context context){
        SharedPreferences mShare = context.getSharedPreferences("uuid",MODE_PRIVATE);
        if(mShare != null){
            uuid = mShare.getString("uuid", "");
        }
        if(TextUtils.isEmpty(uuid)){
            uuid = UUID.randomUUID().toString();
            mShare.edit().putString("uuid",uuid).commit();
        }
        return uuid;
    }

    public String getMacAddress() {
        String macAddress = null ;
        String str = "" ;
        try {
            //linux下查询网卡mac地址的命令
            Process pp = Runtime.getRuntime().exec( "cat /sys/class/net/wlan0/address" );
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;) {
                str = input.readLine();
                if (str != null ) {
                    macAddress = str.trim();// 去空格
                    break ;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return macAddress;
    }
    public String getMacAddress(Context context) {
        String macAddress = null ;
        WifiManager wifiManager =
                (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = ( null == wifiManager ? null : wifiManager.getConnectionInfo());
        macAddress = info.getMacAddress();
        return macAddress;
    }


}
