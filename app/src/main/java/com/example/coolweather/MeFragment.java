package com.example.coolweather;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.util.Utility;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zzq on 2018/8/28.
 */

public class MeFragment extends Fragment {

    private CircleImageView headerImage;
    private boolean loginStatus;
    private String userEmil;
    private String userSigure;
    private String userName;
    private TextView userEmilText;
    private TextView userSigureText;
    private TextView userNameText;
    private Button testButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        headerImage = (CircleImageView) view.findViewById(R.id.user_header);
        userEmilText = (TextView) view.findViewById(R.id.user_email_info);
        userSigureText = (TextView) view.findViewById(R.id.context_info);
        userNameText = (TextView) view.findViewById(R.id.user_name_info);
        testButton = (Button) view.findViewById(R.id.test_button);
        loginStatus = false;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(Utility.TAG, "onActivityCreated");
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        loginStatus = prefs.getBoolean("login_status", false);
//        Log.d(Utility.TAG, "loginStatus1=" + loginStatus);
//        userEmil = prefs.getString("user_emil", null);
//        userSigure = prefs.getString("user_sigure", null);
//        userName = prefs.getString("user_name", null);
//        if (loginStatus) {
//            headerImage.setImageResource(R.drawable.ic_log_head);
//            userEmilText.setText(userEmil);
//            userSigureText.setText(userSigure);
//            userNameText.setText(userName);
//
//        }
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginStatus) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(intent, 1);

                } else {
                    Toast.makeText(getActivity(), "用户登录", Toast.LENGTH_SHORT).show();
                }
            }


        });

        testButton.setVisibility(View.GONE);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmilText.setText("测试");
            }
        });
        Log.d(Utility.TAG, "onActivityCreated continue");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(Utility.TAG, "onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Utility.TAG, "onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Utility.TAG, "onResume");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loginStatus = prefs.getBoolean("login_status", false);
        userEmil = prefs.getString("user_emil", null);
        userSigure = prefs.getString("user_sigure", null);
        userName = prefs.getString("user_name", null);
        if (loginStatus) {
            headerImage.setImageResource(R.drawable.ic_log_head);
            userEmilText.setText(userEmil);
            userSigureText.setText(userSigure);
            userNameText.setText(userName);

        }



    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(Utility.TAG, "onPause");

    }
}
