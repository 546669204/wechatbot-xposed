package com.example.administrator.webot;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MySocketClientUDP {
    public static void main() throws Exception {

        /*
         * 向服务器端发送数据
         */

        // 1.定义服务器的地址，端口号，数据
        InetAddress address = InetAddress.getByName("127.0.0.1");
        int port = 14895;
        byte[] data = "用户名：admin;密码：123".getBytes();
        // 2.创建数据报，包含了发送的信息
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        // 3.创建 DatagramSocket
        DatagramSocket client = new DatagramSocket();
        // 4.向服务器端发送数据
        client.send(packet);

        /*
         * 接受服务器端响应的数据
         */
        // 1.创建数据报，用于接受服务端响应的数据
        byte[] bytes = new byte[2048];
        packet = new DatagramPacket(bytes, bytes.length);
        // 2.接收服务器响应的数据
        client.receive(packet);
        // 3.读取数据
        String reply = new String(bytes, 0, packet.getLength());
        System.out.println("我是客户端，服务器发来消息说：" + reply);

        // 关闭资源
        client.close();
    }
}
