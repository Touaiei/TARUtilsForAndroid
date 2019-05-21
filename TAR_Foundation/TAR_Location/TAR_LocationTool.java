package com.huanwei.TAR_UtilsForAndroid.TAR_Foundation.TAR_Location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
/**
 * 位置相关基础工具类
 *
 * */
public class TAR_LocationTool {
    private static String TAG = "TAR_LocationTool";
//    final protected String TAG = getClass().getName();


    /**
     * 将经纬度转换成中文地址
     *
     * @param location
     * @return
     */
    static public String getLocationAddress(Context context, Location location) {
        String add = "";
        Geocoder geoCoder = new Geocoder(context, Locale.CHINESE);
        try {
            List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address address = addresses.get(0);
            Log.i(TAG, "getLocationAddress: " + address.toString());
            // Address[addressLines=[0:"中国",1:"北京市海淀区",2:"华奥饭店公司写字间中关村创业大街"]latitude=39.980973,hasLongitude=true,longitude=116.301712]
            int maxLine = address.getMaxAddressLineIndex();
            if (maxLine >= 2) {
                add = address.getAddressLine(1) + address.getAddressLine(2);
            } else {
                add = address.getAddressLine(1);
            }
        } catch (IOException e) {
            add = "";
            e.printStackTrace();
        }
        return add;
    }



}
