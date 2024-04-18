package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.Map;

public class OwnerTypeOfRoomActivity extends AppCompatActivity {
    private static final String TAG = "OwnerTypeOfRoomActivity";
    LinearLayout llTro, llGhep, llChungCu, llNguyenCan;
    Boolean isSelectTro = false;
    Boolean isSelectGhep = false;
    Boolean isSelectChungCu = false;
    Boolean isSelectNguyenCan = false;
    Button btnTiepTuc;
    StorageReference storageReference;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore db;
    int type = -999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_type_of_room);
        llTro = findViewById(R.id.llTro);
        llGhep = findViewById(R.id.llGhep);
        llChungCu = findViewById(R.id.llChungCu);
        llNguyenCan = findViewById(R.id.llNguyenCan);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadDataBack();
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
        int typeBack = -999;
        if(preferenceManager.getInt(Constants.KEY_TYPE_ID)!=-1){
            typeBack = preferenceManager.getInt(Constants.KEY_TYPE_ID);
        }
        if(typeBack == 1) isSelectTro = true;
        else if (typeBack ==2) isSelectChungCu =true;
        else if (typeBack==3) isSelectNguyenCan =true;
        else if(typeBack==4) isSelectGhep=true;
        capNhatTrangThai();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn phải chọn loại trọ muốn đăng");
        builder.setIcon(R.drawable.warning);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}