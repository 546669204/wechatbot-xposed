package com.example.administrator.webot;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import junit.framework.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookLoader implements IXposedHookLoadPackage {

    private static String MODULE_PATH = null;


    private void hook_method(String className, ClassLoader classLoader, String methodName,
                             Object... parameterTypesAndCallback){
        try {
            XposedHelpers.findAndHookMethod(className, classLoader, methodName, parameterTypesAndCallback);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    private void hook_methods(String className, String methodName, XC_MethodHook xmh){
        try {
            Class<?> clazz = Class.forName(className);
            for (Method method : clazz.getDeclaredMethods())
                if (method.getName().equals(methodName)
                        && !Modifier.isAbstract(method.getModifiers())
                        && Modifier.isPublic(method.getModifiers())) {
                    XposedBridge.hookMethod(method, xmh);
                }
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam param) throws Throwable{
        if (Config.isDebug){
            //通过反射实现热更新
            final String packageName = TestHook.class.getPackage().getName();
            String filePath = String.format("/data/app/%s-%s.apk", packageName, 1);
            if (!new File(filePath).exists()) {
                filePath = String.format("/data/app/%s-%s.apk", packageName, 2);
                if (!new File(filePath).exists()) {
                    filePath = String.format("/data/app/%s-%s/base.apk", packageName, 1);
                    if (!new File(filePath).exists()) {
                        filePath = String.format("/data/app/%s-%s/base.apk", packageName, 2);
                        if (!new File(filePath).exists()) {
                            XposedBridge.log("Error:在/data/app找不到APK文件" + packageName);
                            return;
                        }
                    }
                }
            }
            final PathClassLoader pathClassLoader = new PathClassLoader(filePath, ClassLoader.getSystemClassLoader());
            final Class<?> aClass = Class.forName(packageName + "." + TestHook.class.getSimpleName(), true, pathClassLoader);
            final Method aClassMethod = aClass.getMethod("handleLoadPackage", XC_LoadPackage.LoadPackageParam.class);
            aClassMethod.invoke(aClass.newInstance(), param);

            //xLog("pkg:"+param.packageName);
        }else {
//			Module.
        }



    }


    private void xLog(String content) {
        XposedBridge.log("*******************************************************************************************************************************");
        XposedBridge.log(content);
        XposedBridge.log("----------------------------------------------------------------------------------------------------------------------");
    }
}