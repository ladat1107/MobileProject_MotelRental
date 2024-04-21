package com.motel.mobileproject_motelrental;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.motel.mobileproject_motelrental.Adapter.TagAdapter;
import com.motel.mobileproject_motelrental.Custom.ConfirmationDialogListener;
import com.motel.mobileproject_motelrental.Custom.CustomDialog;
import com.motel.mobileproject_motelrental.Item.TagItem;

import java.util.ArrayList;
import java.util.List;

public class OwnerTypeOfRoomActivity extends AppCompatActivity {
    LinearLayout llTro, llGhep, llChungCu, llNguyenCan;
    Boolean isSelectTro = false;
    Boolean isSelectGhep = false;
    Boolean isSelectChungCu = false;
    Boolean isSelectNguyenCan = false;
    Button btnTiepTuc;
    ImageView imgBack;
    private String motelId;
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
        motelId = getIntent().getStringExtra("motelId");
        if (motelId != null) {
            getMotel();
        }
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
                else if (isSelectGhep) type = 4;
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

    private void getMotel() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_MOTELS).document(motelId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        preferenceManager.putString("motelIDTemp",motelId);
                        preferenceManager.putString(Constants.KEY_TITLE, document.getString(Constants.KEY_TITLE));
                        Long countLikeLong = document.getLong(Constants.KEY_COUNT_LIKE);
                        if (countLikeLong != null) {
                            int countLike = countLikeLong.intValue();
                            preferenceManager.putInt(Constants.KEY_COUNT_LIKE, countLike);
                        }
                        Long wifiprice = document.getLong(Constants.KEY_PRICE_WIFI);
                        if (wifiprice != null) {
                            int intWifi = wifiprice.intValue();
                            preferenceManager.putInt(Constants.KEY_PRICE_WIFI, intWifi);
                        }
                        Long parkingprice = document.getLong(Constants.KEY_PRICE_PARKING);
                        if (parkingprice != null) {
                            int intPark = parkingprice.intValue();
                            preferenceManager.putInt(Constants.KEY_PRICE_WIFI, intPark);
                        }
                        Long typeLong = document.getLong(Constants.KEY_TYPE_ID);
                        if (typeLong != null) {
                            int typeInt = typeLong.intValue();
                            preferenceManager.putInt(Constants.KEY_TYPE_ID, typeInt);
                            if (typeInt == 1) isSelectTro = true;
                            else if (typeInt == 2) isSelectChungCu = true;
                            else if (typeInt == 3) isSelectNguyenCan = true;
                            else if (typeInt == 4) isSelectGhep = true;
                            capNhatTrangThai();

                        }
                        preferenceManager.putInt(Constants.KEY_COUNT_AIRCONDITIONER, document.getLong(Constants.KEY_COUNT_AIRCONDITIONER).intValue());
                        preferenceManager.putFloat(Constants.KEY_LATITUDE, document.getDouble(Constants.KEY_LATITUDE).floatValue());
                        preferenceManager.putFloat(Constants.KEY_LONGTITUDE, document.getDouble(Constants.KEY_LONGTITUDE).floatValue());
                        preferenceManager.putString(Constants.KEY_MOTEL_NUMBER, document.getString(Constants.KEY_MOTEL_NUMBER));
                        preferenceManager.putInt(Constants.KEY_WARD_MOTEL, document.getLong(Constants.KEY_WARD_MOTEL).intValue());
                        preferenceManager.putInt(Constants.KEY_DISTRICT_MOTEL, document.getLong(Constants.KEY_DISTRICT_MOTEL).intValue());
                        preferenceManager.putInt(Constants.KEY_CITY_MOTEL, document.getLong(Constants.KEY_CITY_MOTEL).intValue());
                        preferenceManager.putString(Constants.KEY_CITY_NAME, document.getString(Constants.KEY_CITY_NAME));
                        preferenceManager.putString(Constants.KEY_DISTRICT_NAME, document.getString(Constants.KEY_DISTRICT_NAME));
                        preferenceManager.putString(Constants.KEY_WARD_NAME, document.getString(Constants.KEY_WARD_NAME));
                        preferenceManager.putInt(Constants.KEY_PRICE, document.getLong(Constants.KEY_PRICE).intValue());
                        preferenceManager.putInt(Constants.KEY_ELECTRICITY_PRICE, document.getLong(Constants.KEY_ELECTRICITY_PRICE).intValue());
                        preferenceManager.putInt(Constants.KEY_WATER_PRICE, document.getLong(Constants.KEY_WATER_PRICE).intValue());
                        preferenceManager.putString(Constants.KEY_EMPTY_DAY, document.getString(Constants.KEY_EMPTY_DAY));
                        preferenceManager.putFloat(Constants.KEY_ACREAGE, document.getDouble(Constants.KEY_ACREAGE).floatValue());
                        preferenceManager.putString(Constants.KEY_CHARACTERISTIC, document.getString(Constants.KEY_CHARACTERISTIC));
                        preferenceManager.putString(Constants.KEY_DESCRIPTION, document.getString(Constants.KEY_DESCRIPTION));
                        preferenceManager.putInt(Constants.KEY_COUNT_FRIDGE, document.getLong(Constants.KEY_COUNT_FRIDGE).intValue());
                        preferenceManager.putInt(Constants.KEY_COUNT_WASHING_MACHINE, document.getLong(Constants.KEY_COUNT_WASHING_MACHINE).intValue());
                        preferenceManager.putBoolean(Constants.KEY_GARET, document.getBoolean(Constants.KEY_GARET));
                        preferenceManager.putBoolean(Constants.KEY_NO_HOST, document.getBoolean(Constants.KEY_NO_HOST));
                        preferenceManager.putString(Constants.KEY_START_TIME, document.getString(Constants.KEY_START_TIME));
                        preferenceManager.putString(Constants.KEY_END_TIME, document.getString(Constants.KEY_END_TIME));
                        preferenceManager.putBoolean(Constants.KEY_STATUS_MOTEL, document.getBoolean(Constants.KEY_STATUS_MOTEL));
                    }
                }
            }
        });
    }


    private void capNhatTrangThai() {
        llNguyenCan.setSelected(isSelectNguyenCan);
        llChungCu.setSelected(isSelectChungCu);
        llGhep.setSelected(isSelectGhep);
        llTro.setSelected(isSelectTro);
    }

    private void loadDataBack() {
        int typeBack = -1;
        if (preferenceManager.getInt(Constants.KEY_TYPE_ID) != -1) {
            typeBack = preferenceManager.getInt(Constants.KEY_TYPE_ID);
        }
        if (typeBack == 1) isSelectTro = true;
        else if (typeBack == 2) isSelectChungCu = true;
        else if (typeBack == 3) isSelectNguyenCan = true;
        else if (typeBack == 4) isSelectGhep = true;
        capNhatTrangThai();
    }

    public void showConfirmationDialog() {
        CustomDialog.showConfirmationDialog(this, R.drawable.img_ld_error, "THÔNG BÁO", "Bạn phải chọn loại trọ muốn đăng", true, new ConfirmationDialogListener() {
            @Override
            public void onOKClicked() {
            }

            @Override
            public void onCancelClicked() {
            }
        });
    }

    public void showDialogBack() {
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
                "motelIDTemp",
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
                Constants.KEY_TYPE_ID,
                Constants.KEY_CITY_NAME,
                Constants.KEY_DISTRICT_NAME,
                Constants.KEY_WARD_NAME
        );
    }
}