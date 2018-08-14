package com.example.administrator.webot;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyApplication extends Application{
    private static Context context;
    NotificationManager nm;
    Notification notification;
    public void onCreate() {
        super.onCreate();
        //获取Context
        context = getApplicationContext();

        context.startService(new Intent(this, MyService.class));
    }

    //返回
    public static Context getContextObject(){
        return context;
    }
}
