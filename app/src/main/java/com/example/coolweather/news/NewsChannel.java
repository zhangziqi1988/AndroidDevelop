package com.example.coolweather.news;

import android.util.Log;

import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zzq on 2018/9/1.
 */

public class NewsChannel {

    private List<String> newsCategory = new ArrayList<>();

    private String URL = "http://api.jisuapi.com/news/channel";

    private String url;
    private String APPKEY;


    public void setAPPKEY(String APPKEY) {
        this.APPKEY = APPKEY;
    }

    public void getNewsCategory() {
        url = URL + "?appkey=" + APPKEY;
        Log.d(Utility.TAG, "NewsChannel url=" + url);

        HttpUtil.sendOkHttoRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseText);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.d(Utility.TAG, "jsonArray=" + jsonArray.get(i));

                        newsCategory.add(jsonArray.get(i).toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

}
