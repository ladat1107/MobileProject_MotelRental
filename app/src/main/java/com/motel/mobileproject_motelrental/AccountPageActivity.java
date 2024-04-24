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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.motel.mobileproject_motelrental.databinding.ActivityHomePageBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

import java.util.HashMap;

public class AccountPageActivity extends AppCompatActivity {

    Button btnDanhSachYeuThich, btnDangTro, btnTroDaDang, btnDoiThongTin, btnDangXuat, btnThayDoiMatKhau, btnDoiAvatar, btnHome;
    ImageView imgAvatar;
    TextView txtName, txtDateOfBirth, txtAddress;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference;

    BottomNavigationView bottomNavigation;
    PreferenceManager preferenceManager;
    HomePageActivity homePageActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
        preferenceManager = new PreferenceManager(getApplicationContext());
        ChuyenSangDangTro();
        ChuyenSangDanhSachPhongDang();
        ChuyenSangDanhSachYeuThich();
        ChuyenSangDoiMatKhau();
        ChuyenSangDoiThongTin();
        ChuyenSangDoiAvatar();
        GetAvatarOnFireBase();
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnDangXuat.setOnClickListener(v -> {
            showToast("Signing out...");
        preferenceManager = new PreferenceManager(getApplicationContext());
            db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
            HashMap<String, Object> updates = new HashMap<>();
            updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
            documentReference.update(updates).addOnSuccessListener(unused -> {preferenceManager.clear();
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));}).addOnFailureListener(e -> showToast("Unable to sign out"));
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void ChuyenSangDangTro() {
        btnDangTro = (Button) findViewById(R.id.btnDangPhong);
        btnDangTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPageActivity.this, OwnerTypeOfRoomActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ChuyenSangDanhSachPhongDang() {
        btnTroDaDang = (Button) findViewById(R.id.btnPhongDaDang);
        btnTroDaDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPageActivity.this, RoomPostedListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ChuyenSangDanhSachYeuThich() {
        btnDanhSachYeuThich = (Button) findViewById(R.id.btnDanhSachYeuThich);
        btnDanhSachYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPageActivity.this, ListFavoriteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ChuyenSangDoiAvatar() {
        btnDoiAvatar = (Button) findViewById(R.id.btnDoiAvatar);
        btnDoiAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPageActivity.this, ChangeAvatarActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void ChuyenSangDoiMatKhau() {
        btnThayDoiMatKhau = (Button) findViewById(R.id.btnThayDoiMatKhau);
        btnThayDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPageActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ChuyenSangDoiThongTin() {
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
        DocumentReference docRef = db.collection("users").document(preferenceManager.getString(Constants.KEY_USER_ID));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
                        storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference  pathReference = storageReference.child("images/"+document.getString(Constants.KEY_IMAGE));
                        pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            Picasso.get().load(uri).into(imgAvatar);
                        }).addOnFailureListener(exception -> {});
                        Log.d(TAG, "DocumentSnapshot data: " + pathReference);
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