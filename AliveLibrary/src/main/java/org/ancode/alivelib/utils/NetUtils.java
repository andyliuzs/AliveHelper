package org.ancode.alivelib.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.ancode.alivelib.config.HelperConfig;

/**
 * 跟网络相关的工具类
 */
public class NetUtils {

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifiConnect(Context context) {
        if (context == null) {
            return false;
        }

        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }


    /***
     * 获取上网方式
     *
     * @return
     */
    public static String getNetStatus(Context context) {
        if (isWifiConnect(context)) {
            return "wifi";
        } else {
            return "3g";
        }
    }

    /**
     * 打开网络设置界面
     */
    public static void openNetworkSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

}