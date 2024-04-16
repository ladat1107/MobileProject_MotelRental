package com.motel.mobileproject_motelrental;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.motel.mobileproject_motelrental.databinding.ActivitySignUpBinding;


import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private ActivitySignUpBinding binding;
    StorageReference storageReference;
    private PreferenceManager preferenceManager;
    private Uri imageUri;
    private boolean succ;
    private String ImageID;
    private boolean selectedGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PhoneNumberValidity();
        initDatePicker();
        binding.datePickerButton.setText(getTodaysDate());
        setListeners();

    }

    private void setListeners() {
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUpDetails() && uploadImage(imageUri))
                signUp();
        });
        binding.rgpGioiTinh.setOnCheckedChangeListener((group, checkedId) -> {
                selectedGender = checkedId==0?false:true;
        });
    }
    private boolean uploadImage(Uri file) {

        ImageID = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child("images/" + ImageID);
        ref.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                succ = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Failed!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                succ = false;
            }
        });
        return succ;
    }
    private void signUp() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, binding.inputName.getText().toString());
        user.put(Constants.KEY_GENDER, selectedGender);
        user.put(Constants.KEY_BIRTHDAY, binding.datePickerButton.getText().toString());
        user.put(Constants.KEY_HOUSE_NUMBER, binding.inputSoNha.getText().toString());
        user.put(Constants.KEY_WARD,binding.cmbXa.getSelectedItem().toString());
        user.put(Constants.KEY_DISTRICT,binding.cmbQuan.getSelectedItem().toString());
        user.put(Constants.KEY_CITY,binding.cmbTinh.getSelectedItem().toString());
        user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        user.put(Constants. KEY_IMAGE, ImageID);
        user.put(Constants.KEY_PHONE_NUMBER,binding.editTextCarrierNumber.getText().toString());
        user.put(Constants.KEY_STATUS_USER, 1);
        database.collection(Constants. KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                    preferenceManager.putBoolean(Constants.KEY_GENDER, selectedGender);
                    preferenceManager.putString(Constants.KEY_BIRTHDAY, binding.datePickerButton.getText().toString());
                    preferenceManager.putString(Constants.KEY_HOUSE_NUMBER, binding.inputSoNha.getText().toString());
                    preferenceManager.putString(Constants.KEY_WARD,binding.cmbXa.getSelectedItem().toString());
                    preferenceManager.putString(Constants.KEY_DISTRICT,binding.cmbQuan.getSelectedItem().toString());
                    preferenceManager.putString(Constants.KEY_CITY,binding.cmbTinh.getSelectedItem().toString());
                    preferenceManager.putString(Constants.KEY_EMAIL,  binding.inputEmail.getText().toString());
                    preferenceManager.putString(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, ImageID);
                    preferenceManager.putString(Constants.KEY_PHONE_NUMBER, binding.editTextCarrierNumber.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent. FLAG_ACTIVITY_NEW_TASK | Intent. FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });
    }
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.buttonSignUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private Boolean isValidSignUpDetails() {
        if (imageUri == null) {
            showToast("Select profile image");
            return false;
        } else if (binding.inputName.getText().toString().trim().isEmpty()) {
            showToast("Enter name");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm your password");
        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("Password & confirm password must be same");
            return false;
        }
        return true;
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                imageUri = result.getData().getData();
                Glide.with(getApplicationContext()).load(imageUri).into(binding.imageProfile);
            }
        }
    });

    private void PhoneNumberValidity() {
        binding.ccp.registerCarrierNumberEditText(binding.editTextCarrierNumber);

        binding.ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber) {
                    binding.imgCheckPhone.setImageResource(R.drawable.hn_ic_check);
                } else {
                    binding.imgCheckPhone.setImageResource(R.drawable.hn_ic_close);
                }
            }
        });
        binding.editTextCarrierNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                binding.imgCheckPhone.setVisibility(!input.isEmpty() ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return day + " /" + month + " /" + year;
    }


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                binding.datePickerButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}