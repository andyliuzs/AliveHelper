package org.ancode.alivelib.ware;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.notification.AliveNotification;
import org.ancode.alivelib.listener.CheckCallBack;
import org.ancode.alivelib.utils.IntentUtils;
import org.ancode.alivelib.utils.Log;

/**
 * Created by andyliu on 16-8-30.
 */
public class BroadCastAliveWare extends BaseWare {
    private static final String TAG = BroadCastAliveWare.class.getSimpleName();
    //about broadcast
    protected String broadcastAction = HelperConfig.BROADCAST_ACTION;

    public BroadCastAliveWare() {
    }

    public BroadCastAliveWare(String broadcastAction) {
        this.broadcastAction = broadcastAction;
    }

    /**
     * 自定义广播ACTION
     * <p/>
     * 默认ACTION=HelperConfig.BROADCAST_ACTION
     * <p/>
     *
     * @param action
     * @return
     */
    public BroadCastAliveWare setBroadCastAction(String action) {
        this.broadcastAction = action;
        return this;
    }


    @Override
    public void check() {
        new CheckAliveWare().check(new CheckCallBack() {
            @Override
            public void onGetData(String data) {
                HelperConfig.CONTEXT.sendBroadcast(IntentUtils.getBroadCast(broadcastAction, data, -1));
            }

            @Override
            public void dataEmpty() {
                Log.v(TAG, "获取的数据为空");
            }

            @Override
            public void getDataError(String error) {
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
                    new CheckAliveWare().check(new CheckCallBack() {
                        @Override
                        public void onGetData(String data) {
                            Intent broadCastIntent = IntentUtils.getBroadCast(broadcastAction, data, -1);
                            PendingIntent mpi = PendingIntent.getBroadcast(HelperConfig.CONTEXT, 0, broadCastIntent, 0);
                            baseNotification.setPedingIntent(mpi);
                            baseNotification.show();
                        }


                        @Override
                        public void dataEmpty() {
                            Log.v(TAG, "获取的数据为空");
                        }

                        @Override
                        public void getDataError(String error) {
                            Log.e(TAG, "获取数据错误\n" + error);
                        }
                    });
                }
            }
        });
    }

}
