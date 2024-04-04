package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class OwnerTypeOfRoom extends AppCompatActivity {
    LinearLayout llTro,llGhep,llChungCu,llNguyenCan;
    Boolean isSelectTro = false;
    Boolean isSelectGhep = false;
    Boolean isSelectChungCu = false;
    Boolean isSelectNguyenCan = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_type_of_room);
        llTro = findViewById(R.id.llTro);
        llGhep = findViewById(R.id.llGhep);
        llChungCu = findViewById(R.id.llChungCu);
        llNguyenCan = findViewById(R.id.llNguyenCan);
        llTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectTro = true;
                isSelectGhep = false;
                isSelectChungCu=false;
                isSelectNguyenCan= false;
                capNhatTrangThai();
            }
        });
        llGhep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectTro = false;
                isSelectGhep = true;
                isSelectChungCu=false;
                isSelectNguyenCan= false;
                capNhatTrangThai();
            }
        });
        llChungCu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectTro = false;
                isSelectGhep = false;
                isSelectChungCu=true;
                isSelectNguyenCan= false;
                capNhatTrangThai();
            }
        });
        llNguyenCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectTro = false;
                isSelectGhep = false;
                isSelectChungCu=false;
                isSelectNguyenCan= true;
                capNhatTrangThai();
            }
        });
    }
    private void capNhatTrangThai(){
        llNguyenCan.setSelected(isSelectNguyenCan);
        llChungCu.setSelected(isSelectChungCu);
        llGhep.setSelected(isSelectGhep);
        llTro.setSelected(isSelectTro);
    }
}