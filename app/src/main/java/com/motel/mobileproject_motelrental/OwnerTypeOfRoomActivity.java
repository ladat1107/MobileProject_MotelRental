package com.motel.mobileproject_motelrental;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import com.motel.mobileproject_motelrental.Custom.ConfirmationDialogListener;
import com.motel.mobileproject_motelrental.Custom.CustomDialog;

public class OwnerTypeOfRoomActivity extends AppCompatActivity  {
    LinearLayout llTro, llGhep, llChungCu, llNguyenCan;
    Boolean isSelectTro = false;
    Boolean isSelectGhep = false;
    Boolean isSelectChungCu = false;
    Boolean isSelectNguyenCan = false;
    Button btnTiepTuc;
    ImageView imgBack;
    private PreferenceManager preferenceManager;
    int type = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_type_of_room);
        llTro = findViewById(R.id.llTro);
        llGhep = findViewById(R.id.llGhep);
        llChungCu = findViewById(R.id.llChungCu);
        llNguyenCan = findViewById(R.id.llNguyenCan);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        imgBack = findViewById(R.id.imgBack);
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadDataBack();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBack();
            }
        });
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectTro) type = 1;
                else if (isSelectChungCu) type = 2;
                else if (isSelectNguyenCan) type = 3;
                else if(isSelectGhep) type =4;
                if (type != -1) {
                    preferenceManager.putInt(Constants.KEY_TYPE_ID, type);
                    Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    showConfirmationDialog();
                }
            }
        });
        llTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectTro = true;
                isSelectGhep = false;
                isSelectChungCu = false;
                isSelectNguyenCan = false;
                capNhatTrangThai();
            }
        });
        llGhep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectTro = false;
                isSelectGhep = true;
                isSelectChungCu = false;
                isSelectNguyenCan = false;
                capNhatTrangThai();
            }
        });
        llChungCu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectTro = false;
                isSelectGhep = false;
                isSelectChungCu = true;
                isSelectNguyenCan = false;
                capNhatTrangThai();
            }
        });
        llNguyenCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectTro = false;
                isSelectGhep = false;
                isSelectChungCu = false;
                isSelectNguyenCan = true;
                capNhatTrangThai();
            }
        });
    }

    private void capNhatTrangThai() {
        llNguyenCan.setSelected(isSelectNguyenCan);
        llChungCu.setSelected(isSelectChungCu);
        llGhep.setSelected(isSelectGhep);
        llTro.setSelected(isSelectTro);
    }

    private void loadDataBack(){
        int typeBack = -1;
        if(preferenceManager.getInt(Constants.KEY_TYPE_ID)!=-1){
            typeBack = preferenceManager.getInt(Constants.KEY_TYPE_ID);
        }
        if(typeBack == 1) isSelectTro = true;
        else if (typeBack ==2) isSelectChungCu =true;
        else if (typeBack==3) isSelectNguyenCan =true;
        else if(typeBack==4) isSelectGhep=true;
        capNhatTrangThai();
    }
    public void showConfirmationDialog(){
        CustomDialog.showConfirmationDialog(this, R.drawable.img_ld_error, "THÔNG BÁO", "Bạn phải chọn loại trọ muốn đăng", true, new ConfirmationDialogListener() {
            @Override
            public void onOKClicked() {
            }
            @Override
            public void onCancelClicked() {
            }
        });
    }
    public void showDialogBack(){
        CustomDialog.showConfirmationDialog(this, R.drawable.img_ld_error, "THÔNG BÁO", "Không lưu thông tin trọ", false, new ConfirmationDialogListener() {
            @Override
            public void onOKClicked() {
                clearPrefernce();
                Intent intent = new Intent(OwnerTypeOfRoomActivity.this, HomePageActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelClicked() {

            }
        });
    }
    private void clearPrefernce() {
        preferenceManager.clearSpecificPreferences(
                Constants.KEY_TITLE,
                Constants.KEY_COUNT_LIKE,
                Constants.KEY_COUNT_AIRCONDITIONER,
                Constants.KEY_LATITUDE,
                Constants.KEY_LONGTITUDE,
                Constants.KEY_MOTEL_NUMBER,
                Constants.KEY_WARD_MOTEL,
                Constants.KEY_DISTRICT_MOTEL,
                Constants.KEY_CITY_MOTEL,
                Constants.KEY_PRICE,
                Constants.KEY_ELECTRICITY_PRICE,
                Constants.KEY_WATER_PRICE,
                Constants.KEY_EMPTY_DAY,
                Constants.KEY_ACREAGE,
                Constants.KEY_CHARACTERISTIC,
                Constants.KEY_DESCRIPTION,
                Constants.KEY_COUNT_FRIDGE,
                Constants.KEY_COUNT_WASHING_MACHINE,
                Constants.KEY_GARET,
                Constants.KEY_NO_HOST,
                Constants.KEY_PRICE_WIFI,
                Constants.KEY_PRICE_PARKING,
                Constants.KEY_START_TIME,
                Constants.KEY_END_TIME,
                Constants.KEY_STATUS_MOTEL,
                Constants.KEY_IMAGE_LIST,
                Constants.KEY_TYPE_ID
        );
    }
}