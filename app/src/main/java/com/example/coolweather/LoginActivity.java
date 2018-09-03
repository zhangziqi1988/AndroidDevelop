package com.example.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.coolweather.db.User;
import com.example.coolweather.util.Utility;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {


    private String passWordDb;
    private String userNameDb;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(Utility.TAG,"Login start");
        LitePal.getDatabase();
        final CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
        final EditText userNameText = (EditText)findViewById(R.id.userName);
        final EditText passWordText = (EditText)findViewById(R.id.passWord);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Button loginButton = (Button)findViewById(R.id.loginButton);
        Button regisButton = (Button)findViewById(R.id.regisButton);

        SharedPreferences preferences = getSharedPreferences("zzq",MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        String userName = preferences.getString("name",null);
        if(userName==null){
            checkBox.setChecked(false);
        }else {
            checkBox.setChecked(true);
            userNameText.setText(userName);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameText.getText().toString();
                String passWord = passWordText.getText().toString();

//                userNameDb = userName;
//                users = DataSupport.findAll(User.class);
                users = DataSupport.where("userName = ?",userName).find(User.class);
                for (User user:users) {
                    passWordDb = user.getPassWord();
                    userNameDb = user.getUserName();

                }

//                userNameDb = userName;
                Log.d(Utility.TAG, "userName" + userName);
                Log.d(Utility.TAG, "passWord" + passWord);
                Log.d(Utility.TAG, "userNameDb" + userNameDb);
                Log.d(Utility.TAG, "passWordDb" + passWordDb);

                if(userName.equals(userNameDb)&&passWord.equals(passWordDb)){


                    Utility.LoginStatus = true;
//                    builder.setTitle("");
                    builder.setMessage("登录成功");
                    builder.create().show();
//                    Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor_login = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                    editor_login.putBoolean("login_status", true);
                    editor_login.commit();
                    if(checkBox.isChecked()){
                        editor.putString("name",userName);
//                        editor.putString("password","123");
                        editor.commit();
                    }else {
                        editor.remove("name");
                        editor.commit();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("login_return", userName);
                    setResult(RESULT_OK,intent);
                    finish();
//                    Intent intent = new Intent(LoginActivity.this, WeatherActivity.class);
//                    startActivity(intent);
//                    finish();
                }else if(userName.equals(userNameDb)&&(!passWord.equals(passWordDb))){
                    builder.setMessage("密码错误！");
                    builder.create().show();
//                    db.close();
//                    Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                }else {
                    builder.setMessage("无此用户，请先注册");
                    builder.create().show();
                }

            }
        });
        regisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisActivity.class);
                startActivity(intent);
            }
        });

//        finish();
    }
}
