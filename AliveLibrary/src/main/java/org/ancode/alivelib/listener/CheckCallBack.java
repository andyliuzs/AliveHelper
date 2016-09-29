package org.ancode.alivelib.listener;

/**
 * Created by andyliu on 16-8-25.
 */
public interface CheckCallBack {
    public void onGetData(String data);

    public void dataEmpty();

    public void getDataError(String error);

}
