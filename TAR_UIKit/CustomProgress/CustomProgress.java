package com.huanwei.TAR_UtilsForAndroid.TAR_UIKit.CustomProgress;

/**
 * Created by Administrator on 2017/5/4.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanwei.huanwei.R;

/**
 * 在屏幕中间转圈圈的那种进度动画
 * */
public class CustomProgress extends Dialog {
    private static CustomProgress dialog;
    public static CustomProgress getInstance(Context context) {
        if (dialog == null) {
            dialog = new CustomProgress(context);
        }
        return dialog;
    }
    public static CustomProgress getInstance(Context context, int theme) {
        if (dialog == null) {
            dialog = new CustomProgress(context,theme);
        }
        return dialog;
    }
    public static CustomProgress init(){
        return dialog;
    }
    private CustomProgress(Context context) {
        super(context);
    }

    private CustomProgress(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        // 开始动画
        spinner.start();
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context
     *            上下文
     * @param message
     *            提示
     * @param cancelable
     *            是否按返回键取消
     * @param cancelListener
     *            按下返回键监听
     * @return
     */
    public static CustomProgress show(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        dialog = getInstance(context, R.style.Custom_Progress);
//        new CustomProgress(context, R.style.Custom_Progress);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_layout);
        if (message == null || message.length() == 0) {
            TextView textView = (TextView)dialog.findViewById(R.id.message);
            textView.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView)dialog.findViewById(R.id.message);
            textView.setText(message);
        }
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        if (cancelListener == null){
            // 监听返回键处理
            dialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    close();
                }
            });
        }else {
            // 监听返回键处理
            dialog.setOnCancelListener(cancelListener);
        }
        // 设置居中
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = layoutParams;
        // 设置背景层透明度
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
//         dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }
    public static void close() {
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
