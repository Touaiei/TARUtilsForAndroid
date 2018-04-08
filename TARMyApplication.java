package com.tar.ronghuibaoforandroid.TARUtilsForAndroid;

import android.app.Activity;
import android.app.Application;

import java.util.Stack;

//import cn.jpush.android.api.JPushInterface;

/**
<<<<<<< HEAD
 * Created by Administrator on 2017/6/27..
=======
 * Created by Administrator on 2017/6/27.
>>>>>>> a59e02eddbf0120abde6ce272946230beccd2653
 */

public class TARMyApplication extends Application {
    private static Stack<Activity> activityStack;
    private static TARMyApplication singleton;

    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;
        /*
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        */
    }


    // 返回应用程序实例
    public static TARMyApplication getInstance() {
        return singleton;
    }

    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }
}
