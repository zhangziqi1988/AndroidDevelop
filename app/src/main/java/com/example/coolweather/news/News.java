package com.example.coolweather.news;

import android.graphics.Bitmap;

/**
 * Created by zzq on 2018/8/30.
 */

public class News {

    private String newsImage;
    private String newsTitle;
    private String newsContent;

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public News() {
    }

    public News(String newsTitle, String newsImage) {
        this.newsTitle = newsTitle;
        this.newsImage = newsImage;

    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public String getNewsTitle() {
        return newsTitle;
    }
}
