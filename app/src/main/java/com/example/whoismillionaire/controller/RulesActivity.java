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

    MediaPlayer mediaRules, mediaReady;

    // Âm giới thiệu ở lần đầu, hết âm sẽ hiện nút sẵn sàng
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        mapping();
        btStart.setBackgroundResource(0);
        startAnimation();
        mediaRules = MediaPlayer.create(RulesActivity.this, R.raw.rule);
        mediaReady = MediaPlayer.create(RulesActivity.this, R.raw.start_question);
        mediaRules.start();
        mediaRules.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btStart.setBackgroundResource(R.drawable.background_answer_when_click);
                btStart.setText("Sẵn sàng");
                btStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isStart){
                            isStart = false;
                            mediaReady.start();
                            mediaReady.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    startActivity(new Intent(RulesActivity.this, MainActivity.class));
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    // Tắt media, và cho phép người chơi có thể sẵn sàng ở lần sau
    @Override
    protected void onStop() {
        super.onStop();
        if(mediaRules.isPlaying()) mediaRules.stop();
        if(mediaReady.isPlaying()) mediaReady.stop();
        isStart = true;
    }

    // Animation
    private void startAnimation() {
        final Animation animStart5 = AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale5);
        tvQ5.setAnimation(animStart5);
        final Animation animStart10 = AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale10);
        tvQ10.setAnimation(animStart10);
        final Animation animStart15 = AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale15);
        tvQ15.setAnimation(animStart15);
        final Animation animStart5050 = AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale5050);
        btStart5050.setAnimation(animStart5050);
        final Animation animStartCall = AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale_call);
        imvStartCall.setAnimation(animStartCall);
        final Animation animStartPerson = AnimationUtils.loadAnimation(RulesActivity.this, R.anim.start_scale_person);
        imvStartPerson.setAnimation(animStartPerson);
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