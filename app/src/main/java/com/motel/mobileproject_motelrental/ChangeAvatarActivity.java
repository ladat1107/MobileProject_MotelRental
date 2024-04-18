package com.motel.mobileproject_motelrental;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.ProgressDialog;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.motel.mobileproject_motelrental.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChangeAvatarActivity extends AppCompatActivity {
    Button btnBack, btnChonAnh, btnChinhSua;
    ImageView imvAvatar;
    ImageButton btnChupAnh, btnXoaAnh;
    Uri photo;
    ActivityMainBinding binding;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.thaydoiavatar);
        ChangeAvatarAction();
    }
    private void ChangeAvatarAction(){
        imvAvatar = findViewById(R.id.imgAvatar);
        GetBack();
        GetAvatarByCapture();
        DeleteImage();
        SelectImageByGallery();
        ChangeAvatar();
    }
    private void ChangeAvatar(){
        btnChinhSua = (Button) findViewById(R.id.btnDoiAvatar);
        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(photo != null) {
                        UploadPhotoToStorage("hdUDaeIQeIbErYFNakZw");
                        photo = null;
                        imvAvatar.setImageURI(null);
                        ReturnAccountPage();
                    }
                    else {
                        Toast.makeText(ChangeAvatarActivity.this, "Bạn chưa chọn ảnh đại diện!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {

                }
            }
        });
    }
    private void GetBack(){
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void SelectImageByGallery() {
        btnChonAnh = (Button) findViewById(R.id.btnChonTuThuVien);
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (ActivityCompat.checkSelfPermission(ChangeAvatarActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(ChangeAvatarActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    return;
                }
                startActivityForResult(intent,100);
            }
        });

    }
    private void GetAvatarByCapture(){
        btnChupAnh = findViewById(R.id.btnChupAnh);
        btnChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(ACTION_IMAGE_CAPTURE);
                if (ActivityCompat.checkSelfPermission(ChangeAvatarActivity.this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(ChangeAvatarActivity.this,new String[]{android.Manifest.permission.CAMERA}, 1);
                    return;
                }
                startActivityForResult(cameraIntent,99);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK)
        {
            photo = data.getData();
            imvAvatar.setImageURI(photo);
        }
        else if (requestCode == 99 && resultCode == Activity.RESULT_OK)
        {
            Bitmap photob = (Bitmap) data.getExtras().get("data");
            photo = getImageUri(ChangeAvatarActivity.this, photob);
            imvAvatar.setImageURI(photo);
        }
    }
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void UploadPhotoToStorage(String documentPath){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("avatar/"+fileName);
        storageReference.putFile(photo).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    DocumentReference userRef = db.collection("users").document(documentPath);
                    userRef.update("image", uri.toString());
                });
                Toast.makeText(ChangeAvatarActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(ChangeAvatarActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();
                }
            });
    }
    private void DeleteImage(){
        btnXoaAnh = (ImageButton) findViewById(R.id.btnXoaAnh);
        btnXoaAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvAvatar.setImageURI(null);
            }
        });
    }
    private void ReturnAccountPage(){
        Intent intent = new Intent(ChangeAvatarActivity.this, AccountPageActivity.class);
        startActivity(intent);
    }
}