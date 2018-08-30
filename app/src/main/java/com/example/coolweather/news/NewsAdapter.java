package com.example.coolweather.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coolweather.R;

import java.util.List;

/**
 * Created by zzq on 2018/8/30.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private int resourceId;


    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        News news = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView newsImage = (ImageView) view.findViewById(R.id.news_image);
        TextView newsTitle = (TextView) view.findViewById(R.id.news_title);
//        newsImage.setImageResource(news.getNewsImage());
//        newsTitle.setText(news.getNewsTitle());
        return view;
    }
}
