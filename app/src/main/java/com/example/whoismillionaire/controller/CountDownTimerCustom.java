package com.example.whoismillionaire.controller;

import android.os.CountDownTimer;

public abstract class CountDownTimerCustom {
    private long millisInFuture = 0;
    private long millisRemaining = 0;    // Biến này lưu trữ thời gian cần đếm còn lại
    private long countdownInterval = 0;

    CountDownTimer countDownTimer = null;
    boolean isPause = true;

    public CountDownTimerCustom(long millisInFuture, long countdownInterval){
        super();
        this.millisInFuture = millisInFuture;
        this.millisRemaining = millisInFuture;
        this.countdownInterval = countdownInterval;
    }

    public abstract void onTick(long milisUntilFinished);
    public abstract void onFinish();

    private void createCountDown(){
        countDownTimer = new CountDownTimer(millisRemaining, countdownInterval) {
            @Override
            public void onTick(long l) {
                millisRemaining = l;
                CountDownTimerCustom.this.onTick(l);
            }

            @Override
            public void onFinish() {
                CountDownTimerCustom.this.onFinish();
            }
        };
    }

    public synchronized final CountDownTimerCustom start(){
        if(isPause){
            createCountDown();
            countDownTimer.start();
            isPause = false;
        }
        return this;
    }

    public void pause(){
        try {
            if(!isPause){
                countDownTimer.cancel();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        isPause = true;
    }

    public final void cancel(){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        isPause = true;
        this.millisRemaining = millisInFuture;
    }
}