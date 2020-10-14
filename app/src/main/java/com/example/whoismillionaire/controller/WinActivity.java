package com.example.whoismillionaire.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.whoismillionaire.R;

public class WinActivity extends AppCompatActivity {
    private Button btPlayAgain;
    MediaPlayer mediaWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_win);
        btPlayAgain = findViewById(R.id.bt_play_again);
        btPlayAgain.setBackgroundResource(0);
        mediaWin = MediaPlayer.create(WinActivity.this, R.raw.win_games);
        mediaWin.start();
        mediaWin.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btPlayAgain.setBackgroundResource(R.drawable.background_answer_when_click);
                btPlayAgain.setText("Chơi lại");
                btPlayAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(WinActivity.this, MainActivity.class));
                    }
                });
            }
        });
    }
}