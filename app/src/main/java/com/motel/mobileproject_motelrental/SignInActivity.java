package com.motel.mobileproject_motelrental;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.motel.mobileproject_motelrental.Interface.BitmapCallback;
import com.motel.mobileproject_motelrental.databinding.ActivitySignInBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
            finish();
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.forgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EnterMailActivity.class);
            startActivity(intent);
        });
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.buttonSignIn.setOnClickListener(v -> {
            if (isValidSignInDetails())
                signIn();
        });
    }

    private void signIn() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
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
                        preferenceManager.putString(Constants.KEY_PHONE_NUMBER, documentSnapshot.getString(Constants.KEY_PHONE_NUMBER));
                        preferenceManager.putString(Constants.KEY_IMAGE_NOBASE64, documentSnapshot.getString(Constants.KEY_IMAGE));
                        endcodeImage(documentSnapshot.getString(Constants.KEY_IMAGE), bitmap -> {
                            if (bitmap != null) {
                                Log.e("Step2", "");
                                String base64String = bitmapToBase64(bitmap);
                                preferenceManager.putString(Constants.KEY_IMAGE, base64String);
                                Log.e("Step4", "");
                                Log.e("Step5", "");
                                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                Log.e("Step6", "");
                            }
                        });
                    } else {
                        loading(false);
                        showToast("Đăng nhập thất bại");
                    }
                });
    }

    private boolean isValidSignInDetails() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Hãy nhập email!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Email không hợp lệ!");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Hãy nhập mật khẩu!");
            return false;
        } else {
            return true;
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.buttonSignIn.setVisibility(View.VISIBLE);
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