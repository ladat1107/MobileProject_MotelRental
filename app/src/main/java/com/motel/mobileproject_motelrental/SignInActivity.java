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
import com.motel.mobileproject_motelrental.databinding.ActivitySignInBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    StorageReference storageReference;
    private Bitmap bitmap;

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
                        displayavatar(documentSnapshot.getString(Constants.KEY_IMAGE), new BitmapCallback() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap) {
                                // Xử lý hình ảnh ở đây
                                if (bitmap != null) {
                                    Log.e("endcodeImage",bitmapToBase64(bitmap));
                                } else {
                                    Log.e("endcodeImage", "Bitmap is null");
                                }
                            }
                        });
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        preferenceManager.putString(Constants.KEY_PHONE_NUMBER, documentSnapshot.getString(Constants.KEY_PHONE_NUMBER));
                        Log.e("Preferences", "User Signed In: " + preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN));
                        Log.e("Preferences", "User ID: " + preferenceManager.getString(Constants.KEY_USER_ID));
                        Log.e("Preferences", "User Name: " + preferenceManager.getString(Constants.KEY_NAME));
                        Log.e("Preferences", "User Gender: " + preferenceManager.getBoolean(Constants.KEY_GENDER));
                        Log.e("Preferences", "User Birthday: " + preferenceManager.getString(Constants.KEY_BIRTHDAY));
                        Log.e("Preferences", "User House Number: " + preferenceManager.getString(Constants.KEY_HOUSE_NUMBER));
                        Log.e("Preferences", "User Ward: " + preferenceManager.getString(Constants.KEY_WARD));
                        Log.e("Preferences", "User District: " + preferenceManager.getString(Constants.KEY_DISTRICT));
                        Log.e("Preferences", "User City: " + preferenceManager.getString(Constants.KEY_CITY));
                        Log.e("Preferences", "User Email: " + preferenceManager.getString(Constants.KEY_EMAIL));
                        Log.e("Preferences", "User Password: " + preferenceManager.getString(Constants.KEY_PASSWORD));
                        Log.e("Preferences", "User Image: " + preferenceManager.getString(Constants.KEY_IMAGE));
                        Log.e("Preferences", "User Phone Number: " + preferenceManager.getString(Constants.KEY_PHONE_NUMBER));

                        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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

    private String endcodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void displayavatar(String ID, BitmapCallback callback) {
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + ID);
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localfile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                // Gọi callback và chuyển bitmap tới nó
                if(callback != null) {
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

    // Interface để định nghĩa callback
    interface BitmapCallback {
        void onBitmapLoaded(Bitmap bitmap);
    }
    public  String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}