package com.motel.mobileproject_motelrental;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.motel.mobileproject_motelrental.databinding.ActivityVerificateBinding;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VerificateActivity extends AppCompatActivity {
    private static String TAG = "VerificateActivity";
    private ActivityVerificateBinding binding;
    private CountdownTimerHelper countdownTimerHelper;

    private String code = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.txtEmail.setText(getIntent().getStringExtra("email"));
        setListener();
        initState();

    }
    private void initState()
    {

        code = generateRandomCode.RandomCode();
        Log.e(TAG, "code: "+code);
        new SendMail().buttonSendEmail(getIntent().getStringExtra("email"),code);
        CountDownTimer();
    }
    private void Continue()
    {
        binding.main.setBackgroundColor(Color.parseColor("#C2C2C2"));
        binding.progressBar.setVisibility(View.VISIBLE);
        String inputCode = binding.inputCode1.getText().toString() +
                binding.inputCode2.getText().toString() +
                binding.inputCode3.getText().toString() +
                binding.inputCode4.getText().toString() +
                binding.inputCode5.getText().toString() +
                binding.inputCode6.getText().toString();
        Log.e(TAG, "inputCode: " + inputCode);
        if(code != null && code.equals(inputCode))
        {
            Log.e(TAG, "Xác thực thành công");
            binding.main.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.progressBar.setVisibility(View.INVISIBLE);
            code = null;
        }else
        {
            Log.e(TAG, "Xác thực thất bại");
            binding.main.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void setListener() {
        binding.btnVerify.setOnClickListener(v -> {
                Continue();
        });
        binding.txtResend.setOnClickListener(v -> {
            binding.txtResend.setEnabled(false);
            initState();
        });

        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                    binding.inputCode2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                    binding.inputCode3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                    binding.inputCode4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                    binding.inputCode5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                    binding.inputCode6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                    Continue();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void CountDownTimer ()
    {
        long totalTimeInMillis = 60; // Thời gian đếm ngược: 60 giây
        long interval = 1; // Khoảng cách giữa các lần gọi onTick: 1 giây
        countdownTimerHelper = new CountdownTimerHelper(totalTimeInMillis, interval, new CountdownTimerHelper.CountdownCallback() {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.txtNumber.setText(" sau: "+String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onCountdownFinished() {
                binding.txtResend.setEnabled(true);
                binding.txtNumber.setText("");
                code = null;
            }
        });
        countdownTimerHelper.startCountdown();
    }

}