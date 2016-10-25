package org.ancode.alivelib.http;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import org.ancode.alivelib.listener.StringCallBack;
import org.ancode.alivelib.utils.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andyliu on 16-10-17.
 */
public class HttpClient {
    public static final String TAG = HttpClient.class.getSimpleName();
    static boolean cancel = false;
    public static final String HTTP_CALL_FLAG = "http_call_flag";
    public static final String DATA_IS_NULL = "data is null";

    /**
     * 查询统计结果
     *
     * @param params
     * @param flag
     * @param stringCallBack
     */
    public static void getAliveStats(final Map<String, String> params, final String flag, StringCallBack stringCallBack) {
        HttpUtils.getAliveStats(params, new StrHandler(stringCallBack), flag);
    }


    /***
     * 获取防杀指南链接
     *
     * @param params
     * @param flag
     * @param stringCallBack
     */
    public static void getUrl(final Map<String, String> params, final String flag, StringCallBack stringCallBack) {
        Map<String, String> map = new HashMap<String, String>();
        HttpUtils.getUrl(params, new StrHandler(stringCallBack), flag);
    }

    static class StrHandler extends Handler {
        protected StringCallBack callBack = null;

        public StrHandler(StringCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (cancel) {
                if (callBack != null) {

                    callBack.error("this connect is cancel");
                } else {
                    Log.e(TAG, "StringCallBack is null_image");
                }
                return;
            }
            String data = msg.getData().getString(HttpUtils.GET_DATA_KEY);
            if (!TextUtils.isEmpty(data)) {
                if (msg.what == HttpUtils.GET_DATA_ERROR) {
                    if (callBack != null) {
                        callBack.error(data);
                    } else {
                        Log.e(TAG, "StringCallBack is null_image");
                    }
                } else if (msg.what == HttpUtils.GET_DATA_WHAT) {
                    if (callBack != null) {

                        callBack.onResponse(data);
                    } else {
                        Log.e(TAG, "StringCallBack is null_image");
                    }

                }
            } else {
                if (callBack != null) {
                    callBack.error(DATA_IS_NULL);
                } else {
                    Log.e(TAG, "StringCallBack is null_image");
                }
                Log.e(TAG, "获取数据失败");
            }

        }
    }

}
