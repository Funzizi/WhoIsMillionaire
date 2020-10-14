package com.example.whoismillionaire.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.whoismillionaire.R;

public class MenuActivity extends AppCompatActivity {
    private Button btStart;

    MediaPlayer mediaMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mapping();
    }

    /*
        + Chạy âm background
        + Nếu User nhấn bắt đầu thì chuyển Activity
        */
    @Override
    protected void onResume() {
        super.onResume();
        mediaMenu = MediaPlayer.create(MenuActivity.this, R.raw.bgmusic_menu);
        if(!mediaMenu.isPlaying()){
            mediaMenu.start();
        }
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, RulesActivity.class));
            }
        });
    }

    // Khi chuyển sang Activity khác thì stop âm background
    @Override
    protected void onPause() {
        super.onPause();
        mediaMenu.stop();
    }

    // Không cho phép User nhấn Back
    @Override
    public void onBackPressed() {
    }

    private void mapping() {
        btStart = findViewById(R.id.bt_start);
    }
}