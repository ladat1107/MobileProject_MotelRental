package com.motel.mobileproject_motelrental;
import android.os.CountDownTimer;

public class CountdownTimerHelper {
    private CountDownTimer countDownTimer;
    private long totalTimeInMillis;
    private long interval;
    private CountdownCallback countdownCallback;

    public CountdownTimerHelper(long totalTimeInMillis, long interval, CountdownCallback countdownCallback) {
        this.totalTimeInMillis = totalTimeInMillis*1000;
        this.interval = interval*1000;
        this.countdownCallback = countdownCallback;
    }

    public void startCountdown() {
        countDownTimer = new CountDownTimer(totalTimeInMillis, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownCallback.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                countdownCallback.onCountdownFinished();
            }
        };

        countDownTimer.start();
    }

    public void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public interface CountdownCallback {
        void onTick(long millisUntilFinished);
        void onCountdownFinished();
    }
}
