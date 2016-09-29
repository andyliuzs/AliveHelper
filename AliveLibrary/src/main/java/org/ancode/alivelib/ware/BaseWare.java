package org.ancode.alivelib.ware;

import android.os.Handler;

import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.notification.AliveNotification;

/**
 * Created by andyliu on 16-8-30.
 */
public abstract class BaseWare {
    private static final String TAG = BaseWare.class.getSimpleName();
    protected Class<?> cls = null;


    protected static final int SHOW_FLAG = 0x01;


    /**
     * 执行检索
     */
    public abstract void check();

    public abstract void showNotification(final AliveNotification baseNotification, final long aftertime);


    /***
     * 延迟操作
     *
     * @param afterTimer
     * @param handler
     */
    protected void delayCall(final long afterTimer, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(afterTimer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(SHOW_FLAG);
            }
        }).start();
    }

}
