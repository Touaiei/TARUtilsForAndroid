package com.huanwei.TAR_UtilsForAndroid.TAR_Foundation;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

import java.util.List;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/12/12.
 */
/**
 * 跳转到系统的网络设置界面
 */
public class TAR_SystemTool {

    /**
     * 跳转到系统的网络设置界面
     */
    public void jumpToSettings(Context context) {
        Intent intent = null;
        // 先判断当前系统版本
        if (android.os.Build.VERSION.SDK_INT > 10) { // 3.0以上
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
        }
        context.startActivity(intent);

        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        context.startActivity(wifiSettingsIntent);
    }


    /**
     * 判断应用是否已经启动
     *
     * @param context     上下文对象
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断服务是否正在运行
     *
     * @param serviceName 服务类的全路径名称 例如： com.jaychan.demo.service.PushService
     * @param context     上下文对象
     * @return
     */
    public static boolean isServiceRunning(String serviceName, Context context) { //活动管理器
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100); //获取运行的服务,参数表示最多返回的数量

        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            String className = runningServiceInfo.service.getClassName();
            if (className.equals(serviceName)) {
                return true; //判断服务是否运行
            }
        }
        return false;
    }

    /**
     * 判断SD卡是否可用
     *
     * @return SD卡可用返回true
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(status);
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




}
