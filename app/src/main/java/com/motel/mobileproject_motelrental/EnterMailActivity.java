package com.motel.mobileproject_motelrental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.motel.mobileproject_motelrental.databinding.ActivityEnterMailBinding;

public class EnterMailActivity extends AppCompatActivity {

    private static String TAG = "EnterMailActivity";
    private ActivityEnterMailBinding binding;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnterMailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        setListener();
    }
    private void setListener() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnContinue.setOnClickListener(v -> {
            loading(true);
            if (binding.inputEmail.toString().trim().isEmpty())
            {
                binding.tbError.setText("Vui lòng nhập địa chỉ email!");
                binding.tbError.setVisibility(View.VISIBLE);
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches())
            {
                binding.tbError.setText("Email không hợp lệ");
                binding.tbError.setVisibility(View.VISIBLE);
            }
            else {
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString().trim())
                        .whereEqualTo(Constants.KEY_STATUS_USER, true)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null
                                    && !task.getResult().getDocuments().isEmpty()) {
                                binding.tbError.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(getApplicationContext(), VerificateActivity.class);
                                intent.putExtra("email", binding.inputEmail.getText().toString());
                                intent.putExtra("userID", task.getResult().getDocuments().get(0).getId());
                                intent.putExtra("type","DoiMatKhau");
                                startActivity(intent);
                            }
                            else {
                                binding.tbError.setText("Email không tồn tại");
                                binding.tbError.setVisibility(View.VISIBLE);
                            }
                        });
            }
            loading(false);
        });
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void loading (Boolean isLoading){
        if (isLoading) {
            binding.btnContinue.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnContinue.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}