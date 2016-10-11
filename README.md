# AliveHelper
AliveHelper防杀助手,统计应用使用率.


## 使用

###1.依赖权限
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
###2.Application中配置防杀助手(无耗时操作)

        //初始化操作
        AliveHelper.init(getApplicationContext());//初始化(必须项)
        AliveHelper.setNotifySmallIcon(R.drawable.alive_helper_small_icon);//如果要弹出通知,需开发者提供应用小图标id
        AliveHelper.setDebug(true);//是否打印防杀助手log
        AliveHelper.setThemeColor(R.color.alive_dialog_btn_border_color);//手动设置展示界面的主色调
        AliveHelper.useAnet(false); //是否使用原网环境

        //开启功能类

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device", "htc");
            jsonObject.put("os", "你的手机系统版本号");
            jsonObject.put("id", "13018211911");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AliveHelper.getHelper().openAliveCount(jsonObject.toString());//开启应用使用率统计

*注意:openAliveCount(String info),info格式为json,json内参数内容不固定,需讨论.