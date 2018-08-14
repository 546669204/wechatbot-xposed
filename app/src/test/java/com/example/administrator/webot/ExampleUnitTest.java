package com.example.administrator.webot;

import android.provider.Settings;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.*;

import com.example.administrator.webot.MySocketServiceUDP;
import com.example.administrator.webot.MySocketClientUDP;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;

import okio.ByteString;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        try {
            if ("init".equals("initwechatbot".substring(0,4))){
                System.out.printf("1");
            }else{
                System.out.printf("2");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        InetAddress localAddress = InetAddress.getLocalHost();
        InetAddress remoteAddress = InetAddress.getByName("www.oracle.com");
        System.out.println("本机的IP地址：" + localAddress.getHostAddress());
        System.out.println("本地的主机名：" + localAddress.getHostName());
        System.out.println("甲骨文的IP地址：" + remoteAddress.getHostAddress());

        HashMap Clients = new HashMap();
        Clients.put("123",4654);
        Clients.put("121323",4654);
        Clients.put("1241233",4654);
        Clients.put("12353",4654);
        Clients.put("12e323",4654);
        Clients.put("12wqe3",4654);

        Iterator iter = Clients.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            Object val = entry.getValue();
            System.out.println("" + key);
        }


        System.out.println(Integer.parseInt("4654"));



        String msg = "<msg fromusername=\"zzxm88\" encryptusername=\"v1_cdf8a7b3da023da5307395352cd08500f7b4150729e300531a9da91d1f296ba7@stranger\" fromnickname=\"明\" content=\"我是明\" fullpy=\"ming\" shortpy=\"M\" imagestatus=\"3\" scene=\"30\" country=\"DE\" province=\"Hamburg\" city=\"\" sign=\"\" percard=\"1\" sex=\"1\" alias=\"xm________________\" weibo=\"\" weibonickname=\"\" albumflag=\"0\" albumstyle=\"0\" albumbgimgid=\"912895298764800_912895298764800\" snsflag=\"48\" snsbgimgid=\"\" snsbgobjectid=\"0\" mhash=\"1b3dad158913a69d934caaadde76a00f\" mfullhash=\"1b3dad158913a69d934caaadde76a00f\" bigheadimgurl=\"http://wx.qlogo.cn/mmhead/ver_1/OGlGw9icNc0W5md9kfaC54hRN6zsxxLicIiboXkjYTSUs6FEhvV56QLz5icRLdkbicSsZCktcxiaQKT0qZsSEk6MYwng/0\" smallheadimgurl=\"http://wx.qlogo.cn/mmhead/ver_1/OGlGw9icNc0W5md9kfaC54hRN6zsxxLicIiboXkjYTSUs6FEhvV56QLz5icRLdkbicSsZCktcxiaQKT0qZsSEk6MYwng/96\" ticket=\"v2_3c2a22e9ef60a5ea2cf518603e7617f2e768650e379ea358bbdeee3ed23244794896737c3736c73715beefbd252c74fa@stranger\" opcode=\"2\" googlecontact=\"\" qrticket=\"\" chatroomusername=\"\" sourceusername=\"\" sourcenickname=\"\"><brandlist count=\"0\" ver=\"698050163\"></brandlist></msg>";
        DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(msg.getBytes()));
        System.out.println(parse.getFirstChild().getTextContent());

        System.out.println(parse.getFirstChild().getAttributes().getNamedItem("ticket").getNodeValue());
    }
    @Test
    public void test2() throws Exception {
        System.out.println(String.format("{\"method\":\"initcontact\",\"data\":\"%s\"}","{\"isrun\":1,\"islogin\":0,\"name\":\"\",\"lastlogin\":\"\",\"runid\":1}".replace("\"","\\\"")));
    }
    public String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            //r.append(hexCode[(b >> 4) & 0xF]);
            //r.append(hexCode[(b & 0xF)]);
            r.append((b & 0xF)+((b >> 4) & 0xF)*16+" ");
        }
        return r.toString();
    }
    @Test
    public void Test3(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.println("Date获取当前日期时间"+simpleDateFormat.format(date));

        String[]  a= new String[]{"1","2"};

        System.out.println(a[1]);

    }
    public class updateNotificationProcess implements Runnable{
        private String Msg = "123456";
        public void SetMsg (String msg){
            this.Msg = msg;
        }
        public updateNotificationProcess(){

        }
        public void setMsg(String msg){
            this.Msg = msg;
        }
        public void run(){
            while (true){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){}
                System.out.println(this.Msg);
            }

        }
    }
    //高位在前，低位在后
    public static byte[] int2bytes(long num){
        byte[] result = new byte[8];
        result[0] = (byte)((num >>> 56) & 0xff);
        result[1] = (byte)((num >>> 48)& 0xff );
        result[2] = (byte)((num >>> 40) & 0xff );
        result[3] = (byte)((num >>> 32) & 0xff );
        result[4] = (byte)((num >>> 24) & 0xff);
        result[5] = (byte)((num >>> 16)& 0xff );
        result[6] = (byte)((num >>> 8) & 0xff );
        result[7] = (byte)((num >>> 0) & 0xff );
        return result;
    }

    //高位在前，低位在后
    public static int bytes2int(byte[] bytes){
        int result = 0;
        if(bytes.length == 4){
            int a = (bytes[0] & 0xff) << 24;//说明二
            int b = (bytes[1] & 0xff) << 16;
            int c = (bytes[2] & 0xff) << 8;
            int d = (bytes[3] & 0xff);
            result = a | b | c | d;
        }
        return result;
    }
}