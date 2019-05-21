package com.huanwei.TAR_UtilsForAndroid.TAR_Foundation;

/**
 * Created by Administrator on 2019/4/8.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;

/**
 * 系统权限管理类。
 */
public class TAR_SystemPermissionsManger {

    /**
     * 是否应该检查权限
     *
     * @return
     */
    public static boolean showCheckPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }


    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    private static final int PRIVATE_CODE = 1315;//开启GPS权限
    /**
     * 提示获取位置权限
     * */
    public static boolean showCheckPermissions(Activity context) {
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//是否打开了定位服务
            if (Build.VERSION.SDK_INT >= 23) {//判断是否为android6.0系统版本，如果是，需要动态添加权限
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {// 没有权限，申请权限。
                    ActivityCompat.requestPermissions(context, LOCATIONGPS,
                            BAIDU_READ_PHONE_STATE);

                }
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
                        //开启定位权限,200是标识码
                        ActivityCompat.requestPermissions(AndroidUtil.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                        return false;
                    } else {
                        return true;
                    }
                }else {


                return false;

            }
        }else {
            Toast.makeText(context, "系统检测到未开启GPS定位服务,请开启", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivityForResult(intent, PRIVATE_CODE);
            return false;
        }



    }
    static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE};











    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    /**
     * 申请摄像头权限
     * 回调在 Activity中的onRequestPermissionsResult 方法回调
     */
    private void requestCameraAccessPermissions(Activity activity) {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                activity.startActivityForResult(new Intent(activity, CaptureActivity.class), 0);
            }
        } else {
            activity.startActivityForResult(new Intent(activity, CaptureActivity.class), 0);
        }
    }
 }
