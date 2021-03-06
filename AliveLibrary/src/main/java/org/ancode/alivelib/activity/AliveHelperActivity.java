package org.ancode.alivelib.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.ancode.alivelib.AliveHelper;
import org.ancode.alivelib.R;
import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.config.HttpUrlConfig;
import org.ancode.alivelib.http.HttpClient;
import org.ancode.alivelib.listener.StringCallBack;
import org.ancode.alivelib.utils.Log;

/**
 * Created by andyliu on 16-8-24.
 */
public class AliveHelperActivity extends BaseActivity {
    private static final String TAG = AliveHelperActivity.class.getSimpleName();
    private WebView webView;
    ProgressBar progressBar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "AliveHelperActivity onCreate");

    }

    @Override
    public void loadData() {
        AliveHelper.getHelper().check(new StringCallBack() {
            @Override
            public void onResponse(String response) {
                showLoading(false);
                onRefresh(response);
            }

            @Override
            public void error(String error) {
                if (error.equals(HttpClient.DATA_IS_NULL)) {
                    showLoading(false);
                    Log.v(TAG, "show default html");
                    onRefresh(HttpUrlConfig.DEFAULT_WARNING_URL);
                } else {
                    showLoading(false);
                    showErrorView(true);
                    Log.e(TAG, "获取数据失败:\n" + error);
                }

            }

        });
    }


    @Override
    protected int setLayout() {
        return R.layout.alive_helper_activity;
    }


    @Override
    protected void initView() {
        setTitle(String.format(getString(R.string.alive_activity_title), appName));
        progressBar = (ProgressBar) findViewById(R.id.progress);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        //获取WebSettings对象,设置缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);//允许DCOM

        final ProgressBar finalProgressBar = progressBar;
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                finalProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    finalProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    finalProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    protected void onRefresh(String data) {
        webView.loadUrl(data);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (URLUtil.isNetworkUrl(url)) {
                    return false;
                }
                Log.v("WebViewDialog", "start Web url is = " + url);
                try {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
