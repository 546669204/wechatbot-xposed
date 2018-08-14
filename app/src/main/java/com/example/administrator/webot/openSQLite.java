package com.example.administrator.webot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONWriter;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.helper.StringUtil;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class openSQLite {
    public static final String WX_ROOT_PATH = "/data/data/com.tencent.mm/";
    private static final String WX_SP_UIN_PATH = WX_ROOT_PATH + "shared_prefs/auth_info_key_prefs.xml";
    private static String mCurrApkPath = "/data/data/" + MyApplication.getContextObject().getPackageName() + "/";
    private static final String COPY_WX_DATA_DB = "wx_data.db";

    private static String mCurrWxUin;
    private static String mDbPassword;
    public static String[] getRContentJson(){
        execRootCmd("chmod -R 777 " + WX_ROOT_PATH);
        //execRootCmd("chmod 777 -R " + WX_SP_UIN_PATH);
        ArrayList<String> imei = getWholeImei();
        initCurrWxUin();
        copyFile(WX_ROOT_PATH + "MicroMsg/" + md5("mm" + mCurrWxUin) + "/EnMicroMsg.db",mCurrApkPath + COPY_WX_DATA_DB);
        Iterator it = imei.iterator();        //根据传入的集合(旧集合)获取迭代器
        while(it.hasNext()){          //遍历老集合
            Object obj = it.next();       //记录每一个元素
            initDbPassword((String)obj,mCurrWxUin);
            String tmp = "";
            String username  = "";
            try {
                tmp  = openWxDb(new File(mCurrApkPath+COPY_WX_DATA_DB));
                username = getUserName(new File(mCurrApkPath+COPY_WX_DATA_DB));
            }catch (Exception e){
                continue;
            }
            if (tmp.equals("") || username.equals("")){
                continue;
            }
            return new String[]{username,tmp};
        }
        return new String[]{};
    }
    /**
     * 执行linux指令
     *
     * @param paramString
     */
    public static void execRootCmd(String paramString) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            Object localObject = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream((OutputStream) localObject);
            String str = String.valueOf(paramString);
            localObject = str + "\n";
            localDataOutputStream.writeBytes((String) localObject);
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();
            localProcess.waitFor();
            localObject = localProcess.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("aaaddd",e.toString());
        }
    }
    /**
     * 获取手机的imei码
     *
     * @return
     */
    private static ArrayList<String> getWholeImei() {
        String imeiStr = null,imeiStr1= null,imeiStr2= null,imeiStr3=null,imeiStr4= null;
        try {
            imeiStr = ((TelephonyManager) MyApplication.getContextObject().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            imeiStr1 = ((TelephonyManager) MyApplication.getContextObject().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(TelephonyManager.PHONE_TYPE_NONE);
            imeiStr2 = ((TelephonyManager) MyApplication.getContextObject().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(TelephonyManager.PHONE_TYPE_GSM);
            imeiStr3 = ((TelephonyManager) MyApplication.getContextObject().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(TelephonyManager.PHONE_TYPE_CDMA);
            imeiStr4 = ((TelephonyManager) MyApplication.getContextObject().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(TelephonyManager.PHONE_TYPE_SIP);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        ArrayList<String> a = new ArrayList<String>();
        a.add(imeiStr);
        a.add(imeiStr1);
        a.add(imeiStr2);
        a.add(imeiStr3);
        a.add(imeiStr4);
        ArrayList newList = new ArrayList<String>();     //创建新集合
        Iterator it = a.iterator();        //根据传入的集合(旧集合)获取迭代器
        while(it.hasNext()){          //遍历老集合
            Object obj = it.next();       //记录每一个元素
            if(!newList.contains(obj)){      //如果新集合中不包含旧集合中的元素
                newList.add(obj);       //将元素添加
            }
        }
        return newList;
    }

    /**
     * 获取微信的uid
     * 微信的uid存储在SharedPreferences里面
     * 存储位置\data\data\com.tencent.mm\shared_prefs\auth_info_key_prefs.xml
     */
    private static void initCurrWxUin() {
        mCurrWxUin = null;
        File file = new File(WX_SP_UIN_PATH);
        try {
            FileInputStream in = new FileInputStream(file);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(in);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            for (Element element : elements) {
                if ("_auth_uin".equals(element.attributeValue("name"))) {
                    mCurrWxUin = element.attributeValue("value");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aaaddd","获取微信uid失败，请检查auth_info_key_prefs文件权限"+ e.toString());
        }
    }
    /**
     * 根据imei和uin生成的md5码，获取数据库的密码（去前七位的小写字母）
     *
     * @param imei
     * @param uin
     * @return
     */
    private static void initDbPassword(String imei, String uin) {
        if (TextUtils.isEmpty(imei) || TextUtils.isEmpty(uin)) {
            //LogUtil.log("初始化数据库密码失败：imei或uid为空");
            return;
        }
        String md5 = md5(imei + uin);
        String password = md5.substring(0, 7).toLowerCase();
        mDbPassword = password;
    }
    /**
     * md5加密
     *
     * @param content
     * @return
     */
    private static String md5(String content) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(content.getBytes("UTF-8"));
            byte[] encryption = md5.digest();//加密
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    sb.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    sb.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteRead = 0;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
    }
    /**
     * 连接数据库
     *
     * @param dbFile
     */
    private static String openWxDb(File dbFile) throws Exception {
        Context context = MyApplication.getContextObject();
        SQLiteDatabase.loadLibs(context);
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }
            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;"); //兼容2.0的数据库
            }
        };

            //打开数据库连接
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, mDbPassword, null, hook);
            //查询所有联系人（verifyFlag!=0:公众号等类型，群里面非好友的类型为4，未知类型2）
            Cursor c1 = db.rawQuery("select username,alias,nickname from rcontact where verifyFlag = 0 and  type == 3", null);
            JSONArray resultSet     = new JSONArray();
            while (c1.moveToNext()) {
                int totalColumn = c1.getColumnCount();
                JSONObject rowObject = new JSONObject();
                for( int i=0 ;  i< totalColumn ; i++ ){
                    if( c1.getColumnName(i) != null ){
                        try{
                            if( c1.getString(i) != null ){
                                Log.d("TAG_NAME", c1.getString(i) );
                                rowObject.put(c1.getColumnName(i) ,  c1.getString(i) );
                            }else{
                                rowObject.put( c1.getColumnName(i) ,  "" );
                            }
                        }catch( Exception e ){
                            Log.d("TAG_NAME", e.getMessage()  );
                        }
                    }
                }
                resultSet.add(rowObject);
            }
            c1.close();
            db.close();
            return resultSet.toJSONString();

    }
    private static String getUserName(File dbFile) throws Exception {
        String username = "";
        Context context = MyApplication.getContextObject();
        SQLiteDatabase.loadLibs(context);
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }
            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;"); //兼容2.0的数据库
            }
        };

        //打开数据库连接
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, mDbPassword, null, hook);
        //查询所有联系人（verifyFlag!=0:公众号等类型，群里面非好友的类型为4，未知类型2）
        Cursor c1 = db.rawQuery("select username from rcontact where verifyFlag = 0 and  type == 1", null);

        c1.moveToFirst();
        username = c1.getString(0);

        c1.close();
        db.close();
        return username;

    }
}

