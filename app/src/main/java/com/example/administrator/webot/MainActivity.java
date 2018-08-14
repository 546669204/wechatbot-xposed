package com.example.administrator.webot;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);

        String host = sp.getString("host","");
        int port = sp.getInt("port",1);
        ((EditText)findViewById(R.id.editText)).setText(""+port);
        ((EditText)findViewById(R.id.editText2)).setText(""+host);

        ((TextView)findViewById(R.id.textView2)).setText("地址:"+host+"+port:"+port);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){ //设置监听事件
            @Override
            public void onClick(View view){
                SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("port",Integer.parseInt(((EditText)findViewById(R.id.editText)).getText().toString()));
                editor.putString("host",((EditText)findViewById(R.id.editText2)).getText().toString());
                editor.commit();
                Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
            }
        });




    }
    protected  void onDestroy(){
        super.onDestroy();
    }

}
