package com.example.whoismillionaire.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whoismillionaire.R;

public class RulesActivity extends AppCompatActivity {
    private TextView tvQ5;
    private TextView tvQ10;
    private TextView tvQ15;
    private Button btStart5050;
    private ImageView imvStartCall;
    private ImageView imvStartPerson;
    private Button btStart;
    boolean isStart = true;    // Có thể nhấn sẵn sàng hay không

    boolean sound;
    MediaPlayer media;

    // Âm giới thiệu ở lần đầu, hết âm sẽ hiện nút sẵn sàng
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        sound = getIntent().getBooleanExtra("sound", true);
        mapping();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sound){
            btStart.setBackgroundResource(0);
            startAnimation();
            playMedia(R.raw.rule);
            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    btStart.setBackgroundResource(R.drawable.background_answer_when_click);
                    btStart.setText("Sẵn sàng");
                    btStart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isStart){
                                isStart = false;
                                playMedia(R.raw.start_question);
                                media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        Intent intent = new Intent(RulesActivity.this, MainActivity.class);
                                        intent.putExtra("sound", true);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
        else {
            btStart.setBackgroundResource(R.drawable.background_answer_when_click);
            btStart.setText("Sẵn sàng");
            btStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RulesActivity.this, MainActivity.class);
                    intent.putExtra("sound", false);
                    startActivity(intent);
                }
            });
        }
    }

    // Tắt media, và cho phép người chơi có thể sẵn sàng ở lần sau
    @Override
    protected void onStop() {
        super.onStop();
        stopMedia();
        isStart = true;
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
        media = MediaPlayer.create(RulesActivity.this, mediaId);
        media.start();
    }

    // Animation
    private void startAnimation() {
        tvQ5.setAnimation(AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale5));
        tvQ10.setAnimation(AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale10));
        tvQ15.setAnimation(AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale15));
        btStart5050.setAnimation(AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale5050));
        imvStartCall.setAnimation(AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale_call));
        imvStartPerson.setAnimation(AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale_person));
    }

    private void mapping() {
        btStart = findViewById(R.id.bt_start);
        tvQ5 = findViewById(R.id.tv_q5);
        tvQ10 = findViewById(R.id.tv_q10);
        tvQ15 = findViewById(R.id.tv_q15);
        btStart5050 = findViewById(R.id.bt_start_5050);
        imvStartCall = findViewById(R.id.imv_start_call);
        imvStartPerson = findViewById(R.id.imv_start_person);
    }
}