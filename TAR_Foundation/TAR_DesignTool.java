package com.huanwei.TAR_UtilsForAndroid.TAR_Foundation;

import android.content.Context;

/**
 * Created by Administrator on 2018/12/12.
 */
/**
 * UI设计类，（界面适配，如像素分辨率 转换等）.
 */

public class TAR_DesignTool {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    } /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }




}
