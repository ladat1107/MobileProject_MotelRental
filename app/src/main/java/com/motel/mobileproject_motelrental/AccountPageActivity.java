package com.motel.mobileproject_motelrental;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.motel.mobileproject_motelrental.HomePageActivity.STORAGE_PERMISSION_CODE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.motel.mobileproject_motelrental.Interface.BitmapCallback;
import com.motel.mobileproject_motelrental.databinding.ActivityAccountPageBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AccountPageActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE_FOR_DANGTRO = 1004;
    Button btnDanhSachYeuThich, btnDangTro, btnTroDaDang, btnDoiThongTin, btnDangXuat, btnThayDoiMatKhau, btnDoiAvatar, btnHome;
    ImageView imgAvatar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference;
    ActivityAccountPageBinding binding;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        ChuyenSangDangTro();
        ChuyenSangDanhSachPhongDang();
        ChuyenSangDanhSachYeuThich();
        ChuyenSangDoiMatKhau();
        ChuyenSangDoiThongTin();
        ChuyenSangDoiAvatar();
        MenuClick();
        ShowInformation();
        GetAvatarOnFireBase();
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnDangXuat.setOnClickListener(v -> {
            showToast("Signing out...");
            preferenceManager = new PreferenceManager(getApplicationContext());
            db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
            HashMap<String, Object> updates = new HashMap<>();
            updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
            documentReference.update(updates).addOnSuccessListener(unused -> {
                preferenceManager.clear();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }).addOnFailureListener(e -> showToast("Unable to sign out"));
        });

    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void ShowInformation() {
        imgAvatar = findViewById(R.id.imgAvatar);
        imgAvatar.setImageBitmap(getBitmapFromEncodedString(preferenceManager.getString(Constants.KEY_IMAGE)));
        binding.tvUserName.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.tvAddress.setText("Địa chỉ: " + preferenceManager.getString(Constants.KEY_CITY) + ", " +
                preferenceManager.getString(Constants.KEY_DISTRICT) + ", " + preferenceManager.getString(Constants.KEY_WARD));
        binding.tvDateOfBirth.setText("Ngày sinh nhật: " + preferenceManager.getString(Constants.KEY_BIRTHDAY));
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ;
        return bitmap;
    }

    private void ChuyenSangDangTro() {
        btnDangTro = (Button) findViewById(R.id.btnDangPhong);
        btnDangTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoreDT();
            }
        });
    }

    private void ChuyenSangDanhSachPhongDang() {
        btnTroDaDang = (Button) findViewById(R.id.btnPhongDaDang);

        btnTroDaDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStore();
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

    public void MenuClick() {
        binding.bottomNavigation.setItemIconTintList(null);
        binding.bottomNavigation.setSelectedItemId(R.id.user);
        binding.btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                finish();
            }
        });

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                finish();
                return true;
            } else if (id == R.id.message) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
            } else if (id == R.id.love) {
                startActivity(new Intent(getApplicationContext(), ListFavoriteActivity.class));
                finish();
                return true;
            } else if (id == R.id.user) {
                startActivity(new Intent(getApplicationContext(), AccountPageActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void endcodeImage(String ID, BitmapCallback callback) {
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + ID);
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localfile).addOnSuccessListener(taskSnapshot -> {
                Log.e("Step 1", "");
                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());

                // Gọi callback và chuyển bitmap tới nó
                if (callback != null) {
                    callback.onBitmapLoaded(bitmap);
                }
            }).addOnFailureListener(exception -> {
                // Xử lý lỗi nếu quá trình tải xuống thất bại
                Log.e("TAG", "Download failed: " + exception.getMessage());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void GetAvatarOnFireBase() {
        DocumentReference docRef = db.collection("users").document(preferenceManager.getString(Constants.KEY_USER_ID));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        endcodeImage(document.getString(Constants.KEY_IMAGE), bitmap -> {
                            if (bitmap != null) {
                                String base64String = bitmapToBase64(bitmap);
                                preferenceManager.putString(Constants.KEY_IMAGE, base64String);
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public String bitmapToBase64(Bitmap bitmap) {
        Log.e("Step3", "");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void requestStore() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            Intent intent = new Intent(getApplicationContext(), RoomPostedListActivity.class);
            startActivity(intent);
        }
    }

    private void requestStoreDT() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE_FOR_DANGTRO);
        } else {
            preferenceManager.putInt(Constants.KEY_COUNT_LIKE, 0);
            Intent intent = new Intent(getApplicationContext(), OwnerTypeOfRoomActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestStore();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE_FOR_DANGTRO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestStoreDT();
            }
        }
    }

}