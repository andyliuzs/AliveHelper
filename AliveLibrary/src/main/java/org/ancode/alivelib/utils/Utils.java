package org.ancode.alivelib.utils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
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


    public static Map<String, String> getMapDevMessage() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("model", Build.MODEL);
        map.put("buildVersion", String.valueOf(Build.VERSION.SDK_INT));
        Log.v(TAG, "model=" + map.get("model") + ",buildVersion=" + map.get("buildVersion"));
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

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }

    public static Bitmap getAppIcon() {
        PackageManager pm = HelperConfig.CONTEXT.getPackageManager();
        Drawable icon = null;
        try {
            String packageName = HelperConfig.CONTEXT.getPackageName().toString();
            icon = pm.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA).loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return drawableToBitmap(icon);
    }


    public static int getThemeColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = HelperConfig.CONTEXT.obtainStyledAttributes(HelperConfig.CONTEXT.getApplicationInfo().theme, new int[]{R.attr.colorPrimary});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

}
