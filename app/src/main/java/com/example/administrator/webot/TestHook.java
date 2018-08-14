package com.example.administrator.webot;

/**
 * Created by Administrator on 2018/4/27 0027.
 */
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.RangeValueIterator;
import android.net.LocalSocketAddress;
import android.provider.DocumentsContract;
import android.sax.Element;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import org.json.JSONObject;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodHook;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findFirstFieldByExactType;
import static de.robv.android.xposed.XposedHelpers.newInstance;
import static android.content.ContentValues.TAG;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class TestHook implements IXposedHookLoadPackage {
    public static Set<String> methodSignSet = Collections.synchronizedSet(new HashSet<String>());
    public static Set<String> callMethodSignSet = Collections.synchronizedSet(new HashSet<String>()) ;
    private static List classArray = new ArrayList();
    private static List classArray2 = new ArrayList();
    private static final String FILTER_PKGNAME = "com.tencent.mm";
    private static Object requestCaller;

    //初始化设置Socket
    private static DatagramSocket client = null;
    public static Boolean isInit = false;

    private static BufferedReader in = null;
    private static PrintWriter out = null;
    private static ClassLoader globalloader = null;
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        String pkgname = lpparam.packageName;
        XposedBridge.log("Loaded app: " + lpparam.packageName);
        if(FILTER_PKGNAME.equals(pkgname)){
            Class<?> hookMessageListenerClass =null;
             hookMessageListenerClass =lpparam.classLoader.loadClass("com.tencent.mm.ac.o");
             Class<?> hookMessageListenerClass2 =null;
            hookMessageListenerClass2 =lpparam.classLoader.loadClass("com.tencent.mm.ac.e");
             Class<?> hookclass1 =null;
            hookclass1 =lpparam.classLoader.loadClass("com.tencent.mm.modelmulti.i");

            client = new DatagramSocket();
            initudp();
            //SharedPreferences sp =  Context.getSharedPreferences("auth_info_key_prefs", Context.MODE_PRIVATE);

            new Thread(new Runnable(){
                public void run(){
                    while(true) {
                        if (isInit){
                            try {
                                byte[] bytes = new byte[2048];
                                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                                client.receive(packet);
                                String reply = new String(bytes, 0, packet.getLength());
                                Log.i("udp client pull",reply);
                                postmsg(globalloader,reply.split("-----")[0],reply.split("-----")[1]);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i("socket",e.toString());
                                //client.close();
                            }
                        }


                    }
                }
            }).start();

            XposedHelpers.findAndHookMethod("com.tencent.mm.bu.a", lpparam.classLoader, "a",
                    String.class,
                    String.class,
                    String.class,
                    long.class,
                    String.class,
                    HashMap.class,
                    boolean.class,

                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                            Log.i("aaaddd","参数1  " +param.args[0]);
                            Log.i("aaaddd","参数2  " +param.args[1]);
                            Log.i("aaaddd","参数3  " +param.args[2]);
                            Log.i("aaaddd","参数4  " +param.args[3]);
                            Log.i("aaaddd","参数5  " +param.args[4]);
                            Log.i("aaaddd","参数6  " +param.args[5]);
                            Log.i("aaaddd","参数7  " +param.args[6]);
                        }
                    });


            findAndHookMethod("com.tencent.wcdb.database.SQLiteDatabase", lpparam.classLoader, "insert",String.class,String.class, ContentValues.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("开始劫持了~");
                    XposedBridge.log("参数1 = " + param.args[0]);
                    XposedBridge.log("参数2 = " + param.args[1]);
                    XposedBridge.log("参数3 = " + param.args[2]);
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    ContentValues contentValues = (ContentValues) param.args[2];
                    String tableName = (String) param.args[0];
                    XposedBridge.log("劫持结束了~");
                    XposedBridge.log("参数1 = " + param.args[0]);
                    XposedBridge.log("参数2 = " + param.args[1]);
                    //判断新消息
                    if (!TextUtils.isEmpty(tableName) && tableName.equals("message")) {
                        Integer isSend = contentValues.getAsInteger("isSend");
                        Integer type = contentValues.getAsInteger("type");
                        Integer status = contentValues.getAsInteger("status");
                        final String talker = contentValues.getAsString("talker");
                        final String content = contentValues.getAsString("content");

                        if (isSend == 0 && status == 3 && type == 1){ // type 1.文本 3.图片 48.位置
                            //群组 发言 talker=5885584579@chatroom content=zzxm88:text
                            //群组@ 发言 talker=5885584579@chatroom content=zzxm88:@嘻嘻  text
                            if (!isInit){
                                initudp();
                            }


                            globalloader = lpparam.classLoader;

                            postmsgbeta(lpparam.classLoader,talker,content);

                        }

                    }
                    //判断新好友
                    if (!TextUtils.isEmpty(tableName) && tableName.equals("fmessage_msginfo")) {
                        Integer isSend = contentValues.getAsInteger("isSend");
                        Integer type = contentValues.getAsInteger("type");
                        final String talker = contentValues.getAsString("talker");
                        final String content = contentValues.getAsString("content");
                        String msgcontent = contentValues.getAsString("msgContent");
                        if (isSend == 0 && type == 1){
                            /*
                            参数3 = encryptTalker=v1_cdf8a7b3da023da5307395352cd08500f7b4150729e300531a9da91d1f296ba7@stranger msgContent=<msg fromusername="zzxm88" encryptusername="v1_cdf8a7b3da023da5307395352cd08500f7b4150729e300531a9da91d1f296ba7@stranger" fromnickname="明" content="我是明" fullpy="ming" shortpy="M" imagestatus="3" scene="30" country="DE" province="Hamburg" city="" sign="" percard="1" sex="1" alias="xm________________" weibo="" weibonickname="" albumflag="0" albumstyle="0" albumbgimgid="912895298764800_912895298764800" snsflag="48" snsbgimgid="" snsbgobjectid="0" mhash="1b3dad158913a69d934caaadde76a00f" mfullhash="1b3dad158913a69d934caaadde76a00f" bigheadimgurl="http://wx.qlogo.cn/mmhead/ver_1/OGlGw9icNc0W5md9kfaC54hRN6zsxxLicIiboXkjYTSUs6FEhvV56QLz5icRLdkbicSsZCktcxiaQKT0qZsSEk6MYwng/0" smallheadimgurl="http://wx.qlogo.cn/mmhead/ver_1/OGlGw9icNc0W5md9kfaC54hRN6zsxxLicIiboXkjYTSUs6FEhvV56QLz5icRLdkbicSsZCktcxiaQKT0qZsSEk6MYwng/96" ticket="v2_c9ded7a183c8bc8c587756da612cea8fadc77558e98239c10d8bb1ff590f5a0088bdd7ef3ae4f6e99acc93e160c2baec@stranger" opcode="2" googlecontact="" qrticket="" chatroomusername="" sourceusername="" sourcenickname=""><brandlist count="0" ver="698050163"></brandlist></msg> chatroomName= svrId=5540230629124514099 createTime=1526281451000 talker=zzxm88 type=1 isSend=0
                            */


                            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                            Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(msgcontent.getBytes()));

                            String ticket = parse.getFirstChild().getAttributes().getNamedItem("ticket").getNodeValue();

                            requestCaller = callStaticMethod(findClass("com.tencent.mm.z.au", lpparam.classLoader), "Dv");
                            Object luckyMoneyRequest = newInstance(findClass("com.tencent.mm.pluginsdk.model.m", lpparam.classLoader),
                                    3,talker,ticket,30);
                            callMethod(requestCaller, "a", luckyMoneyRequest, 0);

                            postmsgbeta(lpparam.classLoader,talker,"newfriend");
                        }

                    }

                }
            });

/*
//2018-05-07
            findAndHookMethod("com.tencent.mm.plugin.luckymoney.b.ac", lpparam.classLoader, "a", int.class, String.class, JSONObject.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {

                            Log.i("luckmoney",JSON.toJSONString(param.args));
                            Log.i("luckmoney",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            findAndHookMethod("com.tencent.mm.modelmulti.i", lpparam.classLoader, "a", String.class,Object.class,int.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","string object int");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            findAndHookMethod("com.tencent.mm.modelmulti.i", lpparam.classLoader, "a", String.class,Object.class,int.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","string object int");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            hookMessageListenerClass =lpparam.classLoader.loadClass("com.tencent.mm.network.e");
            hookMessageListenerClass2 =lpparam.classLoader.loadClass("com.tencent.mm.ac.e");
            findAndHookMethod("com.tencent.mm.modelmulti.i", lpparam.classLoader, "a", hookMessageListenerClass,hookMessageListenerClass2, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.network.e com.tencent.mm.ac.e");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            XposedHelpers.findAndHookConstructor("com.tencent.mm.modelmulti.i", lpparam.classLoader,  String.class,String.class,int.class,int.class,Object.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.modelmulti.i S S i i O ");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            XposedHelpers.findAndHookConstructor("com.tencent.mm.modelmulti.i", lpparam.classLoader,  String.class,String.class,int.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.modelmulti.i S S ");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            XposedHelpers.findAndHookConstructor("com.tencent.mm.modelmulti.i", lpparam.classLoader,  new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.modelmulti.i ");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            XposedHelpers.findAndHookConstructor("com.tencent.mm.modelmulti.i", lpparam.classLoader,  long.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.modelmulti.i long ");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );


            findAndHookMethod("com.tencent.mm.z.au", lpparam.classLoader, "Dv",  new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.z.au#Dv");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            //--------------
            hookMessageListenerClass =lpparam.classLoader.loadClass("com.tencent.mm.ac.o");
            findAndHookMethod("com.tencent.mm.ac.o", lpparam.classLoader, "a", hookMessageListenerClass,int.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ac.o#a");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            hookMessageListenerClass =lpparam.classLoader.loadClass("com.tencent.mm.ac.o");
            findAndHookMethod("com.tencent.mm.ac.o", lpparam.classLoader, "a", hookMessageListenerClass,int.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ac.o#a");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            hookMessageListenerClass =lpparam.classLoader.loadClass("com.tencent.mm.ac.o");
            findAndHookMethod("com.tencent.mm.ac.o", lpparam.classLoader, "a", int.class,String.class,int.class,boolean.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ac.o#a");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );

            //--------------
            hookMessageListenerClass =lpparam.classLoader.loadClass("com.tencent.mm.storage.az");
            findAndHookMethod("com.tencent.mm.ui.chatting.af", lpparam.classLoader, "aH", hookMessageListenerClass, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ui.chatting.af#aH");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            findAndHookMethod("com.tencent.mm.ui.chatting.af", lpparam.classLoader, "aI", hookMessageListenerClass, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ui.chatting.af#aI");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            findAndHookMethod("com.tencent.mm.ui.chatting.af", lpparam.classLoader, "aJ", hookMessageListenerClass, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ui.chatting.af#aJ");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            findAndHookMethod("com.tencent.mm.ui.transmit.d", lpparam.classLoader, "D", String.class,String.class,int.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ui.transmit.d#D");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            findAndHookMethod("com.tencent.mm.ui.transmit.d", lpparam.classLoader, "l", String.class,String.class,boolean.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ui.transmit.d#l");
                            Log.i("postmsg-param",JSON.toJSONString(param.args));
                            Log.i("postmsg-result",JSON.toJSONString(param.getResult()));
                        }
                    }
            );
            //这里是为了解决app多dex进行hook的问题，Xposed默认是hook主dex
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    ClassLoader cl = ((Context)param.args[0]).getClassLoader();
                    Class<?> hookclass = null;
                    try {
                        hookclass = cl.loadClass("dalvik.system.DexFile");
                    } catch (Exception e) {
                        return;
                    }
                    //Log.i("shuchu",JSON.toJSONString(classArray2));
                    XposedHelpers.findAndHookMethod(hookclass, "loadClass", String.class, ClassLoader.class, new XC_MethodHook(){
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            //hookClassInfo((String)param.args[0], (ClassLoader)param.args[1]);
                            //classArray2.add(JSON.toJSONString(param));
                            //Log.i("shuchu",JSON.toJSONString(param));
                            super.beforeHookedMethod(param);
                        }
                    });

                    XposedHelpers.findAndHookMethod(hookclass, "loadClassBinaryName", String.class, ClassLoader.class, List.class,new XC_MethodHook(){
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            //hookClassInfo((String)param.args[0], (ClassLoader)param.args[1]);
                            //classArray2.add(JSON.toJSONString(param));
                            //Log.i("shuchu",JSON.toJSONString(param));
                            super.beforeHookedMethod(param);
                        }
                    });

                    XposedHelpers.findAndHookMethod(hookclass, "defineClass", String.class, ClassLoader.class, long.class, List.class,new XC_MethodHook(){
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            //hookClassInfo((String)param.args[0], (ClassLoader)param.args[1]);
                            //classArray2.add(JSON.toJSONString(param));
                            //Log.i("shuchu",JSON.toJSONString(param));
                            super.beforeHookedMethod(param);
                        }
                    });

                }
            });
*/





            /*hookMessageListenerClass =lpparam.classLoader.loadClass("com.tencent.mm.ac.o");
            findAndHookMethod("com.tencent.mm.ac.o", lpparam.classLoader, "a", int.class,String.class,int.class,boolean.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ac.o#a[int,str,int,bool]");
                            Log.i("postmsg-param",""+param.args[0]);
                            Log.i("postmsg-param",""+param.args[1]);
                            Log.i("postmsg-param",""+param.args[2]);
                            Log.i("postmsg-param",""+param.args[3]);
                            Log.i("postmsg-result",""+param.getResult());
                        }
                    }
            );

            hookMessageListenerClass =lpparam.classLoader.loadClass("com.tencent.mm.ac.e");
            findAndHookMethod("com.tencent.mm.ac.o", lpparam.classLoader, "a", int.class,hookMessageListenerClass, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ac.o#a[int,e]");
                            Log.i("postmsg-param",""+param.args[0]);
                            Log.i("postmsg-param",""+param.args[1]);
                            Log.i("postmsg-result",""+param.getResult());
                        }
                    }
            );

            hookMessageListenerClass =lpparam.classLoader.loadClass("com.tencent.mm.ac.l");
            findAndHookMethod("com.tencent.mm.ac.o", lpparam.classLoader, "a", hookMessageListenerClass,int.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ac.o#a[l,int]");
                            Log.i("postmsg-param",""+param.args[0]);
                            Log.i("postmsg-param",""+param.args[1]);
                            Log.i("postmsg-result",""+param.getResult());
                        }
                    }
            );
            hookMessageListenerClass =lpparam.classLoader.loadClass("com.tencent.mm.ac.l");
            findAndHookMethod("com.tencent.mm.ac.o", lpparam.classLoader, "a", int.class,int.class,String.class,hookMessageListenerClass, new XC_MethodHook() {
                            protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-param","com.tencent.mm.ac.o#a[int,int,str,l]");
                            Log.i("postmsg-param",""+param.args[0]);
                            Log.i("postmsg-param",""+param.args[1]);
                            Log.i("postmsg-param",""+param.args[2]);
                            Log.i("postmsg-param",""+param.args[3]);
                            Log.i("postmsg-result",""+param.getResult());
                        }
                    }
            );


            hookMessageListenerClass =lpparam.classLoader.loadClass("com.tencent.mm.ac.l");

            findAndHookConstructor("com.tencent.mm.pluginsdk.model.m", lpparam.classLoader,  int.class,String.class,String.class,int.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-function","com.tencent.mm.pluginsdk.model.m#m[int,str,str,int]");
                            Log.i("postmsg-param",""+param.args[0]);
                            Log.i("postmsg-param",""+param.args[1]);
                            Log.i("postmsg-param",""+param.args[2]);
                            Log.i("postmsg-param",""+param.args[3]);
                            Log.i("postmsg-result",""+param.getResult());
                        }
                    }
            );
            findAndHookConstructor("com.tencent.mm.pluginsdk.model.m", lpparam.classLoader,  String.class,String.class,int.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-function","com.tencent.mm.pluginsdk.model.m#m[str,str,int]");
                            Log.i("postmsg-param",""+param.args[0]);
                            Log.i("postmsg-param",""+param.args[1]);
                            Log.i("postmsg-param",""+param.args[2]);
                            Log.i("postmsg-result",""+param.getResult());
                        }
                    }
            );
            hookMessageListenerClass =lpparam.classLoader.loadClass("java.util.List");
            findAndHookConstructor("com.tencent.mm.pluginsdk.model.m", lpparam.classLoader,  int.class,hookMessageListenerClass,hookMessageListenerClass,String.class,String.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-function","com.tencent.mm.pluginsdk.model.m#m[int,list,list,str,str]");
                            Log.i("postmsg-param",""+param.args[0]);
                            Log.i("postmsg-param",""+param.args[1]);
                            Log.i("postmsg-param",""+param.args[2]);
                            Log.i("postmsg-param",""+param.args[3]);
                            Log.i("postmsg-param",""+param.args[4]);
                            Log.i("postmsg-result",""+param.getResult());
                        }
                    }
            );
            hookMessageListenerClass2 =lpparam.classLoader.loadClass("java.util.Map");
            findAndHookConstructor("com.tencent.mm.pluginsdk.model.m", lpparam.classLoader,  int.class,hookMessageListenerClass,hookMessageListenerClass,String.class,String.class,hookMessageListenerClass2,String.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-function","com.tencent.mm.pluginsdk.model.m#m[int,list,list,str,str,map,str]");
                            Log.i("postmsg-param",""+param.args[0]);
                            Log.i("postmsg-param",""+param.args[1]);
                            Log.i("postmsg-param",""+param.args[2]);
                            Log.i("postmsg-param",""+param.args[3]);
                            Log.i("postmsg-param",""+param.args[4]);
                            Log.i("postmsg-param",""+param.args[5]);
                            Log.i("postmsg-param",""+param.args[6]);
                            Log.i("postmsg-result",""+param.getResult());
                        }
                    }
            );
            findAndHookConstructor("com.tencent.mm.pluginsdk.model.m", lpparam.classLoader,  int.class,hookMessageListenerClass,hookMessageListenerClass,hookMessageListenerClass,String.class,String.class,hookMessageListenerClass2,String.class,String.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws JSONException {
                            Log.i("postmsg-function","com.tencent.mm.pluginsdk.model.m#m[int,list,list,list,str,str,map,str,str]");
                            Log.i("postmsg-param",""+param.args[0]);
                            Log.i("postmsg-param",""+param.args[1]);
                            Log.i("postmsg-param",""+param.args[2]);
                            Log.i("postmsg-param",""+param.args[3]);
                            Log.i("postmsg-param",""+param.args[4]);
                            Log.i("postmsg-param",""+param.args[5]);
                            Log.i("postmsg-param",""+param.args[6]);
                            Log.i("postmsg-param",""+param.args[7]);
                            Log.i("postmsg-param",""+param.args[8]);
                            Log.i("postmsg-result",""+param.getResult());
                        }
                    }
            );*/
        }




    }

    /**
     * 获取dex路径
     * @param classLoader
     * @return
     */
    public static String getDexPath(ClassLoader classLoader){
        try{
            Field field = classLoader.getClass().getSuperclass().getDeclaredField("pathList");
            field.setAccessible(true);
            Object objPathList = field.get(classLoader);
            Field elementsField = objPathList.getClass().getDeclaredField("dexElements");
            elementsField.setAccessible(true);
            Object[] elements =(Object[])elementsField.get(objPathList);
            for(Object obj : elements){
                Field fileF = obj.getClass().getDeclaredField("file");
                fileF.setAccessible(true);
                File file = (File)fileF.get(obj);
                return file.getAbsolutePath();
            }
        }catch(Exception e){
        }
        return null;
    }

    private static void hookClassInfo(String className, ClassLoader classLoader){
        //过滤系统类名前缀
        if(TextUtils.isEmpty(className)){
            return;
        }
        if(className.startsWith("android.")){
            return;
        }
        if(className.startsWith("java.")){
            return;
        }
        if(!className.startsWith("com.tencent.mm")){
            return;
        }

        if (classArray.contains(className)){
            return ;
        }
        classArray.add(className);
        //Log.i("classname",className+"---"+classLoader);
        //if (true){
        //    return ;
        //}
        //利用反射获取一个类的所有方法
        try{
            Class<?> clazz = XposedHelpers.findClassIfExists(className,classLoader);
            //这里获取类的所有方法，但是无法获取父类的方法，不过这里没必要关系父类的方法
            //如果要关心，那么需要调用getMethods方法即可

            Method[] allMethods = clazz.getDeclaredMethods();
            for(Method method : allMethods){
                Class<?>[] paramTypes = method.getParameterTypes();
                String methodName = method.getName();
                Object[] param = new Object[paramTypes.length+1];
                for(int i=0;i<paramTypes.length;i++){
                    param[i] = paramTypes[i];
                }
                String signStr = getMethodSign(method);
                if(TextUtils.isEmpty(signStr) || isFilterMethod(signStr)){
                    continue;
                }

                //开始构造Hook的方法信息
                param[paramTypes.length] = new XC_MethodHook(){
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String methodSign = getMethodSign(param);
                        if(!TextUtils.isEmpty(methodSign) && !callMethodSignSet.contains(methodSign)){
                            //这里因为会打印日志，所以会出现app的ANR情况
                            Log.i("jw", "call-->"+methodSign);
                            //这里还可以把方法的参数值打印出来，不过如果应用过大，这里会出现ANR
                            for(int i=0;i<param.args.length;i++){
                                Log.i("jw", "==>arg"+i+":"+param.args[i]);
                            }
                            callMethodSignSet.add(methodSign);
                        }
                        super.afterHookedMethod(param);
                    }
                };

                //开始进行Hook操作，注意这里有一个问题，如果一个Hook的方法数过多，会出现OOM的错误，这个是Xposed工具的问题
                if(!TextUtils.isEmpty(signStr) && !methodSignSet.contains(signStr)){
                    //这里因为会打印日志，所以会出现app的ANR情况
                    Log.i("jw", "all-->"+signStr);
                    methodSignSet.add(signStr);
                    XposedHelpers.findAndHookMethod(className, classLoader, methodName, param);
                }
            }
        }catch(Exception e){
            Log.e("fuck",""+ e.toString());
        }
    }

    /**
     * 获取方法的签名信息
     * @param param
     * @return
     */
    private static String getMethodSign(XC_MethodHook.MethodHookParam param){
        try{
            StringBuilder methodSign = new StringBuilder();
            methodSign.append(Modifier.toString(param.method.getModifiers())+" ");
            Object result = param.getResult();
            if(result == null){
                methodSign.append("void ");
            }else{
                methodSign.append(result.getClass().getCanonicalName() + " ");
            }
            methodSign.append(param.method.getDeclaringClass().getCanonicalName()+"."+param.method.getName()+"(");
            for(int i=0;i<param.args.length;i++){
                //这里有一个问题：如果方法的参数值为null,那么这里就会报错! 得想个办法如何获取到参数类型？
                if(param.args[i] == null){
                    methodSign.append("?");
                }else{
                    methodSign.append(param.args[i].getClass().getCanonicalName());
                }
                if(i<param.args.length-1){
                    methodSign.append(",");
                }
            }
            methodSign.append(")");
            return methodSign.toString();
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 获取方法的签名信息
     * public final native String xxx(java.lang.String,int) 类似于这种类型
     * @param method
     * @return
     */
    private static String getMethodSign(Method method){
        try{
            //如果这个方法是继承父类的方法，也需要做过滤
            String methodClass = method.getDeclaringClass().getCanonicalName();
            if(methodClass.startsWith("android.") || methodClass.startsWith("java.")){
                return null;
            }
            StringBuilder methodSign = new StringBuilder();
            Class<?>[] paramTypes = method.getParameterTypes();
            Class<?> returnTypes = method.getReturnType();
            methodSign.append(Modifier.toString(method.getModifiers()) + " ");
            methodSign.append(returnTypes.getCanonicalName() + " ");
            methodSign.append(methodClass+"."+method.getName()+"(");
            for(int i=0;i<paramTypes.length;i++){
                methodSign.append(paramTypes[i].getCanonicalName());
                if(i<paramTypes.length-1){
                    methodSign.append(",");
                }
            }
            methodSign.append(")");
            return methodSign.toString();
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 过滤Object对象自带的几个方法，这些方法可以不用做处理
     * @param methodSign
     * @return
     */
    private static boolean isFilterMethod(String methodSign){
        if("public final void java.lang.Object.wait()".equals(methodSign)){
            return true;
        }
        if("public final void java.lang.Object.wait(long,int)".equals(methodSign)){
            return true;
        }
        if("public final native java.lang.Object.wait(long)".equals(methodSign)){
            return true;
        }
        if("public boolean java.lang.Object.equals(java.lang.Object)".equals(methodSign)){
            return true;
        }
        if("public java.lang.String java.lang.Object.toString()".equals(methodSign)){
            return true;
        }
        if("public native int java.lang.Object.hashCode()".equals(methodSign)){
            return true;
        }
        if("public final native java.lang.Class java.lang.Object.getClass()".equals(methodSign)){
            return true;
        }
        if("public final native void java.lang.Object.notify()".equals(methodSign)){
            return true;
        }
        if("public final native void java.lang.Object.notifyAll()".equals(methodSign)){
            return true;
        }
        return false;
    }

    /**
     * 跨进程读取数据，会显示失败的，这个方法是无效的，因为methodSignSet数据可能跨进程读取失败
     * @return
     */
    @SuppressLint("SdCardPath")
    public static boolean dumpAllMethodInfo(){
        Log.i("jw", "all method size:"+methodSignSet.size());
        if(methodSignSet.size() == 0){
            return false;
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try{
            fw = new FileWriter("/sdcard/"+FILTER_PKGNAME+"_allmethod.txt");
            bw = new BufferedWriter(fw);
            for(String methodStr : methodSignSet){
                bw.write(methodStr);
                bw.newLine();
            }
            return true;
        }catch(Exception e){
            Log.i("jw", "dump all method error:"+Log.getStackTraceString(e));
            return false;
        }finally{
            try{
                if(fw != null){
                    fw.close();
                }
                if(bw != null){
                    bw.close();
                }
            }catch(Exception e){
            }
        }
    }

    /**
     * 跨进程读取数据失败
     * @return
     */
    @SuppressLint("SdCardPath")
    public static boolean dumpCallMethodInfo(){
        Log.i("jw", "call method size:"+callMethodSignSet.size());
        if(callMethodSignSet.size() == 0){
            return false;
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try{
            fw = new FileWriter("/sdcard/"+FILTER_PKGNAME+"_callmethod.txt");
            bw = new BufferedWriter(fw);
            for(String methodStr : callMethodSignSet){
                bw.write(methodStr);
                bw.newLine();
            }
            return true;
        }catch(Exception e){
            Log.i("jw", "dump call method error:"+Log.getStackTraceString(e));
            return false;
        }finally{
            try{
                if(fw != null){
                    fw.close();
                }
                if(bw != null){
                    bw.close();
                }
            }catch(Exception e){
            }
        }
    }

    public void postmsgbeta(final ClassLoader test,final String t,final String c){
        try {
            byte[] data = (t +"-----"+c).getBytes();
            InetAddress address = InetAddress.getByName("127.0.0.1");
            int port = 14895;
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            client.send(packet);
            Log.i("udp client push",t +"-----"+c);
        }catch (IOException e){
            Log.e("udp ",e.toString());

        }

    }
    public void postmsg(final ClassLoader test,final String t,final String c){
        requestCaller = callStaticMethod(findClass("com.tencent.mm.z.au", globalloader), "Dv");
        Object luckyMoneyRequest = newInstance(findClass("com.tencent.mm.modelmulti.i", globalloader),
                t,c,1,0,null);
        callMethod(requestCaller, "a", luckyMoneyRequest, 0);

    }

    public  void initudp(){
        //初始化设置Socket
        try {
            byte[] data = "initwechatbot".getBytes();
            InetAddress address = InetAddress.getByName("127.0.0.1");
            int port = 14895;
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            client.send(packet);
            isInit = true;
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.i("udp client error",ex.toString());
        }


    }

}
