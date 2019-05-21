package com.tar.ronghuibaoforandroid.TARUtilsForAndroid.TAR_UIKit;

import android.os.Bundle;
import android.webkit.WebView;

<<<<<<< HEAD
import com.tar.ronghuibaoforandroid.TARUtilsForAndroid.TARBaseKit.TARBaseActivity;
=======
<<<<<<< HEAD
import com.tar.ronghuibaoforandroid.TARUtilsForAndroid.TARBaseKit.TARBaseActivity;
=======
import com.tar.ronghuibaoforandroid.Bases.TARBaseActivity;
>>>>>>> 1e5c769ad367275f3697be5f1f2b1f5bd683ee3a
>>>>>>> a59e02eddbf0120abde6ce272946230beccd2653

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
