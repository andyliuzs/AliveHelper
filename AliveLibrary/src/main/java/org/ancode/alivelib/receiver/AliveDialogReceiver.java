package org.ancode.alivelib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.AliveHelper;

/**
 * Created by andyliu on 16-8-31.
 */
public class AliveDialogReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(HelperConfig.SHOW_DIALOG_NOTIFICATION_ACTION)) {
            int themeColor = -1;
            String appName = null;

            if (themeColor == -1)
                themeColor = intent.getIntExtra(HelperConfig.THEME_COLOR_KEY, -1);

            if (TextUtils.isEmpty(appName))
                appName = intent.getStringExtra(HelperConfig.APP_NAME_KEY);

//            AliveHelper.getHelper()
//                    .showAsDialog()
//                    .setThemeColor(themeColor)
//                    .check();

            AliveHelper.getHelper()
                    .showDialog();
        }


    }
}
