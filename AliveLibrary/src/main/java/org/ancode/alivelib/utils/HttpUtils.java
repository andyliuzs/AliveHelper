package org.ancode.alivelib.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.config.HttpUrlConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by andyliu on 16-9-1.
 */
public class HttpUtils {
    public static final String TAG = HttpUtils.class.getSimpleName();
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

                    Map<String, String> map = Utils.getProp();
                    String postUrl = null;
                    if (HelperConfig.USE_ANET) {
                        postUrl = HttpUrlConfig.POST_URL_ANET6;
                        Log.v("HttpUtils", "走IPV6");
                    } else {
                        postUrl = HttpUrlConfig.POST_URL;
                        Log.v("HttpUtils", "走IPV4");
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


    public static boolean uploadAliveStats(long beginTime, long endTime) {
        String packageName = HelperConfig.CONTEXT.getPackageName().toString();
        JSONObject info = null;
        try {
            info = new JSONObject(AliveSPUtils.getInstance().getASUploadInfo());
        } catch (JSONException e) {
            Log.e(TAG, "用户设置的info解析出错");
            e.printStackTrace();
            return false;
        }

        JSONObject uploadJson = new JSONObject();
        JSONObject statObject = new JSONObject();
        //统计数据
        List<String> data = AliveStatsUtils.getAliveStatsResult();

        JSONArray dataArray = new JSONArray(data);
        try {
            statObject.put("type", "alive");
            statObject.put("begin_time", beginTime);
            statObject.put("end_time", endTime);
            statObject.putOpt("data", dataArray);
        } catch (JSONException e) {
            Log.e(TAG, "上传统计数据,参数初始化错误 'statObject'错误");
            e.printStackTrace();
            return false;
        }
        try {
            uploadJson.put("app", packageName);
            uploadJson.put("info", info);
            uploadJson.putOpt("stat", statObject);
        } catch (JSONException e) {
            Log.e(TAG, "上传统计数据,参数初始化错误 'uploadJson'错误");
            e.printStackTrace();
            return false;
        }
        String response = HttpHelper.postJson(HttpUrlConfig.ALIVE_STATS_POST_URL, uploadJson.toString(), "uploadStatsTime");

        Log.v(TAG, "uploadStatsTime response= " + response);
        if (TextUtils.isEmpty(response)) {
            Log.e(TAG, "response is null");
            return false;
        } else {
            JSONObject jsonObject = null;
            String result = null;
            try {
                jsonObject = new JSONObject(response);
                result = jsonObject.get("result").toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result == null) {
                return false;
            } else {
                if (result.equals("ok")) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}
