package com.example.whoismillionaire.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.whoismillionaire.R;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView imvIconSplashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imvIconSplashScreen = findViewById(R.id.imv_icon_splash_screen);
        imvIconSplashScreen.startAnimation(AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.splash_screen));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MenuActivity.class));
                finish();
            }
        },1200);
    }
}