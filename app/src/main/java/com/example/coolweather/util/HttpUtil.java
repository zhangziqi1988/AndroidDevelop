package com.example.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by zzq on 2018/8/19.
 * 工具类，用于处理http请求
 */

public class HttpUtil {

    public static void sendOkHttoRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();//构建http请求
        client.newCall(request).enqueue(callback);//在子线程中调用http请求

    }
}
