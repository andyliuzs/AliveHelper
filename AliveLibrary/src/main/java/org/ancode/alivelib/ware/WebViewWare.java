package org.ancode.alivelib.ware;

import android.os.Handler;
import android.os.Message;

import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.listener.StringCallBack;
import org.ancode.alivelib.notification.AliveNotification;
import org.ancode.alivelib.utils.IntentUtils;
import org.ancode.alivelib.utils.Log;

/**
 * Created by andyliu on 16-8-30.
 */
public class WebViewWare extends BaseWare {
    private static final String TAG = WebViewWare.class.getSimpleName();

    public WebViewWare() {
    }

    @Override
    public void check() {
        new CheckAliveWare().check(new StringCallBack() {
            @Override
            public void onResponse(String response) {
                HelperConfig.CONTEXT.startActivity(IntentUtils.getNormalWeb(response));
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
                            baseNotification.show(IntentUtils.getNotifyWeb(response));
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
