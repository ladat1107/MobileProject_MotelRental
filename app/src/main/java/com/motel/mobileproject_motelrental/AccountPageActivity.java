package com.motel.mobileproject_motelrental;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AccountPageActivity extends AppCompatActivity {
    Button btnDanhSachYeuThich, btnDangTro, btnTroDaDang, btnDoiThongTin, btnDangXuat, btnThayDoiMatKhau;
    ImageButton btnDoiAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_page);

        ChuyenSangDanhSachYeuThich();
        ChuyenSangDoiMatKhau();
        ChuyenSangDoiThongTin();
        ChuyenSangDoiAvatar();
    }
    private void ChuyenSangDanhSachYeuThich(){
        btnDanhSachYeuThich = (Button) findViewById(R.id.btnDanhSachYeuThich);
        btnDanhSachYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPageActivity.this, ListFavoriteActivity.class);
                startActivity(intent);
            }
        });
    }
    private void ChuyenSangDoiAvatar(){
        btnDoiAvatar = (ImageButton) findViewById(R.id.imbAvatar);
        btnDoiAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPageActivity.this, ChangeAvatarActivity.class);
                startActivity(intent);
            }
        });
    }
    private void ChuyenSangDoiMatKhau(){
        btnThayDoiMatKhau = (Button) findViewById(R.id.btnThayDoiMatKhau);
        btnThayDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPageActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    private void ChuyenSangDoiThongTin(){
        btnDoiThongTin = (Button) findViewById(R.id.btnThayDoiThongTin);
        btnDoiThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPageActivity.this, ChangeInfomationActivity.class);
                startActivity(intent);
            }
        });
    }
}