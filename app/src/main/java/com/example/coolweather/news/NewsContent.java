package com.example.coolweather.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.coolweather.R;
import com.example.coolweather.util.Utility;

public class NewsContent extends AppCompatActivity {

    private WebView newsContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        Intent intent =getIntent();
        String url = intent.getStringExtra("news_content_url");
        Log.d(Utility.TAG, url);
        newsContent = (WebView) findViewById(R.id.news_content);
        newsContent.getSettings().setJavaScriptEnabled(true);
        newsContent.setWebViewClient(new WebViewClient());
        newsContent.loadUrl(url);
    }
}
