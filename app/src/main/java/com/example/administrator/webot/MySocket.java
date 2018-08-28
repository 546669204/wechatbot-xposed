package com.example.administrator.webot;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MySocket {
    NotificationManager nm;
    Notification notification;

    updateNotificationProcess unp;
    private PrintWriter printWriter;
    private InputStream in;
    private byte[] receiveMsg = new byte[0];
    private Socket socketclient;
    private int socketError = 0;
    private ArrayList<byte[]> SendData = new ArrayList<byte[]>();
    private OkHttpClient okhttpclient = new OkHttpClient();
    public static final MediaType JSONType = MediaType.parse("application/json; charset=utf-8");

    public static DatagramSocket serverudp = null;
    public static Map<String, String> udpClients = new HashMap<String, String>();

    public String LastAddress;

    private String UserName = "";

    class ClientClass {
        InetAddress address;
        int port;

        public ClientClass(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }
    }

    public class updateNotificationProcess implements Runnable {
        private String Msg = "123456";
        private boolean MasterStatus = false;
        private int PushNum = 0;
        private int PullNum = 0;

        public void SetMsg(String msg) {
            this.Msg = msg;
        }

        public updateNotificationProcess() {

        }

        public void setMsg(String msg) {
            this.Msg = msg;
        }

        public void setMasterStatus(boolean status) {
            this.MasterStatus = status;
        }

        public void addPush() {
            this.PushNum++;
        }

        public void addPull() {
            this.PullNum++;
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String ntext = "";
                ntext += (this.MasterStatus ? "Master连接成功" : "Master连接失败") + "    " + (udpClients.size() > 0 ? "微信连接成功" : "微信连接失败") + "\n";
                ntext += "接受消息：" + this.PullNum + "条\n";
                ntext += "发送消息：" + this.PushNum + "条\n";
                ntext += "时间：" + simpleDateFormat.format(date);


                notification.contentView.setCharSequence(R.id.textView5, "setText", ntext);
                nm.notify(19172439, notification);
            }

        }
    }

    public class receiveProcessClass implements Runnable {
        private String Msg;

        public void SetMsg(String msg) {
            this.Msg = msg;
        }

        public void run() {
            System.out.println(this.Msg);

            JSONObject json = JSON.parseObject(this.Msg);
            if (json == null) {
                return;
            }
            String Data = json.getString("data");
            //json.getIntValue("status");
            //json.getString("id");
            //json.getString("to");
            String Method = json.getString("method");
            if (Method.equals("sendtextmsg")) {
                JSONObject d = JSON.parseObject(Data);
                WechatSend("", d.getString("to") + "-----" + d.getString("content"));
                json.put("data", "{\"status\":1}");
                json.put("method", "msgreturn");
                Sned(json.toJSONString().getBytes());
            }
            if (Method.equals("islogin")) {
                json.put("method", "msgreturn");
                json.put("data", "{\"isrun\":1,\"islogin\":1,\"name\":\"\",\"lastlogin\":\"\",\"runid\":\"" + UserName + "\"}");
                Sned(json.toJSONString().getBytes());
            }
        }
    }

    public class serviceudpProcess implements Runnable {
        public void run() {
            try {
                serverudp = new DatagramSocket(14895);
                while (true) {
                    byte[] bytes = new byte[2048];
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                    serverudp.receive(packet);// 此方法会在接收到数据之前一直阻塞
                    String msg = new String(bytes, 0, packet.getLength()); //接受消息 msg 调用
                    Log.i("udp service pull", msg);

                    String addressport = packet.getAddress().getHostAddress() + "|||" + packet.getPort();
                    LastAddress = addressport;
                    if ("init".equals(msg.substring(0, 4))) {
                        if (udpClients.containsKey(addressport)) {
                            continue;
                        }
                        Log.i("udp service", "上线成功:" + msg.substring(4));
                        Random rand = new Random();
                        String key = msg.substring(4) + rand.nextInt(100);
                        while (udpClients.containsValue(key)) {
                            key = msg.substring(4) + rand.nextInt(100);
                        }
                        Log.i("udp aaa", addressport);
                        udpClients.put(addressport, key);
                        //sendmsg(addressport,"initok");
                    }
                    if (msg.split("-----").length > 1) {
                        JSONObject json = new JSONObject();
                        json.put("data", "{\"type\":\"text\",\"content\":\"" + msg.split("-----")[1] + "\",\"takler\":\"" + msg.split("-----")[0] + "\"}");
                        json.put("method", "msgprocess");
                        json.put("to", "1");
                        Sned(json.toJSONString().getBytes());
                        unp.addPull();
                    }
                }
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        try {
            in.close();
            socketclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void main() {
        try {

            nm = (NotificationManager) MyApplication.getContextObject().getSystemService(NOTIFICATION_SERVICE);
            notification = new Notification(R.drawable.example_picture, "微信机器人", System.currentTimeMillis());
            notification.contentView = new RemoteViews(MyApplication.getContextObject().getPackageName(), R.layout.notification);
            notification.contentView.setCharSequence(R.id.textView5, "setText", "416798654678965");
            nm.notify(19172439, notification);

            unp = new updateNotificationProcess();
            Thread unpthread = new Thread(unp);
            unpthread.start();

            SharedPreferences sp = MyApplication.getContextObject().getSharedPreferences("config", Context.MODE_PRIVATE);
            while (socketclient == null) {
                try {
                    Thread.sleep(1000);
                    socketclient = new Socket(sp.getString("host", "127.0.0.1"), sp.getInt("port", 188));
                    socketclient.setSoTimeout(5 * 1000);
                } catch (Exception e) {
                    //e.printStackTrace();
                    //socketclient.close();
                    Log.e("error", e.toString());
                    //socketclient = null;
                }
            }
            //socketclient.setKeepAlive(true);
            /*new Thread(new Runnable(){
                @Override
                public void run() {
                    while(true){
                        try {
                            Thread.sleep(2000);

                            if (isServerClose(socketclient)){
                                unp.setMasterStatus(false);
                                SharedPreferences sp =  MyApplication.getContextObject().getSharedPreferences("config", Context.MODE_PRIVATE);
                                socketclient = new Socket( sp.getString("host","127.0.0.1"), sp.getInt("port",188));
                                in = socketclient.getInputStream();
                                unp.setMasterStatus(true);
                                Sned("{\"method\":\"init\",\"data\":\"wechat\"}".getBytes());
                                Sned(("{\"method\":\"initcontact\",\"data\":\""+String.format("%s",openSQLite.getRContentJson()).replace("\"","\\\"")+"\"}").getBytes());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();*/
            unp.setMasterStatus(true);
            //socketclient.setSoTimeout(60000);
            in = socketclient.getInputStream();
            //init
            String[] sql = openSQLite.getRContentJson();

            try {
                UserName = sql[0];
            } catch (Exception e) {
                Log.e("error-266", e.toString());
            }

            if (UserName != "") {
                Sned(String.format("{\"method\":\"init\",\"data\":\"wechat|%s\"}", UserName).getBytes());
                Sned(("{\"method\":\"initcontact\",\"data\":\"" + String.format("%s", sql[1]).replace("\"", "\\\"") + "\"}").getBytes());
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(10000);
                            Sned("HeartBoom".getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            serviceudpProcess myserviceudpProcess = new serviceudpProcess();
            Thread thread = new Thread(myserviceudpProcess);
            thread.start();
            //初始化发送线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(50);
                            byte[] tmp;
                            synchronized (SendData) {
                                if (SendData.size() == 0) {
                                    continue;
                                }
                                tmp = SendData.get(0);
                                SendData.remove(0);
                            }

                            OutputStream os = socketclient.getOutputStream();
                            os.write(tmp);
                            os.flush();
                            //os.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            //启动接收线程
            receiveMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rebotSocket() {
        try {
            if (socketclient != null) {
                socketclient.close();
            }
            socketclient = null;
            unp.setMasterStatus(false);
            SharedPreferences sp = MyApplication.getContextObject().getSharedPreferences("config", Context.MODE_PRIVATE);
            socketclient = new Socket(sp.getString("host", "127.0.0.1"), sp.getInt("port", 188));
            socketclient.setSoTimeout(5 * 1000);
            in = socketclient.getInputStream();
            unp.setMasterStatus(true);
            String[] sql = openSQLite.getRContentJson();
            Sned(String.format("{\"method\":\"init\",\"data\":\"wechat|%s\"}", sql[0]).getBytes());
            Sned(("{\"method\":\"initcontact\",\"data\":\"" + String.format("%s", sql[1]).replace("\"", "\\\"") + "\"}").getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean Sned(byte[] data) {
        try {
            synchronized (SendData) {
                SendData.add(Protocl.Packet(data));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void receiveMsg() {

        byte[] buff = new byte[1024];
        int size;
        readerData r = new readerData();
        while (true) {
            try {
                if (!socketclient.isClosed() && socketclient.isConnected() && !socketclient.isInputShutdown()) {
                    in = socketclient.getInputStream();

                    if ((size = in.read(buff)) != -1) {
                        receiveMsg = byteMergerAll(receiveMsg, Arrays.copyOfRange(buff, 0, size));
                        receiveMsg = Protocl.Unpack(receiveMsg, r);
                        String tmp = new String(r.getReader());
                        if (tmp.length() > 0) {
                            receiveProcessClass myThread = new receiveProcessClass();
                            myThread.SetMsg(tmp);
                            Thread thread = new Thread(myThread);
                            thread.start();
                        }
                    } else {
                        in.close();
                    }
                } else {
                    if (isServerClose(socketclient)) {
                        rebotSocket();
                        Thread.sleep(3000);
                    }
                }
            } catch (SocketTimeoutException ste) {


            } catch (Exception e) {
                Log.e("aaaddd", e.toString());
                //rebotSocket();

                if (isServerClose(socketclient)) {
                    try {
                        in.close();
                        socketclient.close();
                        Thread.sleep(3000);
                    } catch (Exception e23) {
                        Log.e("aaaddd", e23.toString());
                    }
                    rebotSocket();
                }
            }
        }

    }

    private static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    private Boolean WechatSend(String to, String msg) {
        udpsendmsg(to, msg);
        unp.addPush();
        return true;
    }

    public void udpsendmsg(String key, String msg) {
        try {
            Log.i("udp service push", msg);
            Iterator iter = udpClients.entrySet().iterator();
            /*while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    Object val = entry.getValue();

                    String[] arr = key.split("\\|\\|\\|");

                    byte[] data = msg.getBytes();
                    InetAddress address = InetAddress.getByName("127.0.0.1");
                    int port = Integer.parseInt(arr[1]);
                Log.i("udp bbb","key = " + address + " and value = " + port);
                    DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                    server.send(packet);
            }*/
            if (key == "") {
                key = LastAddress;
            }
            String[] arr = key.split("\\|\\|\\|");

            byte[] data = msg.getBytes();
            InetAddress address = InetAddress.getByName("127.0.0.1");
            int port = Integer.parseInt(arr[1]);
            Log.i("udp bbb", "key = " + address + " and value = " + port);
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            serverudp.send(packet);
        } catch (IOException e) {
            Log.e("udp", e.toString());
            //udpClients.remove(key);
        }

    }

    public static Boolean isServerClose(Socket socket) {
        try {
            socket.sendUrgentData(0xff);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        } catch (Exception se) {
            return true;
        }
    }
}
