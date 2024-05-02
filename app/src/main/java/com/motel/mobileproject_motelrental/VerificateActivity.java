package com.motel.mobileproject_motelrental;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.motel.mobileproject_motelrental.Interface.BitmapCallback;
import com.motel.mobileproject_motelrental.databinding.ActivityVerificateBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;
import java.lang.Thread;

import javax.security.auth.login.LoginException;

public class VerificateActivity extends AppCompatActivity {
    private static String TAG = "VerificateActivity";
    private ActivityVerificateBinding binding;
    private CountdownTimerHelper countdownTimerHelper;
    private PreferenceManager preferenceManager;
    FirebaseFirestore database;
    private String encodedImage;
    StorageReference storageReference;
    private String ImageID;

    private String code = null;
    HashMap<String, Object> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.txtEmail.setText(getIntent().getStringExtra("email"));
        user = new HashMap<>();
        setListener();
        initState();

    }

    private void initState() {

        code = generateRandomCode.RandomCode();
        new SendMail().buttonSendEmail(getIntent().getStringExtra("email"), code);
        CountDownTimer();
    }

    private void Continue() {
        binding.progressBar.setVisibility(View.VISIBLE);
        String inputCode = binding.inputCode1.getText().toString() +
                binding.inputCode2.getText().toString() +
                binding.inputCode3.getText().toString() +
                binding.inputCode4.getText().toString() +
                binding.inputCode5.getText().toString() +
                binding.inputCode6.getText().toString();
        if (code != null && code.equals(inputCode)) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            code = null;
            if (getIntent().getStringExtra("type").equals("DoiMatKhau")) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                intent.putExtra("userID", getIntent().getStringExtra("userID"));
                startActivity(intent);
            } else {
                try {
                    signUp();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void signUp() throws FileNotFoundException {
        preferenceManager = new PreferenceManager(getApplicationContext());
        uploadImage(getIntent().getParcelableExtra("uriImage"));
    }

    private void log(String message) {
        Log.e("Quy trình: ", message);
    }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void uploadImage(Uri file) {
        if (file == null) {
            if (!preferenceManager.getBoolean(Constants.KEY_GENDER))
                ImageID = "avatar-nam.jpg";
            else
                ImageID = "avatar-nu.jpg";
            endcodeImage(ImageID, bitmap -> {
                if (bitmap != null) {
                    encodedImage = bitmapToBase64(bitmap);
                }
            });
        } else {
            ImageID = UUID.randomUUID().toString();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference ref = storageReference.child("images/" + ImageID);
            ref.putFile(file).addOnSuccessListener(taskSnapshot -> {
            });
            try {
                InputStream inputStream = getContentResolver().openInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                encodedImage = bitmapToBase64(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
        preferenceManager.putString(Constants.KEY_IMAGE_NOBASE64,ImageID);
        user.put(Constants.KEY_IMAGE, ImageID);
        database = FirebaseFirestore.getInstance();
        user.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
        user.put(Constants.KEY_GENDER, preferenceManager.getBoolean(Constants.KEY_GENDER));
        user.put(Constants.KEY_BIRTHDAY, preferenceManager.getString(Constants.KEY_BIRTHDAY));
        user.put(Constants.KEY_HOUSE_NUMBER, preferenceManager.getString(Constants.KEY_HOUSE_NUMBER));
        user.put(Constants.KEY_WARD, preferenceManager.getString(Constants.KEY_WARD));
        user.put(Constants.KEY_DISTRICT, preferenceManager.getString(Constants.KEY_DISTRICT));
        user.put(Constants.KEY_CITY, preferenceManager.getString(Constants.KEY_CITY));
        user.put(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL));
        user.put(Constants.KEY_PASSWORD, preferenceManager.getString(Constants.KEY_PASSWORD));
        user.put(Constants.KEY_PHONE_NUMBER, preferenceManager.getString(Constants.KEY_PHONE_NUMBER));
        user.put(Constants.KEY_STATUS_USER, true);

        database.collection(Constants.KEY_COLLECTION_USERS).add(user).addOnSuccessListener(documentReference -> {
            showToast("Thành công");
            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
            preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            finish();
        }).addOnFailureListener(exception -> {
            showToast("Đăng ký không thành công");
        });
    }

    private void endcodeImage(String ID, BitmapCallback callback) {
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + ID);
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localfile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());

                // Gọi callback và chuyển bitmap tới nó
                if (callback != null) {
                    callback.onBitmapLoaded(bitmap);
                }
            }).addOnFailureListener(exception -> {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setListener() {
        binding.btnVerify.setOnClickListener(v -> {
            Continue();
        });
        binding.txtResend.setOnClickListener(v -> {
            binding.txtResend.setEnabled(false);
            initState();
        });

        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty() && count > 0)
                    binding.inputCode2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty() && count > 0)
                    binding.inputCode3.requestFocus();
                else binding.inputCode1.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty() && count > 0)
                    binding.inputCode4.requestFocus();
                else binding.inputCode2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty() && count > 0)
                    binding.inputCode5.requestFocus();
                else binding.inputCode3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty() && count > 0)
                    binding.inputCode6.requestFocus();
                else binding.inputCode4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty() && count > 0 && count != 0)
                    Continue();
                else binding.inputCode5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void CountDownTimer() {
        long totalTimeInMillis = 60; // Thời gian đếm ngược: 60 giây
        long interval = 1; // Khoảng cách giữa các lần gọi onTick: 1 giây
        countdownTimerHelper = new CountdownTimerHelper(totalTimeInMillis, interval, new CountdownTimerHelper.CountdownCallback() {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.txtNumber.setText(" sau: " + String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onCountdownFinished() {
                binding.txtResend.setEnabled(true);
                binding.txtNumber.setText("");
                code = null;
            }
        });
        countdownTimerHelper.startCountdown();
    }

}

