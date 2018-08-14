package com.example.administrator.webot;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class MySocketService {

    private static final int PORT = 9999;
    private List<Socket> mList = new ArrayList<Socket>();
    private ServerSocket server = null;
    private ExecutorService mExecutorService = null; //thread pool

    private OkHttpClient client = new OkHttpClient();
    public static final MediaType JSONType = MediaType.parse("application/json; charset=utf-8");

    public static void main(String[] args) {
        new MySocketService();
    }
    public MySocketService() {
        try {
            server = new ServerSocket(PORT);
            mExecutorService = Executors.newCachedThreadPool();  //create a thread pool
            Log.i("socket","服务器已启动...");
            Socket client = null;
            while(true) {
                client = server.accept();
                //把客户端放入客户端集合中
                mList.add(client);
                mExecutorService.execute(new Service(client)); //start a new thread to handle the connection
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    class Service implements Runnable {
        private Socket socket;
        private BufferedReader in = null;
        private String msg = "";

        public Service(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //客户端只要一连到服务器，便向客户端发送下面的信息。
                msg = "服务器地址：" +this.socket.getInetAddress() + "come toal:" +mList.size()+"（服务器发送）";
                //this.sendmsg();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                while(true) {
                    if((msg = in.readLine())!= null) {
                        //当客户端发送的信息为：exit时，关闭连接
                        if(msg.equals("exit")) {
                            System.out.println("ssssssss");
                            mList.remove(socket);
                            in.close();
                            msg = "user:" + socket.getInetAddress()
                                    + "exit total:" + mList.size();
                            socket.close();
                            this.sendmsg();
                            break;
                            //接收客户端发过来的信息msg，然后发送给客户端。
                        } else {
                            new Thread(new Runnable(){
                                public void run(){
                                    Random rand = new Random();
                                    int i = rand.nextInt(1000);
                                    try {
                                        Thread.sleep(1000+i);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    String result = null;
                                    try {
                                        result = PostData("http://openapi.tuling123.com/openapi/api/v2","{\"reqType\":0,\"perception\":{\"inputText\":{\"text\":\""+msg.split("-----")[1]+"\"}},\"userInfo\":{\"apiKey\":\"141f94237af141918cbfdeaa0323d480\",\"userId\":\""+msg.split("-----")[0]+"\"}}");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    String Content = JSON.parseObject(result).getJSONArray("results").getJSONObject(0).getJSONObject("values").getString("text");
                                    msg =  msg.split("-----")[0]+"-----"+Content+"（爱你）";
                                    sendmsg();

                                }
                            }).start();


                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 循环遍历客户端集合，给每个客户端都发送信息。
         */
        public void sendmsg() {
            Log.i("socket",msg);
            int num =mList.size();
            for (int index = 0; index < num; index ++) {
                Socket mSocket = mList.get(index);
                PrintWriter pout = null;
                try {
                    pout = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(mSocket.getOutputStream())),true);
                    pout.println(msg);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
            response = client.newCall(request).execute();
        }catch (IOException e) {

        }
        return response.body().string();
    }

}