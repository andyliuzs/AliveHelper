package org.ancode.alivelib.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import org.ancode.alivelib.config.HelperConfig;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by andyliu on 16-9-1.
 */
public class DataUtils {
    public static boolean GETING_URL = false;
    public static final String GET_DATA_KEY = "get_data_key";
    public static final int GET_DATA_WHAT = 1;
    public static final int GET_DATA_ERROR = 2;

    public static void getData(final Handler handler, final String flag) {

        if (GETING_URL == true) {
            return;
        }
        GETING_URL = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = null;
                try {

                    Map<String, String> map = Utils.getMapDevMessage();
                    String postUrl = null;
                    if (HelperConfig.USE_ANET) {
                        postUrl = HelperConfig.POST_URL_ANET6;
                        Log.v("DataUtils", "走IPV6");
                    } else {
                        postUrl = HelperConfig.POST_URL;
                        Log.v("DataUtils", "走IPV4");
                    }
                    String data = HttpHelper.post(postUrl, map, flag);
//                    String data = HttpHelper.get(HelperConfig.SERVER_URL, "utf-8", flag);

                    if (TextUtils.isEmpty(data)) {
                        sendHandler(handler, GET_DATA_WHAT, url);
                        GETING_URL = false;
                        return;
                    }
                    JSONObject jsonObj = new JSONObject(data);
                    url = jsonObj.getString("url");

//                    Log.v(TAG, "GET DATA=" + url);
                    GETING_URL = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    GETING_URL = false;
                    sendHandler(handler, GET_DATA_ERROR, e.getLocalizedMessage());
                    return;
                }
                sendHandler(handler, GET_DATA_WHAT, url);
                GETING_URL = false;
            }
        }).start();
    }


    private static void sendHandler(Handler handler, int what, String data) {
        Bundle bundle = new Bundle();
        bundle.putString(GET_DATA_KEY, data);
        Message message = new Message();
        message.setData(bundle);
        message.what = what;
        handler.sendMessage(message);
    }

}
