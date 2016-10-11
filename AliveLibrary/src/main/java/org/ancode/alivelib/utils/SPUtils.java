package org.ancode.alivelib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.ancode.alivelib.base.BaseSPUtils;
import org.ancode.alivelib.config.HelperConfig;

/**
 * Created by xf on 16-8-3.
 */
public class SPUtils extends BaseSPUtils {
    private static final String TAG = SPUtils.class.getSimpleName();
    private static final String SP_NAME = "alive_helper";

    private static SPUtils spUtils;
    private SharedPreferences sp;

    @Override
    protected SharedPreferences getSharedPreferences() {
        return sp;
    }

    private SPUtils() {
        sp = HelperConfig.CONTEXT.getSharedPreferences(SP_NAME, Context.MODE_MULTI_PROCESS);
    }

    public static SPUtils getInstance() {
        if (spUtils == null) {
            synchronized (SPUtils.class) {
                if (spUtils == null) {
                    spUtils = new SPUtils();
                }
            }
        }
        return spUtils;
    }


    /**
     * 统计开始时间
     */
    private static final String ALIVE_COUNT_BEGIN_TIME = "alive_count_begin_time";

    /**
     * 统计结束时间
     */
    private static final String ALIVE_COUNT_END_TIME = "alive_count_end_time";
    /**
     * 终端信息（这里的格式不是确定，不同的app报名自己定义info格式）
     */
    private static final String ALIVE_COUNT_UPLOAD_INFO = "alive_count_upload_info";

    /**
     * 获取统计开始时间
     *
     * @return
     */
    public long getACBeginTime() {
        return getLong(ALIVE_COUNT_BEGIN_TIME, 0);
    }

    /**
     * 设置统计开始时间
     *
     * @return
     */
    public void setACBeginTime(long acStartTime) {
        putLong(ALIVE_COUNT_BEGIN_TIME, acStartTime);
    }

    /**
     * 获取统计结束时间
     *
     * @return
     */
    public long getACEndTime() {
        return getLong(ALIVE_COUNT_END_TIME, 0);
    }


    /**
     * 设置统计结束时间
     *
     * @return
     */
    public void setACEndTime(long acEndTime) {
        putLong(ALIVE_COUNT_END_TIME, acEndTime);
    }

    /***
     * 获取终端信息
     *
     * @return
     */
    public String getACUploadInfo() {
        return getString(ALIVE_COUNT_UPLOAD_INFO, "");
    }

    /**
     * 设置终端信息
     *
     * @param uploadInfo
     */
    public void setACUploadInfo(String uploadInfo) {
        putString(ALIVE_COUNT_UPLOAD_INFO, uploadInfo);
    }
}
