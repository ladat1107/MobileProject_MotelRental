package com.motel.mobileproject_motelrental;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.motel.mobileproject_motelrental.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    boolean isPasswordVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.buttonSignIn.setOnClickListener(v -> {
            if (isValidSignInDetails())
                signIn();
        });
        binding.imgeye.setOnClickListener(v -> {
            if (isPasswordVisible) {
                binding.imgeye.setImageResource(R.drawable.eyeclose); // Thay đổi hình ảnh của ImageButton khi ẩn mật khẩu
                binding.inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                binding.imgeye.setImageResource(R.drawable.eye); // Thay đổi hình ảnh của ImageButton khi hiển thị mật khẩu
                binding.inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            isPasswordVisible = !isPasswordVisible; // Đảo ngược trạng thái
            binding.inputPassword.setSelection(binding.inputPassword.getText().length()); // Đặt con trỏ văn bản vào cuối});
        });
    }
        private void signIn () {
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
                            preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                            preferenceManager.putString(Constants.KEY_PHONE_NUMBER, documentSnapshot.getString(Constants.KEY_PHONE_NUMBER));
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            loading(false);
                            showToast("Đăng nhập thất bại");
                        }
                    });
        }

        private boolean isValidSignInDetails () {
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
        private void loading (Boolean isLoading){
            if (isLoading) {
                binding.buttonSignIn.setVisibility(View.INVISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.buttonSignIn.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.INVISIBLE);
            }
        }
        private void showToast (String message){
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }