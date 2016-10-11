package org.ancode.alivehelperdemo;

import org.ancode.alivelib.AliveHelper;
import org.ancode.alivelib.utils.SPUtils;
import org.json.JSONException;
import org.json.JSONObject;

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

        //初始化操作(无耗时操作)

        AliveHelper.init(getApplicationContext());//初始化**(必须项)**
        AliveHelper.setNotifySmallIcon(R.drawable.alive_helper_small_icon);//如果要弹出通知,需开发者提供应用小图标
        AliveHelper.setDebug(true);//是否打印防杀助手log
//        AliveHelper.setThemeColor(R.color.alive_dialog_btn_border_color);//手动设置展示界面的主色调
//        AliveHelper.useAnet(false); //是否使用原网环境

        //开启功能类

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device", "htc");
            jsonObject.put("os", "系统版本号");
            jsonObject.put("id", "13018211911");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //json格式参数代表上传数据的唯一标示(格式不固定)
        AliveHelper.getHelper().openAliveCount(jsonObject.toString());//开启应用使用率统计
        //*注意:openAliveCount(String info),info格式为json,json内参数内容不固定,需讨论.


//        AliveHelper.getHelper().openAliveWarning(0.9f);
        // *注意事项openAliveWarning(float warningPoint)参数值范围为[0-1]
    }
}
