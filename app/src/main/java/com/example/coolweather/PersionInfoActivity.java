package com.example.coolweather;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PersionInfoActivity extends AppCompatActivity {

    private String userName;
    private TextView userNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persion_info);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        userNameText = (TextView) findViewById(R.id.user_name_info);
        Intent intent = getIntent();
        userName = intent.getStringExtra("user_name");
        userNameText.setText(userName);

    }
}
