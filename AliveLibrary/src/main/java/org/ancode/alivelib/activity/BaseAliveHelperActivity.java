package org.ancode.alivelib.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.ancode.alivelib.AliveHelper;
import org.ancode.alivelib.listener.CheckCallBack;
import org.ancode.alivelib.utils.Utils;
import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.R;
import org.ancode.alivelib.utils.UiHelper;
import org.ancode.alivelib.utils.Log;

/**
 * Created by andyliu on 16-8-24.
 */
public abstract class BaseAliveHelperActivity extends Activity {
    private static final String TAG = BaseAliveHelperActivity.class.getSimpleName();
    private View empty_view;
    private View error_view;
    private View loading_view;
    private View closeBtn;
    private View topView;
    private TextView titleView;
    protected String data = "";
    private String appName;
    private int themeColor = -1;
    private boolean applyStatusColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        initBaseData();
        initBaseView();
        initView();
        loadData();
        Log.v(TAG, "BaseAliveHelperActivity onCreate");
    }

    protected void initBaseData() {
        if (themeColor == -1)
            themeColor = getIntent().getIntExtra(HelperConfig.THEME_COLOR_KEY, -1);

        if (TextUtils.isEmpty(appName))
            appName = getIntent().getStringExtra(HelperConfig.APP_NAME_KEY);

        applyStatusColor = getIntent().getBooleanExtra(HelperConfig.APPLY_STATUS_COLOR, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadData() {
        AliveHelper.getHelper().check(new CheckCallBack() {
            @Override
            public void onGetData(String data) {
                showLoading(false);
                onRefresh(data);
            }

            @Override
            public void dataEmpty() {
                showLoading(false);
                showEmptyView(true);
            }

            @Override
            public void getDataError(String e) {
                showLoading(false);
                showErrorView(true);
                Log.e(TAG, "获取数据失败:\n" + e);
            }
        });
    }

    public void onReLoad() {
        showErrorView(false);
        showEmptyView(false);
        showLoading(true);
        loadData();
    }

    protected abstract int setLayout();

    protected abstract void initView();

    protected abstract void onRefresh(String data);


    protected void initBaseView() {
        empty_view = findViewById(R.id.base_alive_empty_view);
        error_view = findViewById(R.id.base_alive_error_view);
        loading_view = findViewById(R.id.base_alive_loading_view);
        topView = findViewById(R.id.top);
        titleView = (TextView) findViewById(R.id.title);
        closeBtn = findViewById(R.id.close);
        if (!TextUtils.isEmpty(appName)) {
            titleView.setText(String.format(getString(R.string.alive_activity_title), appName));
        } else {
            titleView.setText(String.format(getString(R.string.alive_activity_title), ""));
        }
        setTimeColor();

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        empty_view.findViewById(R.id.empty_btnReload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReLoad();
            }
        });

        error_view.findViewById(R.id.error_btnReload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReLoad();
            }
        });
        showLoading(true);
    }


    public void showLoading(boolean show) {
        if (show) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading_view.setVisibility(View.VISIBLE);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading_view.setVisibility(View.GONE);
                }
            });
        }
    }

    public void showErrorView(boolean show) {
        if (show) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    error_view.setVisibility(View.VISIBLE);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    error_view.setVisibility(View.GONE);
                }
            });
        }
    }

    public void showEmptyView(boolean show) {
        if (show) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    empty_view.setVisibility(View.VISIBLE);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    empty_view.setVisibility(View.GONE);
                }
            });
        }
    }

    private void setTimeColor() {
        int color = -1;
//        if (themeColor == -1) {
//            themeColor = Utils.getThemeColor();
//        }
        if (themeColor != -1) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                color = getColor(themeColor);
            } else {
                color = getResources().getColor(themeColor);
            }
        } else {
            color = UiHelper.getThemeColor();
        }

        topView.setBackgroundColor(color);
        if (applyStatusColor) {
            UiHelper.setStatusColor(this, color);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
