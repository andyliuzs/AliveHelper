package org.ancode.alivelib.ware;

import android.os.Handler;
import android.os.Message;

import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.listener.CheckCallBack;
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
        new CheckAliveWare().check(new CheckCallBack() {
            @Override
            public void onGetData(String data) {
                HelperConfig.CONTEXT.startActivity(IntentUtils.getNormalWeb(data));
            }


            @Override
            public void dataEmpty() {
                Log.v(TAG, "获取的数据为空");
            }

            @Override
            public void getDataError(String error) {
                Log.e(TAG, "获取数据错误\n"+error);
            }
        });
    }

    @Override
    public void showNotification(final AliveNotification baseNotification, long aftertime) {
        delayCall(aftertime, new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.what == SHOW_FLAG) {
                    new CheckAliveWare().check(new CheckCallBack() {
                        @Override
                        public void onGetData(String data) {
                            baseNotification.show(IntentUtils.getNotifyWeb(data));
                        }


                        @Override
                        public void dataEmpty() {
                            Log.v(TAG, "获取的数据为空");
                        }

                        @Override
                        public void getDataError(String error) {
                            Log.e(TAG, "获取数据错误\n"+error);
                        }
                    });
                }
            }
        });
    }

}
