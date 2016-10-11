package org.ancode.alivelib.config;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by andyliu on 16-8-31.
 */
public class HelperConfig {
    public static Context CONTEXT = null;
    public static boolean DEBUG = false;
    public static boolean USE_ANET = false;

    /***
     * 状态栏是否使用主题颜色
     */
    public static final String APPLY_STATUS_COLOR = "apply_status_color";
    public static int THEME_COLOR_ID = -1;

    /**
     * notification跳转dialog 广播 action
     */
    public static final String SHOW_DIALOG_NOTIFICATION_ACTION = "org.ancde.alivelib.SHOW_DIALOG_NOTIFICATION_ACTION";
    /***
     * 默认发送广播 action
     */
    public static final String BROADCAST_ACTION = "org.ancde.alivelib.DATA_BROADCAT_ACTION";
    /***
     * 接收广播发来的dataKey
     */
    public static final String DATA_KEY = "data_key";

    public static final String THEME_COLOR_KEY = "theme_color_key";
    public static final String APP_NAME_KEY = "app_name_key";

    //统计数据相关配置
    /***
     * APP存活统计频率
     */
    public static final int ALIVE_COUNT_RATE = 30 * 1000;
    /**
     * 时间相差有效值
     */
    public static final int CHECK_COUNT_DIFFER = 35 * 1000;

    /***
     * alivecount存储文件名称
     */
    public static final String ALIVE_COUNT_FILE_NAME = "alive_cout_file";
    /***
     * 应用默认警告点
     * 0.0-1.0
     */
    public static float WARNING_POINT = 0.8f;
    /***
     * 小图标id
     */
    public static int SMALL_ICON_ID = -1;

    /**
     * aliveCount上传数据频率 单位/小时
     */
    public static final int UPLOAD_ALIVE_COUNT_RATE = 1;
}
