package org.ancode.alivelib.ware;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;

import org.ancode.alivelib.R;
import org.ancode.alivelib.listener.StringCallBack;
import org.ancode.alivelib.notification.AliveNotification;
import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.utils.IntentUtils;
import org.ancode.alivelib.dialog.WebViewDialog;
import org.ancode.alivelib.utils.Log;
import org.ancode.alivelib.utils.Utils;

/**
 * Created by andyliu on 16-8-30.
 */
public class DialogAliveWare extends BaseWare {

    private static final String TAG = DialogAliveWare.class.getSimpleName();
    WebViewDialog aliveDialog = null;
    //about activity
    protected int themeColor = HelperConfig.THEME_COLOR_ID;

    public DialogAliveWare() {
    }

    /***
     * 设置themecolor
     *
     * @param themeColor
     * @return
     */
    public DialogAliveWare setThemeColor(int themeColor) {
        this.themeColor = themeColor;
        return this;
    }


    @Override
    public void check() {
        new CheckAliveWare().check(new StringCallBack() {
            @Override
            public void onResponse(String response) {
                String title = "";
                String appName = Utils.getAppName();
                if (TextUtils.isEmpty(appName)) {
                    title = String.format(HelperConfig.CONTEXT.getString(R.string.alive_activity_title), "");
                } else {
                    title = String.format(HelperConfig.CONTEXT.getString(R.string.alive_activity_title), appName);

                }

                try {
                    WebViewDialog.Builder builder = new WebViewDialog.Builder(HelperConfig.CONTEXT.getApplicationContext())
                            .setTitle(title)
                            .setPositiveButtonTextColor(themeColor)
                            .setUrl(response)
                            .setPositiveButton(HelperConfig.CONTEXT.getString(R.string.alive_close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    aliveDialog = builder.create();
//                aliveDialog.setCancelable(false);
                    aliveDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    aliveDialog.show();
                } catch (Exception e) {
                    String error = e.getLocalizedMessage();
                    if (error.contains("permission")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Log.e(TAG, "dialog无法显示，您没有手动授权'在其他应用上层显示应用'\n请手动授权");
                        } else {
                            Log.e(TAG, "dialog无法显示，您没有申请'android.permission.SYSTEM_ALERT_WINDOW'权限\n使用library中Dialog必须申请本权限");

                        }

                    } else {
                        Log.e(TAG, "显示dialog错误\n" + e.getLocalizedMessage());
                    }

                }
            }

            @Override
            public void error(String error) {
                Log.e(TAG, "获取数据错误\n" + error);
            }
        });

    }

    @Override
    public void showNotification(final AliveNotification baseNotification, long aftertime) {
        delayCall(aftertime, new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.what == SHOW_FLAG) {
                    new CheckAliveWare().check(new StringCallBack() {
                        @Override
                        public void onResponse(String response) {
                            Intent showDialogIntent = IntentUtils.getBroadCast(HelperConfig.SHOW_DIALOG_NOTIFICATION_ACTION, response, themeColor);
                            PendingIntent showDialogPi = PendingIntent.getBroadcast(HelperConfig.CONTEXT, 0, showDialogIntent, 0);
                            baseNotification.setPedingIntent(showDialogPi);
                            baseNotification.show();
                        }

                        @Override
                        public void error(String error) {
                            Log.e(TAG, "获取数据错误\n" + error);
                        }
                    });
                }
            }
        });
    }
}
