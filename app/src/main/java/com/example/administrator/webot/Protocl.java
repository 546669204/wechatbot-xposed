package com.example.administrator.webot;

import java.math.BigInteger;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.common.graph.*;
import com.google.common.primitives.Bytes;
class readerData{
    private byte[] reader;
    public void setReader(byte[] tmp){
        this.reader = tmp;
    }
    public byte[] getReader(){
        byte[] tmp = this.reader;
        this.reader = new byte[]{};
        return tmp;
    }
}
public class Protocl {
    private static String ConstHeader         = "{28285252_hcaiyue_top_15159898}";
    private static int ConstHeaderLength   = ConstHeader.length();
    private static int ConstSaveDataLength = 8;
    private static String ConstFooter         = "{137621_woyaoyigebaojieshula_159687}";
    private static int ConstFooterLength   = ConstFooter.length();

    public static byte[] Packet(byte[] message){
        byte[] bytes = new byte[10];
        ByteBuffer buf = ByteBuffer.allocate(102400);
        buf.put(ConstHeader.getBytes());
        buf.put(int2bytes(message.length));
        buf.put(message);
        buf.put(ConstFooter.getBytes());
        buf.flip();

        bytes = new byte[buf.limit()];
        buf.get(bytes, 0, bytes.length);
        return bytes;
    }
    public static byte[] Unpack(byte[] buffer,readerData reader){
        ByteBuffer buf = ByteBuffer.allocate(102400);
        long length = buffer.length;
        int biao = 0;
        if (length <= ConstHeaderLength+ConstSaveDataLength+ConstFooterLength) {
            return buffer;
        }
        if (Bytes.indexOf(buffer, ConstHeader.getBytes()) == -1 || Bytes.indexOf(buffer, ConstFooter.getBytes()) == -1){
            return buffer;
        }
        int start = Bytes.indexOf(buffer, ConstHeader.getBytes());
        if (start == -1) {
            return buffer;
        }
        int end =  Bytes.indexOf(Arrays.copyOfRange(buffer,start+ConstHeaderLength,buffer.length), ConstFooter.getBytes());
        if (end == -1) {
            return buffer;
        }
        end += start + ConstHeaderLength;
        biao = start + ConstHeaderLength;
        int messageLength = (int)bytes2int(Arrays.copyOfRange(buffer,biao,biao+ConstSaveDataLength));
        if (end-start-ConstHeaderLength-ConstSaveDataLength != messageLength) {
            return Arrays.copyOfRange(buffer,end+ConstFooterLength,buffer.length);
        }
        biao += ConstSaveDataLength;

        reader.setReader(Arrays.copyOfRange(buffer,biao,biao+messageLength));
        biao += messageLength + ConstFooterLength;
        if (biao == length) {
            return new byte[]{};
        }

        return Arrays.copyOfRange(buffer,biao,buffer.length);
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
    public static long bytes2int(byte[] bytes){
        long result = 0;
        if(bytes.length == 8){
            int a = (bytes[0] & 0xff) << 56;//说明二
            int b = (bytes[1] & 0xff) << 48;
            int c = (bytes[2] & 0xff) << 40;
            int d = (bytes[3] & 0xff) << 32;
            int e = (bytes[4] & 0xff) << 24;//说明二
            int f = (bytes[5] & 0xff) << 16;
            int j = (bytes[6] & 0xff) << 8;
            int h = (bytes[7] & 0xff);
            result = a | b | c | d | e | f | j | h ;
        }
        return result;
    }
}
