package org.ancode.alivelib.utils;

import android.content.Intent;
import android.net.Uri;

import org.ancode.alivelib.activity.AliveHelperActivity;
import org.ancode.alivelib.config.HelperConfig;

/**
 * Created by andyliu on 16-8-26.
 */
public class IntentUtils {

    public static Intent getNormalActivity(Class<?> cls, int themeColor, boolean applyStatusColor) {
        Intent intent = null;
        if (cls != null) {
            intent = new Intent(HelperConfig.CONTEXT, cls);
        } else {
            intent = new Intent(HelperConfig.CONTEXT, AliveHelperActivity.class);
        }
        intent.putExtra(HelperConfig.THEME_COLOR_KEY, themeColor);
        intent.putExtra(HelperConfig.APP_NAME_KEY, Utils.getAppName());
        intent.putExtra(HelperConfig.APPLY_STATUS_COLOR, applyStatusColor);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent getNormalWeb(String url) {
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return it;
    }


    public static Intent getNotifyActivity(Class<?> cls, int themeColor) {
        Intent intent = null;
        if (cls != null) {
            intent = new Intent(HelperConfig.CONTEXT, cls);
        } else {
            intent = new Intent(HelperConfig.CONTEXT, AliveHelperActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(HelperConfig.THEME_COLOR_KEY, themeColor);
        intent.putExtra(HelperConfig.APP_NAME_KEY, Utils.getAppName());
        return intent;
    }

    public static Intent getNotifyWeb(String url) {
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        return it;
    }

    public static Intent getBroadCast(String action, String data, int themeColor) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(HelperConfig.DATA_KEY, data);
        intent.putExtra(HelperConfig.THEME_COLOR_KEY, themeColor);
        intent.putExtra(HelperConfig.APP_NAME_KEY, Utils.getAppName());
        return intent;
    }

}
