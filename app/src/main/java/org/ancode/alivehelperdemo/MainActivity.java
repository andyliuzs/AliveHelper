package org.ancode.alivehelperdemo;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ancode.alivelib.AliveHelper;
import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.listener.StringCallBack;
import org.ancode.alivelib.utils.Log;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    Button showActivityBtn;
    Button onlygetdataBtn;
    Button showdialogBtn;
    Button sendbroadcastBtn;
    Button showwebBtn;
    Button notificationGoActivity;
    TextView textview;
    Button showAliveStats;
    //about broadcast
    public static final String BROADCAST_ACTION = "org.ancode.test.ACTION";
    private TestReceiver testReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        registerBroadCast();

        //initPermission();
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 10);
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (Settings.canDrawOverlays(this)) {
                Log.v(TAG, "SYSTEM_ALERT_WINDOW 授权成功...");
                initPermission();
            } else {
                Log.v(TAG, "SYSTEM_ALERT_WINDOW 授权失败...");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadCast();
    }

    private void registerBroadCast() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        testReceiver = new TestReceiver();
        registerReceiver(testReceiver, intentFilter);
    }

    private void unregisterBroadCast() {
        unregisterReceiver(testReceiver);
    }

    private void initView() {
        textview = (TextView) findViewById(R.id.textview);
        showActivityBtn = (Button) findViewById(R.id.showactivity);
        showActivityBtn.setOnClickListener(this);
        onlygetdataBtn = (Button) findViewById(R.id.onlygetdata);
        onlygetdataBtn.setOnClickListener(this);
        showdialogBtn = (Button) findViewById(R.id.showdialog);
        showdialogBtn.setOnClickListener(this);
        sendbroadcastBtn = (Button) findViewById(R.id.sendbroadcast);
        sendbroadcastBtn.setOnClickListener(this);
        showwebBtn = (Button) findViewById(R.id.showweb);
        showwebBtn.setOnClickListener(this);
        notificationGoActivity = (Button) findViewById(R.id.go_activity_notification);
        notificationGoActivity.setOnClickListener(this);
        notificationGoActivity = (Button) findViewById(R.id.go_activity_notification);
        notificationGoActivity.setOnClickListener(this);
        showAliveStats =(Button)findViewById(R.id.go_alive_stats_activity);
        showAliveStats.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.onlygetdata:
                AliveHelper.getHelper()
                        .check(new StringCallBack() {
                            @Override
                            public void onResponse(String response) {
                                textview.setText("StringCallBack function url==" + response);
                            }

                            @Override
                            public void error(String error) {
                                textview.setText("StringCallBack function error\n" + error);
                            }
                        });

                break;
            case R.id.showactivity:
                //使用默认activity
                AliveHelper.getHelper()
                        .showAliveHelper();
                break;

            case R.id.go_activity_notification:
                AliveHelper.getHelper()
                        .notification(2000);

                break;
            case R.id.showdialog:
                AliveHelper.getHelper()
                        .showDialog();
                break;
            case R.id.showweb:
                AliveHelper.getHelper()
                        .showWeb();
                break;
            case R.id.sendbroadcast:

                //指定action方法1
                AliveHelper.getHelper()
                        .sendBroadCast(BROADCAST_ACTION);
                //指定action方法2
//              AliveHelper.getHelper().sendBroadCast();

                break;

            case R.id.go_alive_stats_activity:
                AliveHelper.getHelper().showAliveStats();
                break;

        }
    }


    class TestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_ACTION)) {
                String data = intent.getStringExtra(HelperConfig.DATA_KEY);
                textview.setText("Broadcast function url = " + data);
            }
        }
    }


}
