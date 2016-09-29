package org.ancode.alivelib.ware;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import org.ancode.alivelib.R;
import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.notification.AliveNotification;
import org.ancode.alivelib.utils.IntentUtils;
import org.ancode.alivelib.utils.Utils;

/**
 * Created by andyliu on 16-8-30.
 */
public class ActivityAliveWare extends BaseWare {

    //about activity
    protected int themeColor = HelperConfig.THEME_COLOR_ID;
    protected boolean applyStatusColor = true;

    public ActivityAliveWare() {

    }

    /***
     * 设置默认Activity主题颜色
     *
     * @param themeColor
     * @return
     */
    public ActivityAliveWare setThemeColor(int themeColor) {
        this.themeColor = themeColor;
        return this;
    }

    /**
     * 是否开启状态栏颜色
     * 默认开启
     *
     * @param applyStatusColor
     * @return
     */
    public ActivityAliveWare applyStatusColor(boolean applyStatusColor) {
        this.applyStatusColor = applyStatusColor;
        return this;
    }

    /**
     * 自定义Activity
     *
     * @param cls
     * @return
     */
    public ActivityAliveWare setActivity(Class<?> cls) {
        this.cls = cls;
        return this;
    }


    @Override
    public void check() {
        HelperConfig.CONTEXT.startActivity(IntentUtils.getNormalActivity(cls, themeColor, applyStatusColor));
    }

    @Override
    public void showNotification(final AliveNotification baseNotification, long aftertime) {

        delayCall(aftertime, new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.what == SHOW_FLAG) {
//                    Log.v("应用","应用名称他是  -----"+appName);
                    baseNotification.show(IntentUtils.getNotifyActivity(cls, themeColor));
                }
            }
        });
    }

    AliveNotification baseNotification = null;

    public void showNotification(long aftertime) {
        baseNotification = new AliveNotification();
        Bitmap lageIcon = Utils.getAppIcon();
        String appName = Utils.getAppName();
        String title = String.format(HelperConfig.CONTEXT.getString(R.string.alive_activity_title), appName);
        String text = String.format(HelperConfig.CONTEXT.getString(R.string.alive_notify_text), appName);
        baseNotification.setTitle(title)
                .setText(text)
                .setTickerText(title)
                .setLargeIcon(lageIcon)
                .setSmallIcon(HelperConfig.CONTEXT.getApplicationInfo().icon);
        showNotification(baseNotification, aftertime);
    }

    public void showNotification(int lageIconId, int smallIconId, long aftertime) {
        baseNotification = new AliveNotification();
        Bitmap lageIcon = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            lageIcon = Utils.drawableToBitmap(HelperConfig.CONTEXT.getDrawable(lageIconId));
        } else {
            lageIcon = Utils.drawableToBitmap(HelperConfig.CONTEXT.getResources().getDrawable(lageIconId));
        }
        String appName = Utils.getAppName();
        String title = String.format(HelperConfig.CONTEXT.getString(R.string.alive_activity_title), appName);
        String text = String.format(HelperConfig.CONTEXT.getString(R.string.alive_notify_text), appName);
        baseNotification.setTitle(title)
                .setText(text)
                .setTickerText(title)
                .setLargeIcon(lageIcon)
                .setSmallIcon(smallIconId);
        showNotification(baseNotification, aftertime);
    }


}
