package org.ancode.alivelib.config;

import android.app.Application;
import android.content.Context;

/**
 * Created by andyliu on 16-8-31.
 */
public class HelperConfig {
    public static Context CONTEXT = null;
    public static boolean DEBUG = false;
    public static boolean USE_ANET = false;
    //notification跳转dialog 广播 action
    public static final String SHOW_DIALOG_NOTIFICATION_ACTION = "org.ancde.alivelib.SHOW_DIALOG_NOTIFICATION_ACTION";
    //默认发送广播 action
    public static final String BROADCAST_ACTION = "org.ancde.alivelib.DATA_BROADCAT_ACTION";
    //接收广播发来的dataKey
    public static final String DATA_KEY = "data_key";
    public static final String THEME_COLOR_KEY = "theme_color_key";
    public static final String APP_NAME_KEY = "app_name_key";
    //状态栏是否使用主题颜色
    public static final String APPLY_STATUS_COLOR = "apply_status_color";
    public static final String POST_URL = "http://192.168.2.33:3000/geturl";
    public static final String POST_URL_ANET6 = "http://192.168.2.33:3000.anet6.link/geturl";
    public static int THEME_COLOR_ID = -1;
}
