package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {
    CheckBox cbDongY;
    Button btnTiepTuc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cbDongY = findViewById(R.id.cbDongY);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        cbDongY.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnTiepTuc.setEnabled(isChecked);
            }
        });
    }
}