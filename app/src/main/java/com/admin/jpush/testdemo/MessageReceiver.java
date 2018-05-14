package com.admin.jpush.testdemo;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.admin.jpush.BaseApplication;
import com.admin.jpush.Logger;
import com.admin.jpush.R;
import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.UUID;

import cn.jpush.android.api.JPushInterface;

/**
 * 消息接收器
 */

public class MessageReceiver  extends BroadcastReceiver {
    private static final String TAG = "MessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Logger.d(TAG, "[MessageReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MessageReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MessageReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                //自定义消息不会展示在通知栏，完全要开发者写代码去处理
                //发送消息给ActMessage
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MessageReceiver] 接收到推送下来的通知");
                //如果通知的内容为空，则在通知栏上不会展示通知。
                //但是，这个广播 Intent 还是会有。开发者可以取到通知内容外的其他信息。
                //int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                //Logger.d(TAG, "[MessageReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MessageReceiver] 用户点击打开了通知");

                //1. 一般情况下，用户不需要配置此 receiver action。
                //2.如果开发者在 AndroidManifest.xml 里未配置此 receiver action，
                // 那么，SDK 会默认打开应用程序的主 Activity，相当于用户点击桌面图标的效果。
                //3.如果开发者在 AndroidManifest.xml 里配置了此 receiver action，
                // 那么，当用户点击通知时，SDK 不会做动作。开发者应该在自己写的 BroadcastReceiver 类里处理，比如打开某 Activity 。
                //String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                /*//打开自定义的Activity
                Intent i = new Intent(context, TestActivity.class);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(i);*/

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MessageReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                //JPush 服务的连接状态发生变化。（注：不是指 Android 系统的网络连接状态。）
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[MessageReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
                Logger.d(TAG, "[MessageReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //解析intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //处理自定义消息
    private void processCustomMessage(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String jsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);//Json字符串

        MessageMode mode = new MessageMode();
        mode.setTitle(title);
        mode.setMsg(message);
        mode.setExtra(jsonStr);

        if(BaseApplication.isIsNotify()){
            //自定义消息不会展示在通知栏，完全要开发者写代码去处理
            //把消息展示在通知栏
            NotifyUtil notify = new NotifyUtil(context, UUID.randomUUID().hashCode());
            PendingIntent pIntent = getPendingIntent(context, Constant.KEY, mode, ActMessage.class);
            notify.notify_normail_moreline(pIntent, R.mipmap.ic_launcher_round, "", title, message, true, true, false);
        }

        //发布消息给整个项目
        EventBus.getDefault().post(mode);
    }

    private PendingIntent getPendingIntent(Context context, String key, MessageMode mode, Class classz) {
        Intent intent = new Intent(context, classz);
        if (!TextUtils.isEmpty(key) && mode != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(key,mode);
            intent.putExtras(bundle);
        }
        PendingIntent pIntent = PendingIntent.getActivity(context,1024, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pIntent;
    }
}
