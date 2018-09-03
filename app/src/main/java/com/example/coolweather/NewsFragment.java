package com.example.coolweather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.coolweather.news.News;
import com.example.coolweather.news.NewsAdapter;
import com.example.coolweather.news.NewsChannel;
import com.example.coolweather.news.NewsContent;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
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

    private TabLayout newsTabLayout;
    private ViewPager newsViewPager;
    private List<News> newsList = new ArrayList<>();
    private ListView newsListView;
    private static final String APPKEY = "70e02d39b263a566";// 你的appkey
    private static final String URL = "http://api.jisuapi.com/news/get";
    private String channel;// utf8  新闻频道(头条,财经,体育,娱乐,军事,教育,科技,NBA,股票,星座,女性,健康,育儿)
    private static final int num = 10;// 数量 默认10，最大40
    private ImageView newsImage;
    private SwipeRefreshLayout newsRefresh;
    private NewsAdapter newsAdapter;
    private String newsUrl;
    private News news;
    private List<String> newsChannelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        newsListView = (ListView) view.findViewById(R.id.news_list_view);
        newsImage = (ImageView) view.findViewById(R.id.image);
        newsRefresh = (SwipeRefreshLayout) view.findViewById(R.id.news_refresh);

        newsTabLayout = (TabLayout) view.findViewById(R.id.news_categary);
//        newsViewPager = (ViewPager) view.findViewById(R.id.news_viewpager);

        Log.d(Utility.TAG, "newsfragment onCreateView");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initTab();


    }


    public void initTab() {

        newsTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


//        newsTabLayout.setupWithViewPager(newsViewPager);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getNewsCategory();

            }
        });

    }


    public void getNewsCategory() {
        String URL = "http://api.jisuapi.com/news/channel";

        String url = URL + "?appkey=" + APPKEY;
        Log.d(Utility.TAG, "getNewsCategory url=" + url);

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

                        newsChannelList.add(jsonArray.get(i).toString());

                    }
                    Log.d(Utility.TAG, "newsChannelList length=" + newsChannelList.size());


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < newsChannelList.size(); i++) {
                                String s = newsChannelList.get(i);
                                Log.d(Utility.TAG, "newsChannelList=" + s);

                                newsTabLayout.addTab(newsTabLayout.newTab());
                                newsTabLayout.getTabAt(i).setText(s);

                            }

                            if (newsTabLayout.getTabAt(0).isSelected()) {
                                Log.d(Utility.TAG, "newsTabLayout init" + newsTabLayout.getTabAt(0).getText());

                                try {
                                    initNews("头条");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                newsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


                                    @Override
                                    public void onRefresh() {
                                        try {
                                            refreshNews("头条");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                            newsTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {

                                    int pos = tab.getPosition();
                                    channel = newsChannelList.get(pos);
                                    Log.d(Utility.TAG, "channel=" + channel);

                                    try {
                                        initNews(channel);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d(Utility.TAG, "onTabSelected pos=" + pos);

                                    newsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


                                        @Override
                                        public void onRefresh() {
                                            Log.d(Utility.TAG, "onRefresh" + channel);
                                            try {
                                                refreshNews(channel);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {
                                    int pos = tab.getPosition();

                                    Log.d(Utility.TAG, "onTabUnselected pos=" + pos);

                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {
                                     int pos = tab.getPosition();
                                    Log.d(Utility.TAG, "onTabReselected pos=" + pos);
                                    channel = newsChannelList.get(pos);
                                    Log.d(Utility.TAG, "channel=" + channel);

                                    try {
                                        initNews(channel);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d(Utility.TAG, "onTabSelected pos=" + pos);

                                    newsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                                        @Override
                                        public void onRefresh() {
                                            try {
                                                refreshNews(channel);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }

                            });

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public void displayNewsContent(String newsUrl) {
        Intent intent = new Intent(getActivity(), NewsContent.class);
        intent.putExtra("news_content_url", newsUrl);
        startActivity(intent);

    }

    private void refreshNews(String channel) throws UnsupportedEncodingException {

        Log.d(Utility.TAG, "refreshNews channel" + channel);
        initNews(channel);
        newsAdapter.notifyDataSetChanged();
        newsRefresh.setRefreshing(false);
//        final String newsUrl = "http://news.sina.cn/gn/2018-08-31/detail-ihinpmnq3713503.d.html?vt=4&pos=108";
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                news = newsList.get(position);
                displayNewsContent(newsUrl);

            }
        });
    }

    private void initNews(String channel) throws UnsupportedEncodingException {
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
                newsList.clear();
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
                        String Contenturl = obj.getString("url");
                        String weburl = obj.getString("weburl");
                        Log.d(Utility.TAG, "title=" + title + " " + "time=" + time + " " + "pic=" + pic + " " + "url="
                                + Contenturl + " " + "weburl=" + weburl);

                        News news = new News(title, pic);
                        news.setNewsContent(Contenturl);
                        newsList.add(news);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(Utility.TAG, "initNews newsList length=" + newsList.size());
                            for (int i = 0; i < newsList.size(); i++) {
                                Log.d(Utility.TAG, "initNews newsList=" + newsList.get(i).getNewsTitle() + "--" + newsList.get(i).getNewsImage());

                            }

                            newsAdapter = new NewsAdapter(getActivity(), R.layout.news_item, newsList);
                            newsListView.setAdapter(newsAdapter);
                            Log.d(Utility.TAG, "newsAdapter length=" + newsAdapter.getCount());

//                            newsListView.get
                            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    news = newsList.get(position);
                                    Log.d(Utility.TAG, "onItemClick news=" + news);
                                    displayNewsContent(news.getNewsContent());

                                }
                            });
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }


}
