package com.example.coolweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.coolweather.util.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zzq on 2018/8/28.
 */

public class StreetFragment extends Fragment {


    private Button takePhotoButton;
    private ImageView picImageView;
    private static final int TAKE_PHOTO = 2;
    private Uri imageUri;
    private WeatherActivity weatherActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_street, container, false);
        takePhotoButton = (Button) view.findViewById(R.id.take_photo);
        picImageView = (ImageView) view.findViewById(R.id.picture);
        imageUri = null;
        weatherActivity = (WeatherActivity) getActivity();
        Log.d(Utility.TAG, "onCreateView");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(Utility.TAG, "onActivityCreated");

        super.onActivityCreated(savedInstanceState);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Utility.TAG, "拍照");

                File outputImage = new File(getActivity().getExternalCacheDir(), "street_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(getActivity(), "com.example.coolweather.fileprovider", outputImage);

                } else {
                    imageUri = Uri.fromFile(outputImage);
                }

                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                getActivity().startActivityForResult(intent, TAKE_PHOTO);
                Log.d(Utility.TAG, "imageUri=" + imageUri);

                //向WeatherActivity传递imageUri
                if (getActivity() instanceof WeatherActivity) {
                    ((WeatherActivity) getActivity()).setImageUriWeather(imageUri);
                }


            }
        });

        displayPicture();

    }

    //展示照片，由WeatherActivity调用
    public void displayPicture() {
        Log.d(Utility.TAG, "展示照片");

        //将拍摄的照片显示出来
        imageUri = getImageUri();
        if (imageUri != null) {
            Log.d(Utility.TAG, "displayPicture-1 imageUri=" + imageUri);

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
            editor.putString("street_image_uri", imageUri.toString());
            editor.apply();
        } else {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            imageUri = Uri.parse(prefs.getString("street_image_uri", null));
            Log.d(Utility.TAG, "displayPicture-2 imageUri=" + imageUri);
        }
        try {
            Log.d(Utility.TAG, "展示照片");

            //将拍摄的照片显示出来
            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
            picImageView.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case TAKE_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    try {
//                        Log.d(Utility.TAG,"展示照片");
//
//                        //将拍摄的照片显示出来
//                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
//                        picImageView.setImageBitmap(bitmap);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                break;
//
//                default:
//                    break;
//        }
//    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public ImageView getPicImageView() {
        return picImageView;
    }

    public void setPicImageView(ImageView picImageView) {
        this.picImageView = picImageView;
    }
}
