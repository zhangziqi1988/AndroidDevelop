package com.example.coolweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.coolweather.news.News;
import com.example.coolweather.news.NewsAdapter;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zzq on 2018/8/28.
 */

public class NewsFragment extends Fragment {
    @Nullable


    private List<News> newsList = new ArrayList<>();
    private ListView newsListView;
    private static final String APPKEY = "70e02d39b263a566";// 你的appkey
    private static final String URL = "http://api.jisuapi.com/news/get";
    private static final String channel = "头条";// utf8  新闻频道(头条,财经,体育,娱乐,军事,教育,科技,NBA,股票,星座,女性,健康,育儿)
    private static final int num = 10;// 数量 默认10，最大40


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        newsListView = (ListView) view.findViewById(R.id.news_list_view);
        Log.d(Utility.TAG, "newsfragment onCreateView");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(Utility.TAG, "newsfragment onActivityCreated");
        try {
            initNews();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        NewsAdapter newsAdapter = new NewsAdapter(getActivity(), R.layout.news_item, newsList);
//        newsListView.setAdapter(newsAdapter);

    }

    private void initNews() throws UnsupportedEncodingException {
        String result = null;
        String url = URL + "?channel=" + URLEncoder.encode(channel, "utf-8") + "&num=" + num + "&appkey=" + APPKEY;

        Log.d(Utility.TAG, "url" + url);

            HttpUtil.sendOkHttoRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(responseText);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        JSONArray jsonArray = jsonObject1.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String title = obj.getString("title");
                            String time = obj.getString("time");
                            String src = obj.getString("src");
                            String category = obj.getString("category");
                            String pic = obj.getString("pic");
                            String content = obj.getString("content");
                            String url1 = obj.getString("url");
                            String weburl = obj.getString("weburl");
                            Log.d(Utility.TAG,"title=" + title + "time=" + time + "pic=" + pic + "url1="
                                    + url1 + "weburl=" + weburl);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });



    }


}
