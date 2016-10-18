package org.ancode.alivelib.utils;

import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;


import org.ancode.alivelib.R;
import org.ancode.alivelib.config.HelperConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andyliu on 16-8-25.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();


    /***
     * 获取机型信息
     *
     * @return
     */
    public static Map<String, String> getProp() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("model", Build.MODEL);
        //ANDROID版本号
        // map.put("buildVersion", String.valueOf(Build.VERSION.RELEASE));
        //版本号
        map.put("version", String.valueOf(Build.DISPLAY));
        //编译版本号
//        map.put("buildVersion", String.valueOf(Build.VERSION.SDK_INT));
        Log.v(TAG, "model=" + map.get("model").toString() + ",version=" + map.get("version"));
        return map;
    }

    /**
     * 通过包名获取应用程序的名称。
     */
    public static String getAppName() {
        PackageManager pm = HelperConfig.CONTEXT.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(
                    pm.getApplicationInfo(HelperConfig.CONTEXT.getPackageName().toString(),
                            PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }






}
