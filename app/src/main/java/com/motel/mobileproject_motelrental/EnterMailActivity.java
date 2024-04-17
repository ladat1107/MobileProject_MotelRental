package com.motel.mobileproject_motelrental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.motel.mobileproject_motelrental.databinding.ActivityEnterMailBinding;

public class EnterMailActivity extends AppCompatActivity {

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
            if (binding.inputEmail.toString().trim().isEmpty())
                showToast("Vui lòng nhập địa chỉ email!");
            else {
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.toString())
                        .whereEqualTo(Constants.KEY_STATUS_USER, true)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null
                                    && !task.getResult().getDocuments().isEmpty()) {
                                Intent intent = new Intent(getApplicationContext(), VerificateActivity.class);
                                intent.putExtra("email", binding.inputEmail.toString().trim());
                                startActivity(intent);
                            } else {
                                showToast("Email không được tìm thấy!");
                            }
                        });
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}