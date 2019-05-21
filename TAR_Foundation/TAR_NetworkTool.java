package com.huanwei.TAR_UtilsForAndroid.TAR_Foundation;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2018/12/12.
 */
/**
 * 网络检查类
 * */
public class TAR_NetworkTool {

    /**
     * 检查网络是否连通
     */
    public static boolean isNetworkAvailable(Context context) { // 创建并初始化连接对象
        ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 判断初始化是否成功并作出相应处理
        if (connMan != null) { // 调用getActiveNetworkInfo方法创建对象,如果不为空则表明网络连通，否则没连通
            NetworkInfo info = connMan.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }




    /**
     * 检测当前网络的类型 是否是wifi
     *
     * @param context
     * @return
     */
    public static int checkedNetWorkType(Context context) {
        if (!isNetworkAvailable(context)) {
            return 0;//无网络
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()) {
            return 1;//wifi
        } else {
            return 2;//非wifi
        }
    }

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    public static boolean checkGPSIsOpen(Context context) {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            isOpen = true;
        } else {
            isOpen = false;
        }
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    /*
    public static void openGPSSettings(final Context context) {
        if (checkGPSIsOpen(context)) { //            initLocation(); //自己写的定位方法
        } else { //            //没有打开则弹出对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

            builder.setTitle("温馨提示");
            builder.setMessage("当前应用需要打开定位功能。请点击\"设置\"-\"定位服务\"打开定位功能。");
            //设置对话框是可取消的
            builder.setCancelable(false);

            builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    //跳转GPS设置界面
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    ActivityManager.getInstance().exit();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    */








}
