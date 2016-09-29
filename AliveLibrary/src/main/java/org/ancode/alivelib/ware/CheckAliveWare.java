package org.ancode.alivelib.ware;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import org.ancode.alivelib.listener.CheckCallBack;
import org.ancode.alivelib.notification.AliveNotification;
import org.ancode.alivelib.utils.DataUtils;
import org.ancode.alivelib.utils.HttpHelper;
import org.ancode.alivelib.utils.Log;

/**
 * Created by andyliu on 16-8-30.
 */
public class CheckAliveWare extends BaseWare {

    private static final String TAG = CheckAliveWare.class.getSimpleName();
    boolean cancel = false;
    protected CheckCallBack checkCallBack = null;
    public static final String HTTP_CALL_FLAG = "http_call_flag";

    public CheckAliveWare() {
    }


    public void cancel() {
        cancel = true;
        HttpHelper.cancelAll();
    }

    private void getData(final Handler handler, final String flag) {
        DataUtils.getData(handler, flag);
    }


    Handler getDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (cancel) {
                if (checkCallBack != null) {

                    checkCallBack.getDataError("this connect is cancel");
                } else {
                    Log.e(TAG, "checkCallBack is null_image");
                }
                return;
            }
            String data = msg.getData().getString(DataUtils.GET_DATA_KEY);
            if (!TextUtils.isEmpty(data)) {
                if (msg.what == DataUtils.GET_DATA_ERROR) {
                    if (checkCallBack != null) {
                        checkCallBack.getDataError(data);
                    } else {
                        Log.e(TAG, "checkCallBack is null_image");
                    }
                } else if (msg.what == DataUtils.GET_DATA_WHAT) {
                    if (checkCallBack != null) {

                        checkCallBack.onGetData(data);
                    } else {
                        Log.e(TAG, "checkCallBack is null_image");
                    }

                }
            } else {
                if (checkCallBack != null) {
                    checkCallBack.dataEmpty();
                } else {
                    Log.e(TAG, "checkCallBack is null_image");
                }
                Log.e(TAG, "获取数据失败");
            }

        }
    };


    public CheckAliveWare setCheckCallBack(CheckCallBack checkCallBack) {
        this.checkCallBack = checkCallBack;
        return this;
    }


    /****
     * <p>
     * 调用本方法之前必须调用setCheckCallBack(CheckCallBack checkCallBack)
     * </p>
     */
    @Override
    public void check() {
        if (checkCallBack == null) {
            throw new RuntimeException("checkCallBack is null_image");
        } else {
            getData(getDataHandler, HTTP_CALL_FLAG);
        }
    }

    public CheckAliveWare check(CheckCallBack checkCallBack) {
        this.checkCallBack = checkCallBack;
        getData(getDataHandler, HTTP_CALL_FLAG);
        return this;
    }

    @Override
    public void showNotification(AliveNotification baseNotification, long aftertime) {
        Log.v(TAG, "直接获取数据未提供notification");
    }

}
