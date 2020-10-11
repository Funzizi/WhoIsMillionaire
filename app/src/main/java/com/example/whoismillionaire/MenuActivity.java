package com.example.whoismillionaire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    private Button btStart;
    private TextView tvQ5;
    private TextView tv10;
    private TextView tv15;
    private Button btStart5050;
    private ImageView imvStartCall;
    private ImageView imvStartPerson;
    boolean isStart = false;
    MediaPlayer mediaStart, mediaReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mapping();
        btStart.setBackgroundResource(0);
        mediaStart = MediaPlayer.create(MenuActivity.this, R.raw.rule);
        mediaStart.start();
        onAnimation();
        mediaStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btStart.setBackgroundResource(R.drawable.background_answer_when_click);
                btStart.setText("Sẵn sàng");
                btStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!isStart){
                            isStart = true;
                            mediaReady = MediaPlayer.create(MenuActivity.this, R.raw.start_question);
                            mediaReady.start();
                            mediaReady.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    startActivity(new Intent(MenuActivity.this, MainActivity.class));
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void onAnimation() {
        final Animation animStart5 = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.start_scale5);
        tvQ5.setAnimation(animStart5);
        final Animation animStart10 = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.start_scale10);
        tv10.setAnimation(animStart10);
        final Animation animStart15 = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.start_scale15);
        tv15.setAnimation(animStart15);
        final Animation animStart5050 = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.start_scale5050);
        btStart5050.setAnimation(animStart5050);
        final Animation animStartCall = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.start_scale_call);
        imvStartCall.setAnimation(animStartCall);
        final Animation animStartPerson = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.start_scale_person);
        imvStartPerson.setAnimation(animStartPerson);
    }

    private void mapping() {
        btStart = findViewById(R.id.bt_start);
        tvQ5 = findViewById(R.id.tv_q5);
        tv10 = findViewById(R.id.tv_q10);
        tv15 = findViewById(R.id.tv_q15);
        btStart5050 = findViewById(R.id.bt_start_5050);
        imvStartCall = findViewById(R.id.imv_start_call);
        imvStartPerson = findViewById(R.id.imv_start_person);
    }
}