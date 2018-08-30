package com.example.coolweather.util;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.news.News;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zzq on 2018/8/19.
 * 工具类，解析和处理服务器返回的json数据
 */



public class Utility {


    public static final String TAG = "CoolWeather";
    public static boolean LoginStatus = false;
    /**
     * 解析和处理服务器返回的省级数据
     *
     */

    public static boolean handleProvinceResponse(String response){
        Log.d("CoolWeather","province response=" + response);
        if(!TextUtils.isEmpty(response)){//判断字符串是否为空
            try{
                JSONArray allProvice= new JSONArray(response);
                for (int i = 0; i < allProvice.length(); i++){
                    JSONObject provinceObject = allProvice.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * @param response 返回的数据
     * @param provinceId 市所属的省ID
     * @return
     */

    public static boolean handlerCityResponse(String response,int provinceId){
        Log.d("CoolWeather","response is null?"+TextUtils.isEmpty(response));
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0;i < allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        return false;

    }

    /**
     * 解析和处理服务器返回的县级数据
     * @param response 返回的县级数据
     * @param cityId 县所属的市Id
     * @return
     */
    public static boolean handlerCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i =0;i < allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountryName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handlerWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }


    //解析city
//    public static City handlerCityResponse(String cityContent){
//        try {
//
//            Gson gson = new Gson();
//            List<City> cityList = gson.fromJson(cityContent, new TypeToken<List<City>>(){}.getType());
//
//
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//
//    }
    /**
     * 判断服务是否处于运行状态.
     * @param servicename
     * @param context
     * @return
     */
    public static boolean isServiceRunning(String servicename,Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo>  infos = am.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo info: infos){
            if(servicename.equals(info.service.getClassName())){
                return true;
            }
        }
        return false;
    }




}
