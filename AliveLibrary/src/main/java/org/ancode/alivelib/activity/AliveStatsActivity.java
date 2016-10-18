package org.ancode.alivelib.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.ancode.alivelib.R;
import org.ancode.alivelib.config.Constants;
import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.http.HttpClient;
import org.ancode.alivelib.listener.StringCallBack;
import org.ancode.alivelib.utils.AliveSPUtils;
import org.ancode.alivelib.utils.DateTimeUtils;
import org.ancode.alivelib.utils.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andyliu on 16-8-24.
 */
public class AliveStatsActivity extends BaseActivity {
    private static final String TAG = AliveStatsActivity.class.getSimpleName();
    private List<String> queryResult = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "AliveStatsActivity onCreate");
    }

    @Override
    public void loadData() {
        Map<String, String> params = new HashMap<String, String>();
        String packageName = HelperConfig.CONTEXT.getPackageName().toString();
        Date date = new Date();
        String beginTime = String.valueOf(DateTimeUtils.getBeforeDate(date, 1).getTime());
        String endTime = String.valueOf(date.getTime());
//        params.put("app", packageName);
//        params.put("type", Constants.TYPE_ALIVE);
//        params.put("tag", AliveSPUtils.getInstance().getASTag());
//        params.put("begin", beginTime);
//        params.put("end", endTime);

        //测试数据
        params.put("app", "org.ancode.mixun");
        params.put("type", Constants.TYPE_ALIVE);
        params.put("tag", "MX:92416");
        params.put("begin", beginTime);
        params.put("end", endTime);
        HttpClient.queryAliveStats(params, HttpClient.HTTP_CALL_FLAG, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response);
                    if (jsonObj.has("result")) {
                        String result = jsonObj.get("result").toString();
                        if (result.equals("ok")) {
                            if (jsonObj.has("stats")) {
                                showLoading(false);
                                JSONArray jsonArray = jsonObj.getJSONArray("stats");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    queryResult.add(jsonArray.getString(i));
                                    Log.v(TAG, jsonArray.getString(i));
                                }

                            } else {
                                showLoading(false);
                                showEmptyView(true);
                            }

                        } else if (result.equals("failed")) {
                            showLoading(false);
                            showErrorView(true);
                        }
                    } else {
                        showLoading(false);
                        showErrorView(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error(String error) {

            }
        });
    }


    @Override
    protected int setLayout() {
        return R.layout.alive_stats_activity;
    }


    @Override
    protected void initView() {
        setTitle(String.format(getString(R.string.alive_stats_title), appName));
    }


    @Override
    protected void onRefresh(String data) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

}
