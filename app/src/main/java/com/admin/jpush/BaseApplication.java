package com.admin.jpush;

import android.app.Application;

import com.admin.jpush.testdemo.ToastUtil;

import cn.jpush.android.api.JPushInterface;

public class BaseApplication extends Application{

    private static BaseApplication mInstance;
    public  static String appName ;
    public static boolean isNotify = true;//通知栏是否提醒新消息

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        appName = getApplicationInfo().name;
        //请求权限
        //JPushInterface.requestPermission(this);
        //开启极光推送:
        //1.debug 为true则会打印debug级别的日志，false则只会打印warning级别以上的日志。
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        /*//停止极光推送:
        //1.JPush SDK 提供的推送服务是默认开启的；
        //2.如果停止推送服务后，开发者App被卸载重新安装，JPush SDK 会恢复正常的默认行为。
        JPushInterface.stopPush(this);
        //恢复极光推送:
        //1.调用了此 API 后，极光推送完全恢复正常工作。
        JPushInterface.resumePush(this);
        //用来检查 Push Service 是否已经被停止
        JPushInterface.isPushStopped(this);
        //JPush SDK开启和关闭省电模式，默认为关闭
        JPushInterface.setPowerSaveMode(this,false);
        //获得注册的key
        //String rid = JPushInterface.getRegistrationID(this);*/
    }
    public static BaseApplication getAppContext() {
        return mInstance;
    }

    public static boolean isIsNotify() {
        return isNotify;
    }

    public static void setIsNotify(boolean isNotify) {
        BaseApplication.isNotify = isNotify;
    }
}
