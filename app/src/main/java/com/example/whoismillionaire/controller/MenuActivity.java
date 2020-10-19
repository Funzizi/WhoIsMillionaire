package com.example.whoismillionaire.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.whoismillionaire.R;

public class MenuActivity extends AppCompatActivity {
    private ImageButton imbSoundMenu;
    private Button btStart;
    private Button btHistory;

    MediaPlayer media;
    boolean sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mapping();
        sound = getIntent().getBooleanExtra("sound", true);
    }

    /*
        + Chạy âm background
        + Nếu User nhấn bắt đầu thì chuyển Activity
        */
    @Override
    protected void onResume() {
        super.onResume();
        playMedia(R.raw.bgmusic_menu);
        imbSoundMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound){
                    sound = false;
                    stopMedia();
                    imbSoundMenu.setBackgroundResource(R.drawable.sound_off);
                }
                else {
                    sound = true;
                    playMedia(R.raw.bgmusic_menu);
                    imbSoundMenu.setBackgroundResource(R.drawable.sound_on);
                }
            }
        });
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RulesActivity.class);
                intent.putExtra("sound", sound);
                startActivity(intent);
            }
        });
        btHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, HistoryActivity.class));
            }
        });
    }

    // Khi chuyển sang Activity khác thì stop âm background
    @Override
    protected void onPause() {
        super.onPause();
        stopMedia();
    }

    // Không cho phép User nhấn Back
    @Override
    public void onBackPressed() {
    }

    public void stopMedia() {
        if (media != null) {
            media.stop();
            media.release();
            media = null;
        }
    }

    public void playMedia(int mediaId) {
        stopMedia();
        media = MediaPlayer.create(MenuActivity.this, mediaId);
        media.start();
    }

    private void mapping() {
        imbSoundMenu = findViewById(R.id.imb_sound_menu);
        btStart = findViewById(R.id.bt_start);
        btHistory = findViewById(R.id.bt_history);
    }
}