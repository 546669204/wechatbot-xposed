package com.example.administrator.webot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.io.IOException;
import java.net.Socket;

public class MyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("MyService.onBind");
        return null;
    }
    @Override
    public void onCreate() {
        System.out.println("MyService.onCreate");
        new Thread(new Runnable(){
            @Override
            public void run() {
                MySocket sock = new MySocket();
                sock.main();
            }
        }).start();
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("MyService.onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("MyService.onDestroy");

        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("MyService.onUnbind");
        return super.onUnbind(intent);
    }
}
