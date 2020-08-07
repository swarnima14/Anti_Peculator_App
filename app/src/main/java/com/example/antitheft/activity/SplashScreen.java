package com.example.antitheft.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import gr.net.maroulis.library.EasySplashScreen;

import com.example.antitheft.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasySplashScreen config=new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(WelcomeActivity.class)
                .withSplashTimeOut(1500)
                .withBackgroundColor(Color.parseColor("#FFFFFF"))
                .withLogo(R.drawable.splash);

        View easySplashScreen=config.create();
        setContentView(easySplashScreen);
    }
}

