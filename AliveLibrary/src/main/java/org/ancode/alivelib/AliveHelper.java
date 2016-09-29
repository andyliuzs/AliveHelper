package org.ancode.alivelib;

import android.app.Application;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;

import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.listener.CheckCallBack;
import org.ancode.alivelib.notification.AliveNotification;
import org.ancode.alivelib.utils.Utils;
import org.ancode.alivelib.ware.ActivityAliveWare;
import org.ancode.alivelib.ware.BroadCastAliveWare;
import org.ancode.alivelib.ware.DialogAliveWare;
import org.ancode.alivelib.ware.CheckAliveWare;
import org.ancode.alivelib.ware.WebViewWare;

/**
 * Created by andyliu on 16-8-25.
 */
public class AliveHelper {
    private static AliveHelper aliveHelper = null;

    /****
     * 初始化library
     *
     * @param context
     * @return
     */
    public static AliveHelper init(Context context) {
        if (aliveHelper == null) {
            HelperConfig.CONTEXT = context;
            aliveHelper = new AliveHelper();
        }
        return aliveHelper;
    }


    /**
     * 是否使用原网地址访问数据
     *
     * @param b
     * @return
     */
    public static AliveHelper useAnet(boolean b) {
        HelperConfig.USE_ANET = b;
        return aliveHelper;
    }


    /**
     * log开关
     *
     * @param debug
     * @return
     */
    public static AliveHelper setDebug(boolean debug) {
        HelperConfig.DEBUG = debug;
        return aliveHelper;
    }

    public static AliveHelper setThemeColor(int themeColorId) {
        HelperConfig.THEME_COLOR_ID = themeColorId;
        return aliveHelper;
    }


    /**
     * 获取防杀助手Helper
     *
     * @return AliveHelper
     */
    public static AliveHelper getHelper() {
        if (aliveHelper == null) {
            throw new NullPointerException("未初始化AliveHelper，请先在Application中初始化AliveHelper。");
        } else {
            return aliveHelper;
        }
    }

    /***
     * 释放helper
     */
    public static void killHelper() {
        if (aliveHelper != null) {
            aliveHelper = null;
        }
        if (HelperConfig.CONTEXT != null) {
            HelperConfig.CONTEXT = null;
        }
    }


    public void check(CheckCallBack cb) {
        CheckAliveWare checkAliveWare = new CheckAliveWare();
        checkAliveWare.setCheckCallBack(cb);
        checkAliveWare.check();
    }

    /**
     * 显示notification
     *
     * @param afterTime 延迟时间显示时间
     */
    public void notification(long afterTime) {
        new ActivityAliveWare().showNotification(afterTime);
    }

    /***
     * 自定义notification图标
     *
     * @param lageIconId
     * @param smallIconId
     * @param afterTime
     */
    public void notification(int lageIconId, int smallIconId, long afterTime) {
        new ActivityAliveWare().showNotification(lageIconId, smallIconId, afterTime);
    }

    /***
     * 取消方杀助手的提示
     */
    public void cancelNotification() {
        new AliveNotification().cancelAliveHelper();
    }

    public void showActivity() {
        new ActivityAliveWare().check();
    }

    public void showWeb() {
        new WebViewWare().check();
    }

    public void showDialog() {
        new DialogAliveWare().check();
    }

    /**
     * 使用默认Action发送广播
     * ACTION=HelperConfig.BROADCAST_ACTION
     */
    public void sendBroadCast() {
        new BroadCastAliveWare().check();
    }

    /**
     * 自定义Action发送广播
     */
    public void sendBroadCast(String action) {
        new BroadCastAliveWare().setBroadCastAction(action).check();
    }

}
