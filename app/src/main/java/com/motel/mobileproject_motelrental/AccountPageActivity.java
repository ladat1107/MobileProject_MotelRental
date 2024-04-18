package com.motel.mobileproject_motelrental;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AccountPageActivity extends AppCompatActivity {
    Button btnDanhSachYeuThich, btnDangTro, btnTroDaDang, btnDoiThongTin, btnDangXuat, btnThayDoiMatKhau, btnDoiAvatar;
    ImageView imgAvatar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_page);
        ChuyenSangDanhSachYeuThich();
        ChuyenSangDoiMatKhau();
        ChuyenSangDoiThongTin();
        ChuyenSangDoiAvatar();
        GetAvatarOnFireBase();
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
        btnDoiAvatar = (Button) findViewById(R.id.btnDoiAvatar);
        btnDoiAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPageActivity.this, ChangeAvatarActivity.class);
                startActivityForResult(intent, 1);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Reload the page here
                recreate(); // Or any other method to reload the page
            }
        }
    }
    private void GetAvatarOnFireBase(){
        DocumentReference docRef = db.collection("users").document("hdUDaeIQeIbErYFNakZw");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
                        StorageReference httpsReference = storage.getReferenceFromUrl(document.getString("image"));
                        httpsReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            Picasso.get().load(uri).into(imgAvatar);
                        }).addOnFailureListener(exception -> {});
                        Log.d(TAG, "DocumentSnapshot data: " + document.getString("image"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}