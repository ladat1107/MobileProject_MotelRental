package com.motel.mobileproject_motelrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class OwnerTypeOfRoomActivity extends AppCompatActivity {
    private static final String TAG = "OwnerTypeOfRoomActivity";
    LinearLayout llTro,llGhep,llChungCu,llNguyenCan;
    Boolean isSelectTro = false;
    Boolean isSelectGhep = false;
    Boolean isSelectChungCu = false;
    Boolean isSelectNguyenCan = false;
    Button btnTiepTuc;
    StorageReference storageReference;
    private PreferenceManager preferenceManager ;
    private FirebaseFirestore db;
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
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = 1;
                if(isSelectTro) type = 1;
                else if(isSelectChungCu) type = 2;
                else if(isSelectNguyenCan) type = 3;
                else  type =4;
                Map<String, Object> data = new HashMap<>();
                data.put(Constants.KEY_TYPE_ID, String.valueOf(type));
                btnTiepTuc.setEnabled(false);
                // Tạo một tài liệu mới trong collection "example"
                db.collection("images").add(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    String id = task.getResult().getId();
                                    Intent intent = new Intent(OwnerTypeOfRoomActivity.this, LocationActivity.class);
                                    intent.putExtra("motelID", id);
                                    Log.e(TAG, "DocumentSnapshot successfully written!" );
                                    startActivity(intent);

                                } else {
                                    btnTiepTuc.setEnabled(true);
                                    Log.w(TAG, "Error writing document", task.getException());
                                    Toast.makeText(OwnerTypeOfRoomActivity.this, "Chọn loại phòng muốn đăng", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

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