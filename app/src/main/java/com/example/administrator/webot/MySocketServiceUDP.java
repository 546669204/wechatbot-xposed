package com.example.administrator.webot;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


class ClientClass{
    InetAddress address;
    int port;
    public ClientClass(InetAddress address,int port){
        this.address = address;
        this.port = port;
    }
}
public class MySocketServiceUDP {
    public static DatagramSocket server = null;
    public static Map<String,String> Clients = new HashMap<String,String>();
    private OkHttpClient okhttpclient = new OkHttpClient();
    public static final MediaType JSONType = MediaType.parse("application/json; charset=utf-8");
    public  void main() {
        try {
            server =  new DatagramSocket(14895);
            while(true) {
                byte[] bytes = new byte[2048];
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                server.receive(packet);// 此方法会在接收到数据之前一直阻塞
                String msg = new String(bytes, 0, packet.getLength()); //接受消息 msg 调用
                Log.i("udp service pull",msg);
                String addressport = packet.getAddress().getHostAddress()+"|||"+packet.getPort();
                if ("init".equals(msg.substring(0,4))){
                    if (Clients.containsKey(addressport)){
                        continue;
                    }
                    Log.i("udp service","上线成功:"+msg.substring(4));
                    Random rand = new Random();
                    String key = msg.substring(4) + rand.nextInt(100);
                    while(Clients.containsValue(key)){
                        key = msg.substring(4) + rand.nextInt(100);
                    }
                    Log.i("udp aaa",addressport);
                    Clients.put(addressport,key);
                    //sendmsg(addressport,"initok");
                }
                if (msg.split("-----").length>1){
                    String result = null;
                    try {
                        String[] arr = msg.split("-----");
                        String takler = arr[0];
                        String text = arr[1];
                        takler = takler.replace("_","");
                        result = PostData("http://openapi.tuling123.com/openapi/api/v2","{\"reqType\":0,\"perception\":{\"inputText\":{\"text\":\""+text+"\"}},\"userInfo\":{\"apiKey\":\"141f94237af141918cbfdeaa0323d480\",\"userId\":\""+takler+"\"}}");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("udp json",result);
                    String Content = JSON.parseObject(result).getJSONArray("results").getJSONObject(0).getJSONObject("values").getString("text");
                    msg =  msg.split("-----")[0]+"-----"+Content+"";

                    sendmsg(addressport,msg);
                    /*if (Clients.containsKey(addressport)){
                        sendmsg(addressport,msg);
                    }*/
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
    public  void quit(){
        // 关闭资源
        server.close();
    }
    public  void sendmsg(String key,String msg){
        try {
            Log.i("udp service push",msg);
            Iterator iter = Clients.entrySet().iterator();
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
            String[] arr = key.split("\\|\\|\\|");

            byte[] data = msg.getBytes();
            InetAddress address = InetAddress.getByName("127.0.0.1");
            int port = Integer.parseInt(arr[1]);
            Log.i("udp bbb","key = " + address + " and value = " + port);
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            server.send(packet);
        }catch (IOException e){
            Log.e("udp",e.toString());
        }

    }
    public String PostData(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSONType, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = null;
        try {
            response = okhttpclient.newCall(request).execute();
        }catch (IOException e) {

        }
        return response.body().string();
    }
}

