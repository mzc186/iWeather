package com.example.mzc.iweather.netutil;

/**
 * Created by MZC on 9/4/2016.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
