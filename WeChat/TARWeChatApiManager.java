package com.tar.ronghuibaoforandroid.TARUtilsForAndroid.WeChat;



import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.tar.ronghuibaoforandroid.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

import okhttp3.internal.Util;


/**
 * Created by TAR on 2018/1/16.
 */

/**
 *微信相关api管理类
 * */


public class TARWeChatApiManager implements IWXAPIEventHandler {
    private static final String TAG = String.format("%s", "TARWeChatApiManager");

    private String appID = "wxc3b1010635191b5d";
    private IWXAPI api;
    private android.app.Activity mActivity;


    public TARWeChatApiManager(Activity activity) {
        if (activity == null){
            Log.e(TAG, "TARWeChatApiManager: "+"无法调起微信支付，请指定当前Activity");
        }
        this.mActivity = activity;
        Log.e(TAG, "this.mActivity: "+this.mActivity);
        Log.e(TAG, "mActivity: "+mActivity);
    }


    /*注册微信appID*/
    public void registerAppID(String appID)
    {
        this.appID = appID;
        //通过WXAPIFactory工厂获取IWAPI的实例
        api = WXAPIFactory.createWXAPI(mActivity,this.appID,true);
        //将应用的appID注册到微信
        api.registerApp(this.appID);
    }

    public void startWeChatPayWithPayOrder(JSONObject order)
    {
        Log.e(TAG, "startWeChatPayWithPayOrder: "+order);

        String appId = order.getString("appId");
        String partnerId = order.getString("partnerId");
        String prepayId = order.getString("prepayId");
        String packageValue = order.getString("packageValue");
        String nonceStr = order.getString("nonceStr");
        String timeStamp = order.getString("timeStamp");
        String sign = order.getString("sign");

        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.packageValue = packageValue;
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;

        Log.e(TAG, "startWeChatPayWithPayOrder: "+request.appId+"\n"+request.partnerId+"\n"+request.prepayId+"\n"+request.packageValue+"\n"+request.nonceStr+"\n"+request.timeStamp+"\n"+request.sign);


//        PayReq request = new PayReq();
//        request.appId = "wxd930ea5d5a258f4f";
//        request.partnerId = "1900000109";
//        request.prepayId= "1101000000140415649af9fc314aa427",;
//        request.packageValue = "Sign=WXPay";
//        request.nonceStr= "1101000000140429eb40476f8896f4c9";
//        request.timeStamp= "1398746574";
//        request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
        api.sendReq(request);
    }


    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            Log.d(TAG,"onPayFinish,errCode="+baseResp.errCode);
            if (baseResp.errCode == 0){
                Toast.makeText(mActivity, "支付成功！", Toast.LENGTH_SHORT).show();
            }else if (baseResp.errCode == -1){
            }else if (baseResp.errCode == -2){
                Toast.makeText(mActivity, "用户取消了！", Toast.LENGTH_SHORT).show();
            }else if (baseResp.errCode == -3){
                Toast.makeText(mActivity, "发送失败了！", Toast.LENGTH_SHORT).show();
            }else if (baseResp.errCode == -4){
                Toast.makeText(mActivity, "身份验证未通过！", Toast.LENGTH_SHORT).show();
            }else if (baseResp.errCode == -5){
            }else if (baseResp.errCode == -6){
                Toast.makeText(mActivity, "禁止支付！", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mActivity, "我也不知道为什么没有支付成功！", Toast.LENGTH_SHORT).show();
            }
//            AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
//            builder.setTitle(R.string.app_tip);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    public void dfdfdfdf()
    {
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = "http://www.baidu.com/";
        //
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = "dddd";
        msg.description = "dddaaa";
        Bitmap thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.search_bar_icon_normal);
//        msg.thumbData = Util.bmpToByteArray(thumb, true);
//        msg.thumbData = Bitmap2Bytes(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("webpage");
        req.transaction = "webpage";


        req.message = msg;
//        req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

//        api.sendReq(req);
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }



    public void startWeChatShareForWebPageWithURL(String url,String title,String description,int iconResourcesID,int scene)
    {
        WXWebpageObject webpage = new WXWebpageObject();

        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = title;

        msg.description = description;

        Bitmap thumb = BitmapFactory.decodeResource(mActivity.getResources(), iconResourcesID);

//        msg.thumbData = Util.bmpToByteArray(thumb, true);
        msg.thumbData = Bitmap2Bytes(thumb);





        SendMessageToWX.Req req = new SendMessageToWX.Req();

//        req.transaction = buildTransaction("webpage");
        req.transaction = "webpage";

        req.message = msg;

//        req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        req.scene = scene;

        api.sendReq(req);
    }
}
