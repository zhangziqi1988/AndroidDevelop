package com.example.coolweather.gson;

/**
 * Created by zzq on 2018/8/19.
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
