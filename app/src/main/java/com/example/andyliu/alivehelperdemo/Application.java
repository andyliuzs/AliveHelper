package com.example.andyliu.alivehelperdemo;

import org.ancode.alivelib.AliveHelper;

/**
 * Created by andyliu on 16-8-25.
 */
public class Application extends android.app.Application {
    private static android.app.Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        init();
    }

    public static android.app.Application getInstance() {
        return application;
    }

    private void init() {
        /**
         * 1.初始化上下文(必须)
         * 2.是否打印log(可选)
         * 3.设置显示UI的主题颜色(可选)
         * 4.是否在原网环境下(可选)
         */
//        AliveHelper.init(getApplicationContext());
//        AliveHelper.setDebug(true);
//        AliveHelper.setThemeColor(R.color.alive_dialog_line_color);
//        AliveHelper.useAnet(false);
        AliveHelper.init(getApplicationContext()).setDebug(true);
    }
}
