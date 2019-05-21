package com.huanwei.TAR_UtilsForAndroid.TAR_UIKit.TAR_Toast;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/12/12.
 */
/**
 * 提示工具类
 * */
public class TAR_Toast {
    /**
     * toast 多次点击只会出现一个
     */
    private static Toast toast = null;
    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

}
