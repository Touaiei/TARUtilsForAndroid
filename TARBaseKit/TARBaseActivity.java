package com.tar.ronghuibaoforandroid.TARUtilsForAndroid.TARBaseKit;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tar.ronghuibaoforandroid.TARUtilsForAndroid.TARMyApplication;

/**
 * Created by TAR on 2018/1/10.
 */

/**
 * 基础BaseActivity
 */
public abstract class TARBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TARMyApplication.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        initMainView();
//        requestMainData();
    }


    /**
     * 初始化主 UI 视图
     * */
    public abstract void initMainView();

    /**
     * 请求数据
     * */
    public abstract void requestMainData();



}


