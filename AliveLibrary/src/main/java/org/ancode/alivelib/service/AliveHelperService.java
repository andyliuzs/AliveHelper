package org.ancode.alivelib.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import org.ancode.alivelib.AliveHelper;
import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.utils.CountAliveUtils;
import org.ancode.alivelib.utils.DateTimeUtils;
import org.ancode.alivelib.utils.HttpUtils;
import org.ancode.alivelib.utils.Log;
import org.ancode.alivelib.utils.NetUtils;
import org.ancode.alivelib.utils.SPUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andyliu on 16-10-8.
 */
public class AliveHelperService extends Service {
    public static final String ACTION = "action";

    public static final String OPEN_ALIVE_COUNT_SERVICE_ACTION = "org.ancode.alivelib.service.OPEN_ALIVE_COUNT_SERVICE";
    public static final String OPEN_ALIVE_WARNING_SERVICE_ACTION = "org.ancode.alivelib.service.OPEN_ALIVE_WARNING_SERVICE";

    public static final String CLOSE_ALIVE_COUNT_SERVICE_ACTION = "org.ancode.alivelib.service.CLOSE_ALIVE_COUNT_SERVICE";
    public static final String CLOSE_ALIVE_WARNING_SERVICE_ACTION = "org.ancode.alivelib.service.CLOSE_ALIVE_WARNING_SERVICE";

    private final int WARNING_TIME = 1000 * 60 * 30;
    private static final String TAG = AliveHelperService.class.getSimpleName();
    private Timer aliveCountTimer = null, warningTimer = null;

    //文件写入
    private File aliveCountfile = null;
    private FileWriter fileWriter = null;
    private BufferedWriter writer = null;
    //是否已经提示
    private boolean isNotify = false;

    private boolean isFirstCount = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getStringExtra(ACTION);
        switch (action) {
            case OPEN_ALIVE_COUNT_SERVICE_ACTION:
                openCountLiveTimer();
                break;
            case OPEN_ALIVE_WARNING_SERVICE_ACTION:
                openWarningTimer();
                break;
            case CLOSE_ALIVE_COUNT_SERVICE_ACTION:
                closeCountLiveTimer();
                break;
            case CLOSE_ALIVE_WARNING_SERVICE_ACTION:
                closeWarningTimer();
                break;
            default:
                throw new RuntimeException("no this action");
        }
        return super.onStartCommand(intent, START_STICKY, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void openCountLiveTimer() {
        if (aliveCountTimer == null) {
            Log.v(TAG, "---start count alive---");
            aliveCountTimer = new Timer();
            aliveCountTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        countAlive();
                        isFirstCount = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, HelperConfig.ALIVE_COUNT_RATE);
        }
    }

    private void closeCountLiveTimer() {
        if (aliveCountTimer != null) {
            aliveCountTimer.purge();
            aliveCountTimer.cancel();
            aliveCountTimer = null;
            Log.v(TAG, "---close count alive---");
        }
    }

    private void openWarningTimer() {

        if (warningTimer == null) {
            Log.v(TAG, "---open notification timer---");
            warningTimer = new Timer();
            warningTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (isNotify) {
                            Log.v(TAG, "allready notificaton,do return");
                            return;
                        }
                        while (isFirstCount) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        float percent = CountAliveUtils.getAlivePercent();
                        if (percent >= 0) {
                            if (percent <= HelperConfig.WARNING_POINT) {
                                handler.sendEmptyMessage(SHOW_NOTIFICATION);
                                isNotify = true;
                            }
                        } else {
                            isNotify = true;
                            Log.e(TAG, "no count yesterday, no warning!");
                        }
                        Log.v(TAG, "alive percent=" + percent + ",warning point=" + HelperConfig.WARNING_POINT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, WARNING_TIME);
        }
    }

    private void closeWarningTimer() {
        if (warningTimer != null) {
            warningTimer.purge();
            warningTimer.cancel();
            warningTimer = null;
            Log.v(TAG, "---close notification alert---");
        }

    }

    private static final int SHOW_NOTIFICATION = 0x101;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SHOW_NOTIFICATION) {
                AliveHelper.getHelper().notification(0);
            }
        }
    };


    private void countAlive() {
        long nowTime = new Date().getTime();
        try {
            checkFileWriter();

            //TODO[统计Wifi/3G状态]
            String netStatus = NetUtils.getNetStatus(HelperConfig.CONTEXT);
            //写入文件
            writer.write(nowTime + " " + netStatus + "\r\n");
            writer.flush();
            Log.v(TAG, "insert time =" + DateTimeUtils.timeFormat(nowTime, null));

            //TODO[计算统计范围,超过一小时,将数据上传至服务器]
            //开始时间为0时重新赋值
            long startTime = SPUtils.getInstance().getACBeginTime();
            if (startTime == 0) {
                SPUtils.getInstance().setACBeginTime(nowTime);
                startTime = nowTime;
            }

            //结束时间实时赋值
            SPUtils.getInstance().setACEndTime(nowTime);

            float differTime = DateTimeUtils.getDifferHours(startTime, nowTime);
            if (differTime >= HelperConfig.UPLOAD_ALIVE_COUNT_RATE) {
                Log.v(TAG, "距离第一次统计时间" + differTime + "小时,开始上传服务器");
                //开始上传
                if (HttpUtils.uploadAliveCount(startTime, nowTime)) {
                    Log.v(TAG, "----上传数据成功----");
                    //交互成功修
                    SPUtils.getInstance().setACBeginTime(0);
                    deleteFile(HelperConfig.ALIVE_COUNT_FILE_NAME);
                    clearFileWriter();
                    Log.v(TAG, "----重置数据成功----");
                }
            } else {
                Log.v(TAG, "距离第一次统计时间" + differTime + "小时,不上传服务器");
            }


        } catch (FileNotFoundException e) {
            Log.e(TAG, "write error:" + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "write error:" + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkFileWriter() throws Exception {
        if (aliveCountfile == null) {

            aliveCountfile = new File(getFilesDir(), HelperConfig.ALIVE_COUNT_FILE_NAME);

            if (!aliveCountfile.exists()) {
                try {
                    aliveCountfile.createNewFile();
                } catch (IOException e) {
                    Log.e(TAG, "create file error:" + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }

        if (fileWriter == null) {
            fileWriter = new FileWriter(aliveCountfile, true);
        }
        if (writer == null) {
            writer = new BufferedWriter(fileWriter);
        }
    }

    public void clearFileWriter() {
        try {
            writer.close();
        } catch (IOException e) {
            Log.e(TAG, "close error:" + e.getLocalizedMessage());
            e.printStackTrace();
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "close error:" + e.getLocalizedMessage());
            e.printStackTrace();
        }
        aliveCountfile = null;
        fileWriter = null;
        writer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeCountLiveTimer();
        closeWarningTimer();
        clearFileWriter();
    }
}
