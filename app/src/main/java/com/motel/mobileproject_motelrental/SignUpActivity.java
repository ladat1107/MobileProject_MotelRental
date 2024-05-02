package com.motel.mobileproject_motelrental;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;
import com.motel.mobileproject_motelrental.databinding.ActivitySignUpBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity implements Comparator<String> {

    private static final String TAG = "SignUpActivity";
    private DatePickerDialog datePickerDialog;
    private ActivitySignUpBinding binding;
    StorageReference storageReference;
    private boolean isNotExistEmail = false;
    private PreferenceManager preferenceManager;
    private Uri imageUri = null;
    private String ImageID;
    private boolean trueNumber;
    private boolean selectedGender = false;
    private List<String> provinceList, districtList, wardList;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PhoneNumberValidity();
        loadJSONData();
        setUpSpinners();
        initDatePicker();
        binding.datePickerButton.setText(getTodaysDate());
        setListeners();
    }

    private void setListeners() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseFirestore.getInstance();
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUpDetails()) {
//                if(imageUri!=null)
//                    uploadImage(imageUri);
                chk();
            }
        });
        binding.rgpGioiTinh.setOnCheckedChangeListener((group, checkedId) -> {
            selectedGender = checkedId != 0;
        });
    }

    private void chk() {
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString().trim())
                .whereEqualTo(Constants.KEY_STATUS_USER, true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {
                        showToast("Email đã được đăng ký trước đây");
                    } else {
                        //uploadImage(imageUri);
                        signUp();
                    }
                });
    }

    private void uploadImage(Uri file) {
        if (file == null) {
            if (!preferenceManager.getBoolean(Constants.KEY_GENDER))
                ImageID = "avatar-nam.jpg";
            else
                ImageID = "avatar-nu.jpg";
        } else {
            ImageID = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/" + ImageID);
            ref.putFile(file).addOnSuccessListener(taskSnapshot -> {
            }).addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Failed!" + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
    private void log(String message) {
        Log.e("Quy trình: ", message);
    }
    private void signUp() {
        preferenceManager.putString(Constants.KEY_IMAGE_NOBASE64, ImageID);
        preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
        preferenceManager.putBoolean(Constants.KEY_GENDER, selectedGender);
        preferenceManager.putString(Constants.KEY_BIRTHDAY, binding.datePickerButton.getText().toString());
        preferenceManager.putString(Constants.KEY_HOUSE_NUMBER, binding.inputSoNha.getText().toString());
        preferenceManager.putString(Constants.KEY_WARD, binding.cmbXa.getSelectedItem().toString());
        preferenceManager.putString(Constants.KEY_DISTRICT, binding.cmbQuan.getSelectedItem().toString());
        preferenceManager.putString(Constants.KEY_CITY, binding.cmbTinh.getSelectedItem().toString());
        preferenceManager.putString(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        preferenceManager.putString(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        preferenceManager.putString(Constants.KEY_PHONE_NUMBER, binding.editTextCarrierNumber.getText().toString());
        Intent intent = new Intent(getApplicationContext(), VerificateActivity.class);
        intent.putExtra("email", binding.inputEmail.getText().toString());
        intent.putExtra("uriImage", imageUri);
        intent.putExtra("type", "DangKy");
        startActivity(intent);

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
        if (binding.inputName.getText().toString().trim().isEmpty()) {
            showToast("Nhập họ tên");
            return false;
        } else if (binding.cmbTinh.getSelectedItemPosition() == 0) {
            showToast("Chọn tỉnh thành phố");
            return false;
        } else if (binding.cmbQuan.getSelectedItemPosition() == 0) {
            showToast("Chọn quận huyện");
            return false;
        } else if (binding.cmbXa.getSelectedItemPosition() == 0) {
            showToast("Chọn phường xã");
            return false;
        } else if (binding.editTextCarrierNumber.getText().toString().trim().isEmpty()) {
            showToast("Nhập số điện thoại");
            return false;
        } else if (!trueNumber) {
            showToast("Số điện thoại không hợp lệ");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Nhập email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Email không hợp lệ");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Nhập mật khẩu");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Nhập mật khẩu để xác nhận lại");
        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("Mật khẩu & xác nhận mật khẩu phải giống nhau");
            return false;
        } else if (binding.inputPassword.getText().length()< 6) {
            showToast("Mật khẩu phải lớn hơn hoặc bằng 6 ký tự");
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
                binding.textAddImage.setVisibility(View.GONE);
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
                    trueNumber = true;
                } else {
                    binding.imgCheckPhone.setImageResource(R.drawable.hn_ic_close);
                    trueNumber = false;
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

    private void loadJSONData() {
        try {
            InputStream inputStream = getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            parseJSON(json);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJSON(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray dataArray = jsonObject.getJSONArray("data");
        provinceList = new ArrayList<>();

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject provinceObject = dataArray.getJSONObject(i);
            String provinceName = provinceObject.getString("name");
            provinceList.add(provinceName);
        }
    }

    private void setUpSpinners() {
        provinceList.add("-- Chọn tỉnh thành phố --");
        Collections.sort(provinceList, SignUpActivity.this);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceList);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.cmbTinh.setAdapter(provinceAdapter);


        binding.cmbTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvince = (String) parent.getItemAtPosition(position);
                loadDistricts(selectedProvince);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.cmbQuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDistrict = (String) parent.getItemAtPosition(position);
                loadWards(selectedDistrict);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadDistricts(String selectedProvince) {
        districtList = new ArrayList<>();
        districtList.add("-- Chọn quận huyện --");
        // Find the province object in JSON data
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset("data.json"));
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject provinceObject = dataArray.getJSONObject(i);
                String provinceName = provinceObject.getString("name");
                if (provinceName.equals(selectedProvince)) {
                    JSONArray districtArray = provinceObject.getJSONArray("level2s");
                    for (int j = 0; j < districtArray.length(); j++) {
                        JSONObject districtObject = districtArray.getJSONObject(j);
                        String districtName = districtObject.getString("name");
                        districtList.add(districtName);
                    }
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districtList);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.cmbQuan.setAdapter(districtAdapter);

    }

    private void loadWards(String selectedDistrict) {
        wardList = new ArrayList<>();
        wardList.add("-- Chọn phường xã --");
        // Find the district object in JSON data
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset("data.json"));
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject provinceObject = dataArray.getJSONObject(i);
                JSONArray districtArray = provinceObject.getJSONArray("level2s");
                for (int j = 0; j < districtArray.length(); j++) {
                    JSONObject districtObject = districtArray.getJSONObject(j);
                    String districtName = districtObject.getString("name");
                    if (districtName.equals(selectedDistrict)) {
                        JSONArray wardArray = districtObject.getJSONArray("level3s");
                        for (int k = 0; k < wardArray.length(); k++) {
                            JSONObject wardObject = wardArray.getJSONObject(k);
                            String wardName = wardObject.getString("name");
                            wardList.add(wardName);
                        }
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wardList);
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.cmbXa.setAdapter(wardAdapter);
    }

    private String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream inputStream = getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public int compare(String s1, String s2) {
        String city1 = s1.replaceAll("^(Thành phố|Tỉnh)\\s*", "");
        String city2 = s2.replaceAll("^(Thành phố|Tỉnh)\\s*", "");

        // So sánh theo thứ tự từ A đến Z
        return city1.compareToIgnoreCase(city2);
    }
}