package com.tar.ronghuibaoforandroid.TARUtilsForAndroid.TAR_UIKit;

import android.os.Bundle;
import android.webkit.WebView;

import com.tar.ronghuibaoforandroid.TARUtilsForAndroid.TARBaseKit.TARBaseActivity;

/*
*
* */
public class WebViewActivity extends TARBaseActivity {
    WebView mainWebView = new WebView(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tarweb_view);
        setContentView(mainWebView);

        initMainView();
        requestMainData();
    }

    @Override
    public void initMainView() {

    }

    public void initWebView(){

    }

    @Override
    public void requestMainData() {

    }


}
