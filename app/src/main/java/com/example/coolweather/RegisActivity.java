package com.example.coolweather;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coolweather.db.User;
import com.example.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.List;

public class RegisActivity extends AppCompatActivity {

    private Button button;
    private SQLiteDatabase db;
    private EditText userNameText;
    private EditText passWordText;
    private String passWordDb= null;
    private String userNameDb = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(Utility.TAG, "RegisActivity start");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        userNameText = (EditText) findViewById(R.id.userName);
        passWordText = (EditText) findViewById(R.id.passWord);
        button = (Button) findViewById(R.id.regisButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                db = openOrCreateDatabase("user.db",MODE_PRIVATE,null);

//                db.execSQL("create table if not exists usertb(_id primary key autoincrement,user text not null,password text not null)");
                String userName = userNameText.getText().toString();
                String passWord = passWordText.getText().toString();
//                ContentValues content = new ContentValues();
//                content.put("user",userName);
//                content.put("password",passWord);
//                db.insert("testtb",null,content);
//                Toast.makeText(RegisActivity.this,"注册成功！", Toast.LENGTH_SHORT).show();
//                db.close();


                List<User> users = DataSupport.where("userName = ?", userName).find(User.class);
                for (User user : users) {
                    passWordDb = user.getPassWord();
                    userNameDb = user.getUserName();

                }

                if (passWordDb!=null) {
                    builder.setMessage("该用户已注册，请直接登录！");
                    builder.create().show();
                    finish();

                }else {
                    User user = new User();
                    user.setUserName(userName);
                    user.setPassWord(passWord);
                    user.save();
                    builder.setMessage("注册成功");
                    builder.create().show();
                    finish();
                }

            }
        });
    }
}
