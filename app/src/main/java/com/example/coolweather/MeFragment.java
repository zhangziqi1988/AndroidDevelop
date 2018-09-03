package com.example.coolweather;

import android.app.ActionBar;
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
import android.widget.TextView;

import com.example.coolweather.util.Utility;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zzq on 2018/8/28.
 */

public class MeFragment extends Fragment {

    private CircleImageView headerImage;
    private boolean loginStatus;
    private String userEmil;
    private String userSigure;
    private TextView userEmilText;
    private TextView userSigureText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        headerImage = (CircleImageView) view.findViewById(R.id.user_header);
        userEmilText = (TextView) view.findViewById(R.id.user_email_info);
        userSigureText = (TextView) view.findViewById(R.id.context_info);

        loginStatus = false;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loginStatus = prefs.getBoolean("login_status", false);
        Log.d(Utility.TAG, "loginStatus1=" + loginStatus);
        userEmil = prefs.getString("user_emil", null);
        userSigure = prefs.getString("user_sigure", null);
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginStatus) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent);

                    if (userEmil != null && userSigure != null) {
                        userEmilText.setText(userEmil);
                        userSigureText.setText(userSigure);

                    }
                } else {
                    headerImage.setImageResource(R.drawable.ic_log_head);
                }
            }


        });
        loginStatus = prefs.getBoolean("login_status", false);
        Log.d(Utility.TAG, "loginStatus2=" + loginStatus);

        if (loginStatus) {
            headerImage.setImageResource(R.drawable.ic_log_head);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if()
    }
}
