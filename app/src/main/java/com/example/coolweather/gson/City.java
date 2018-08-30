package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zzq on 2018/8/25.
 */

public class City {
    public int id;
    @SerializedName("name")
    public String cityName;

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }
}
