package com.example.coolweather;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.gson.Forecast;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.service.AutoUpdateService;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.coolweather.R.menu.nav_menu;

public class WeatherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RadioGroup.OnCheckedChangeListener {

    public DrawerLayout drawerLayout;
    private Button navButtonHome;
    private Button navButtonBack;
    private Button nabButtonSetting;
    public SwipeRefreshLayout swipeRefresh;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    private String mWeatherId;
    private String mWaatherIdLoc;
    private String mCityName;
    private NavigationView navigationView;
    private NavigationView nav_update_weather;
    private boolean isChanged;
    private CircleImageView loginView;
    private TextView userName;
    private String loginReturnName;
    private RadioButton radioWeather;
    private RadioButton radioStreet;
    private RadioButton radioNews;
    private RadioButton radioMe;
    private RadioGroup radioGroup;
    private StreetFragment streetFragment;
    private NewsFragment newsFragment;
    private MeFragment meFragment;
    private AlertDialog alertDialogQuit;
    private Uri imageUriWeather;

    public Uri getImageUriWeather() {
        return imageUriWeather;
    }

    public void setImageUriWeather(Uri imageUriWeather) {
        this.imageUriWeather = imageUriWeather;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始化各控件
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButtonBack = (Button) findViewById(R.id.nav_button_back);
        nabButtonSetting = (Button) findViewById(R.id.nav_button_setting);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        nav_update_weather = (NavigationView) findViewById(R.id.update_weather);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);

        View headLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        navigationView.inflateMenu(R.menu.nav_menu);
        loginView = (CircleImageView) headLayout.findViewById(R.id.login);
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                boolean login_status = prefs.getBoolean("login_status", false);
                if (!login_status) {
                    Intent intent = new Intent(WeatherActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 1);

//                finish();
                    Log.d(Utility.TAG, "点击登录");
                } else {

                    SharedPreferences prefs_user_name = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                    String user_name = prefs_user_name.getString("user_name", null);
                    Intent intent = new Intent(WeatherActivity.this, PersionInfoActivity.class);
                    Log.d(Utility.TAG, "user_name=" + user_name);
                    intent.putExtra("user_name", user_name);
                    startActivity(intent);
                }
            }
        });
        userName = (TextView) headLayout.findViewById(R.id.user_name);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        Log.d("CoolWeather", "weatherString=" + weatherString);


//        String bingPic = prefs.getString("bing_pic", null);
//        if (bingPic != null) {
//            Glide.with(this).load(bingPic).into(bingPicImg);
//
//        } else {
//            loadBingPic();
//        }
        mWeatherId = getIntent().getStringExtra("weather_id");//获取ChooseArea传递来的Intent参数
        mCityName = getIntent().getStringExtra("city_name");//获取ChooseArea传递来的Intent参数
        Log.d("CoolWeather", "mCityName=" + mCityName);
        Log.d("CoolWeather", "weatherString=" + weatherString);

        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Log.d("CoolWeather", "有缓存");
            Weather weather = Utility.handlerWeatherResponse(weatherString);
            String cityName = weather.basic.cityName;
//            mWaatherIdLoc = weather.basic.weatherId;

            Log.d("CoolWeather", "cityName=" + cityName);
            Log.d("CoolWeather", "mWaatherIdLoc=" + mWaatherIdLoc);


            if (mCityName == null && cityName != null) {
                showWeatherInfo(weather);

            } else if (cityName.equals(mCityName)) {
                Log.d("CoolWeather", "cityName=" + cityName);
//                mWaatherIdLoc = weather.basic.weatherId;

                showWeatherInfo(weather);
            } else {
                //无缓存时去服务器查询天气
                Log.d("CoolWeather", "无缓存");

                Log.d("CoolWeather", "mWeatherId=" + mWeatherId);

//            String weatherId = getIntent().getStringExtra("weather_id");
                weatherLayout.setVisibility(View.INVISIBLE);//请求数据的时候将ScrollView隐藏
                drawerLayout.closeDrawers();
                swipeRefresh.setRefreshing(true);
                requestWeather(mWeatherId);
            }
        } else {
            //无缓存时去服务器查询天气
            Log.d("CoolWeather", "无缓存");

            Log.d("CoolWeather", "mWeatherId=" + mWeatherId);

//            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);//请求数据的时候将ScrollView隐藏
            drawerLayout.closeDrawers();
            swipeRefresh.setRefreshing(true);
            requestWeather(mWeatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("CoolWeather", "setOnRefreshListener mWeatherId=" + mWeatherId);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherString = prefs.getString("weather", null);
                Weather weather = Utility.handlerWeatherResponse(weatherString);
                mWaatherIdLoc = weather.basic.weatherId;
                requestWeather(mWaatherIdLoc);
            }
        });
        navButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nabButtonSetting.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
//                boolean login_status = prefs.getBoolean("login_status", false);
//                if (login_status) {
//                    loginView.setImageResource(R.drawable.ic_log_head);
//                }

                drawerLayout.openDrawer(GravityCompat.START);

                Log.d("CoolWeather", "加载滑动菜单");
            }
        });


//        navigationView.setCheckedItem(R.id.choose_city);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////                Toast.makeText(WeatherActivity.this,"选择城市",Toast.LENGTH_SHORT).show();
//                chooseCity();
////                replceChooseFragment();
//
//
//                return true;
//            }
//        });


//        nav_update_weather.setCheckedItem(R.id.update_weather);
//        nav_update_weather.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                Toast.makeText(WeatherActivity.this,"更新天气",Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

        //设置navigationview的menu监听
        navigationView.setNavigationItemSelectedListener(this);
        initUpdateWeatherStatus();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    loginReturnName = data.getStringExtra("login_return");
                    userName.setText(loginReturnName);
                    loginView.setImageResource(R.drawable.ic_log_head);
                    userName.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                    editor.putString("user_name", loginReturnName);
                    editor.commit();
                }
                break;
            case 2:
                //处理拍照
                if (resultCode == RESULT_OK) {
                    Log.d(Utility.TAG, "拍照成功");
//                    StreetFragment streetFragment_pic = new StreetFragment();
//                    @SuppressLint("ResourceType") StreetFragment streetFragment_pic = (StreetFragment) getSupportFragmentManager().findFragmentById(R.layout.fragment_street);
//                    Log.d(Utility.TAG, "streetFragment_pic=" + streetFragment_pic);
                    imageUriWeather = getImageUriWeather();
                    Log.d(Utility.TAG, "imageUriWeather=" + imageUriWeather);
                    if (imageUriWeather != null) {

//                        try {
//                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUriWeather));
//                            Log.d(Utility.TAG, "picImageView=" + streetFragment_pic.getPicImageView());
                        Log.d(Utility.TAG, "streetFragment=" + streetFragment);

                        streetFragment.setImageUri(imageUriWeather);
                        streetFragment.displayPicture();
//                        ImageView imageView = (ImageView)streetFragment_pic.

//                        removeFragment(streetFragment);
//                            streetFragment_pic.getPicImageView().setImageBitmap(bitmap);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }/

                    }
                }
                break;
        }
    }

    private void initUpdateWeatherStatus() {
        //读取更新天气状态
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        isChanged = prefs.getBoolean("update_weather_status", false);
        Log.d(Utility.TAG, "initUpdateWeatherStatus" + isChanged);
        if (!isChanged) {
//            // 停止后台更新天气服务
//            Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
//            stopService(intent);
        } else {
            // 启动后台更新天气服务
            Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
            startService(intent);
        }

    }

    //重写返回按钮的点击事件
    @Override
    public void onBackPressed() {

        finish();
    }


    //引入nav的menu布局
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(nav_menu, menu);


        return true;
    }


    //设置nav的menu布局的子项点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.choose_city:
                chooseCity();
                break;

            case R.id.update_weather:
                //读取更新天气状态
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                isChanged = prefs.getBoolean("update_weather_status", false);
                if (isChanged) {
                    item.setTitle("自动更新天气: 关");
                    // 停止后台更新天气服务
                    Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
                    stopService(intent);
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                    editor.putBoolean("update_weather_status", false);
                    editor.apply();
                } else {
                    item.setTitle("自动更新天气: 开");
                    //开启后台更新天气服务
                    Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
                    startService(intent);
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                    editor.putBoolean("update_weather_status", true);
                    editor.apply();
                }

                Log.d(Utility.TAG, "isChanged=" + isChanged);

                break;


            default:
                break;

        }

        return true;
    }


//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//        if(id == R.id.action_)
//        return true;
//    }


    private void replceChooseFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_view, new ChooseAreaFragment());
        transaction.commit();
    }

    private void chooseCity() {
        Intent intent = new Intent(WeatherActivity.this, ChooseArea.class);
        startActivity(intent);
        finish();
//        onPause();


    }


    //加载必应每日一图
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttoRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    //根据天气情况加载本地图片
    public void loadWeatherPic(String weaherInfoText) {
        if (weaherInfoText.equals("多云")) {
            Glide.with(WeatherActivity.this).load(R.drawable.ic_cloud).into(bingPicImg);
//            return R.drawable.ic_cloud;
        } else if (weaherInfoText.equals("晴")) {
            Glide.with(WeatherActivity.this).load(R.drawable.ic_sun).into(bingPicImg);

//            return R.drawable.ic_sun;
        } else if (weaherInfoText.equals("大雨")) {
            Glide.with(WeatherActivity.this).load(R.drawable.ic_big_rain).into(bingPicImg);

//            return R.drawable.ic_big_rain;
        } else if (weaherInfoText.equals("小雨")) {
            Glide.with(WeatherActivity.this).load(R.drawable.ic_small_rain).into(bingPicImg);

//            return R.drawable.ic_small_rain;
        } else if (weaherInfoText.equals("阴")) {
            Glide.with(WeatherActivity.this).load(R.drawable.ic_cloud_1).into(bingPicImg);
        } else {
            loadBingPic();
        }

    }

    //根据天气id请求城市天气信息
    public void requestWeather(final String weatherId) {

        String weatherUrl = "http://guolin.tech/api/weather?cityid="
                + weatherId + "&key=0c4ff940564941749a68ff82fcf29bfc";
        Log.d("CoolWeather", "weatherUrl=" + weatherUrl);

        HttpUtil.sendOkHttoRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseText = response.body().string();
                final Weather weather = Utility.handlerWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {

                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            Log.d("CoolWeather", "resquest weather mWeatherId" + mWeatherId);
                            showWeatherInfo(weather);

                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });


    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        Log.d(Utility.TAG, "weatherInfo=" + weatherInfo);
        forecastLayout.removeAllViews();
        loadWeatherPic(weatherInfo);
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);

        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);

        }
        String comfort = "舒适度" + weather.suggestion.comfort.info;
        String carWash = "洗车指数" + weather.suggestion.carWash.info;
        String sport = "洗车建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

    }

    //设置radiobutton的点击事件

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {

            case R.id.radio_weather:
//                swipeRefresh.setVisibility(View.VISIBLE);
//                bingPicImg.setVisibility(View.VISIBLE);

                Intent intent = new Intent(WeatherActivity.this, WeatherActivity.class);
                startActivity(intent);
                finish();
                if (streetFragment != null) {
                    removeFragment(streetFragment);
                }
                if (newsFragment != null) {
                    removeFragment(newsFragment);
                }
                if (meFragment != null) {
                    removeFragment(meFragment);
                }
//                Toast.makeText(WeatherActivity.this,"weather",Toast.LENGTH_SHORT).show();
                break;

            case R.id.radio_street:
                swipeRefresh.setVisibility(View.GONE);
                bingPicImg.setVisibility(View.GONE);
                streetFragment = new StreetFragment();
                replaceFragment(streetFragment);
//                Toast.makeText(WeatherActivity.this,"street",Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_news:
                swipeRefresh.setVisibility(View.GONE);
                bingPicImg.setVisibility(View.GONE);
                newsFragment = new NewsFragment();
                replaceFragment(newsFragment);
//                Toast.makeText(WeatherActivity.this,"radio_news",Toast.LENGTH_SHORT).show();
                break;

            case R.id.radio_me:
                swipeRefresh.setVisibility(View.GONE);
                bingPicImg.setVisibility(View.GONE);
                meFragment = new MeFragment();
                replaceFragment(meFragment);
//                Toast.makeText(WeatherActivity.this,"radio_me",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.drawer_layout, fragment);
        transaction.commit();
    }

    private void removeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(Utility.TAG, "返回键");

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                final AlertDialog.Builder dialogQuit = new AlertDialog.Builder(WeatherActivity.this);
                dialogQuit.setTitle("确定退出吗？");
                dialogQuit.setCancelable(false);
                dialogQuit.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialogQuit.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogQuit.setView(View.GONE);
                    }
                });
                dialogQuit.show();
                break;
        }

        return true;
    }
}
