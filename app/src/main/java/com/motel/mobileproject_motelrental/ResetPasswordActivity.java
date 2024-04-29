package com.motel.mobileproject_motelrental;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.motel.mobileproject_motelrental.Interface.BitmapCallback;
import com.motel.mobileproject_motelrental.databinding.ActivityResetPasswordBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ResetPasswordActivity extends AppCompatActivity {

    private static String TAG = "ResetPasswordActivity";
    private ActivityResetPasswordBinding binding;
    PreferenceManager preferenceManager;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListener();

    }

    private void setListener() {
        binding.btnBack.setOnClickListener(v ->
                onBackPressed()
        );
        binding.btnVerify.setOnClickListener(v -> {
            if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
                binding.tbError.setText("Mật khẩu mới & xác nhận mật khẩu không trùng khớp");
                binding.tbError.setVisibility(View.VISIBLE);
            } else {

                binding.tbError.setVisibility(View.INVISIBLE);
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection(Constants.KEY_COLLECTION_USERS).document(getIntent().getStringExtra("userID"))
                        .update(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
                signIn();
            }
        });
    }

    private void signIn() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, getIntent().getStringExtra("email"))
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
                .whereEqualTo(Constants.KEY_STATUS_USER, true) // true là tài khoản còn hoạt động, false là ngừng hoạt động
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && !task.getResult().getDocuments().isEmpty()) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putBoolean(Constants.KEY_GENDER, documentSnapshot.getBoolean(Constants.KEY_GENDER));
                        preferenceManager.putString(Constants.KEY_BIRTHDAY, documentSnapshot.getString(Constants.KEY_BIRTHDAY));
                        preferenceManager.putString(Constants.KEY_HOUSE_NUMBER, documentSnapshot.getString(Constants.KEY_HOUSE_NUMBER));
                        preferenceManager.putString(Constants.KEY_WARD, documentSnapshot.getString(Constants.KEY_WARD));
                        preferenceManager.putString(Constants.KEY_DISTRICT, documentSnapshot.getString(Constants.KEY_DISTRICT));
                        preferenceManager.putString(Constants.KEY_CITY, documentSnapshot.getString(Constants.KEY_CITY));
                        preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
                        preferenceManager.putString(Constants.KEY_PASSWORD, documentSnapshot.getString(Constants.KEY_PASSWORD));
                        preferenceManager.putString(Constants.KEY_IMAGE_NOBASE64, documentSnapshot.getString(Constants.KEY_IMAGE));
                        preferenceManager.putString(Constants.KEY_PHONE_NUMBER, documentSnapshot.getString(Constants.KEY_PHONE_NUMBER));
                        endcodeImage(documentSnapshot.getString(Constants.KEY_IMAGE), bitmap -> {
                            if (bitmap != null) {
                                String base64String = bitmapToBase64(bitmap);
                                preferenceManager.putString(Constants.KEY_IMAGE, base64String);
                                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        loading(false);
                        showToast("Đăng nhập thất bại");
                    }
                });
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.btnVerify.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnVerify.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

    public String bitmapToBase64(Bitmap bitmap) {
        Log.e("Step3", "");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}