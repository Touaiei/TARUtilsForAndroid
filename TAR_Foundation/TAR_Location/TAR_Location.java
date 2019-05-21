package com.huanwei.TAR_UtilsForAndroid.TAR_Foundation.TAR_Location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2019/3/21.
 */

public class TAR_Location {
    private static TAR_Location instance = null;
    private Context context = null;


    /***
     * 一种常用的形式
     */
    public static synchronized TAR_Location getInstance(Context context ) {
        // 这个方法比上面有所改进，不用每次都进行生成对象，只是第一次
        // 使用时生成实例，提高了效率！
        if (instance == null)
            instance = new TAR_Location();
        instance.context =context;
        return instance;
    }

    /**
     * 首先获取LocationManager的对象
     * */
    private LocationManager getLocationManager(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager;
    }

    /**
     * 设置定位方式，GPS或网络定位，两种定位方式各有特点，
     * GPS定位精度高，但是非常耗电，
     * 网络定位精度稍低，但耗电量比较小：
     * */
    private String judgeProvider(Context context, LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        /*
        if (prodiverlist.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;//网络定位
        } else if (prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;//GPS定位
        } else {
            Toast.makeText(context, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
        }
        */
        if (prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;//GPS定位
        }
        return null;
    }

    /**
     * 最后获取包含当前位置信息（比如经度纬度）的Location对象：
     * */
    public Location beginLocation(Context context) {
        //获得位置服务
        LocationManager locationManager = getLocationManager(context);
        String provider = judgeProvider(context, locationManager);
        //有位置提供器的情况
        if (provider != null) {
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }else {
                Location location = locationManager.getLastKnownLocation(provider);
                Log.e("", "providerprovider=="+provider );
                return location;
            }
        } else {
            //不存在位置提供器的情况
            Toast.makeText(context, "不存在位置提供器的情况", Toast.LENGTH_SHORT).show();
            return null;
        }
//        return null;
    }

    @SuppressLint("MissingPermission")
    public void locationUpdate(TAR_LocationListener listener) {
        LocationManager locationManager = getLocationManager(context);
        String provider = judgeProvider(context, locationManager);
        if (provider == null){
            Toast.makeText(context,"未开启定位服务",Toast.LENGTH_LONG).show();
            return;
        }
        locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);// 产生位置改变事件的条件设定为距离改变10米，时间间隔为2秒，设定监听位置变化
        setTar_locationListener(listener);
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            //onLocationChanged()这个方法在位置改变时被调用
            Toast.makeText(context,"onLocationChanged", Toast.LENGTH_LONG).show();
            tar_locationListener.onLocationChanged(location);
        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub
            //onProviderDisabled()这个方法在用户禁用具有定位功能的硬件时被调用
            Toast.makeText(context,"onProviderDisabled", Toast.LENGTH_LONG).show();
            tar_locationListener.onProviderDisabled(arg0);
        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub
            //onProviderEnabled()这个方法在用户启用具有定位功能的硬件时被调用
            Toast.makeText(context,"onProviderEnabled", Toast.LENGTH_LONG).show();
            tar_locationListener.onProviderEnabled(arg0);

        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub
            //onStatusChanged()这个方法在定位功能硬件状态改变时被调用
            Toast.makeText(context,"onStatusChanged", Toast.LENGTH_LONG).show();
            tar_locationListener.onStatusChanged(arg0,arg1,arg2);

        }

    };




    private TAR_LocationListener tar_locationListener = null;
    public interface TAR_LocationListener {
        void onLocationChanged(Location location);
        void onStatusChanged(String provider, int status, Bundle extras);
        void onProviderEnabled(String provider);
        void onProviderDisabled(String provider);
    }
    private void setTar_locationListener(TAR_LocationListener tar_locationListener) {
        this.tar_locationListener = tar_locationListener;
    }
}
