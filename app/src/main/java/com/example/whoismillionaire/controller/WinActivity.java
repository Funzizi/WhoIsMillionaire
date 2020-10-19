package com.example.whoismillionaire.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.whoismillionaire.R;

public class WinActivity extends AppCompatActivity {
    private Button btPlayAgain;
    private ImageView imvWin;

    boolean sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        initView();
        sound = getIntent().getBooleanExtra("sound", true);
        imvWin.startAnimation(AnimationUtils.loadAnimation(WinActivity.this, R.anim.win_scale));
        btPlayAgain.setBackgroundResource(0);
        if(sound){
            MediaPlayer media = MediaPlayer.create(WinActivity.this, R.raw.win_games);
            media.start();
            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    btPlayAgain.setBackgroundResource(R.drawable.background_answer_when_click);
                    btPlayAgain.setText("Chơi lại");
                    btPlayAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(WinActivity.this, MainActivity.class);
                            intent.putExtra("sound", true);
                            startActivity(intent);
                        }
                    });
                }
            });
        }
        else {
            btPlayAgain.setBackgroundResource(R.drawable.background_answer_when_click);
            btPlayAgain.setText("Chơi lại");
            btPlayAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WinActivity.this, MainActivity.class);
                    intent.putExtra("sound", false);
                    startActivity(intent);
                }
            });
        }
    }

    private void initView() {
        btPlayAgain = findViewById(R.id.bt_play_again);
        imvWin = findViewById(R.id.imv_win);
    }
}