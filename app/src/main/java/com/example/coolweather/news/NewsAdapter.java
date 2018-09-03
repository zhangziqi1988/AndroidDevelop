package com.example.coolweather.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coolweather.R;
import com.example.coolweather.util.Utility;

import java.util.List;

/**
 * Created by zzq on 2018/8/30.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private int resourceId;
    private Context mContext;
    private List<News> newsList;
    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
        resourceId = resource;
        mContext = context;
        newsList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        News news = getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        } else {
            view = convertView;
        }
        ImageView newsImage = (ImageView) view.findViewById(R.id.news_image);
        TextView newsTitle = (TextView) view.findViewById(R.id.news_title);
        if (news.getNewsImage().isEmpty()) {
            //如果网络图片的url是空，则加载本地图片
            Glide.with(mContext).load(R.drawable.ic_image_fail).fitCenter().into(newsImage);

        } else {
            Glide.with(mContext).load(news.getNewsImage()).fitCenter().into(newsImage);
        }
        newsTitle.setText(news.getNewsTitle());


        return view;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }
}
