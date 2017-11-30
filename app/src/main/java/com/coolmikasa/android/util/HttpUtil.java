package com.coolmikasa.android.util;


import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 发起http请求 并注册一个回调来处理服务器的响应
 */
public class HttpUtil {
    public static void SendOKHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
