package com.motel.mobileproject_motelrental;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChangeInfomationActivity extends AppCompatActivity implements Comparator<String> {
    private Spinner spinnerProvince, spinnerDistrict, spinnerWard;
    private List<String> provinceList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnBack;
    EditText edHoVaTen, edSoDienThoai, edSoNha;
    RadioButton rbNam, rbNu;
    Button btnChinhSua;
    String provinceUser, districtUser, wardUser;
    List<String> districtList, wardList;
    ProgressDialog progressDialog;
    TextInputLayout layoutHoVaTen, layoutSoDienThoai, layoutSoNha;
    PreferenceManager preferenceManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinhsuathongtin);
        spinnerProvince = findViewById(R.id.spTinhThanhPho);
        spinnerDistrict = findViewById(R.id.spQuanHuyen);
        spinnerWard = findViewById(R.id.spXaPhuong);
        edHoVaTen = (EditText) findViewById(R.id.etHoVaTen);
        edSoDienThoai = (EditText) findViewById(R.id.etSoDienThoai);
        edSoNha = (EditText) findViewById(R.id.etSoNha);
        rbNam = (RadioButton) findViewById(R.id.rbNam);
        rbNu = (RadioButton) findViewById(R.id.rbNu);
        layoutHoVaTen = findViewById(R.id.layoutHoVaTen);
        layoutSoDienThoai = findViewById(R.id.layoutSoDienThoai);
        layoutSoNha = findViewById(R.id.layoutSoNha);
        preferenceManager = new PreferenceManager(getApplicationContext());
        GetDataOnFireBase();
        loadJSONData();
        GetBack();
        UpdateInformation();
        EditTextAction(edHoVaTen, layoutHoVaTen);
        EditTextAction(edSoDienThoai, layoutSoDienThoai);
        EditTextAction(edSoNha, layoutSoNha);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void EditTextAction(EditText editText, TextInputLayout layout){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edHoVaTen.getWindowToken(), 0);
                }
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                layout.setBoxStrokeColor(Color.parseColor("#0c7094"));
                layout.setHelperText("");
                return false;
            }
        });
    }
    private void GetBack(){
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        provinceList.add("-- Chọn tỉnh/thành phố --");
        Collections.sort(provinceList, ChangeInfomationActivity.this);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceList);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(provinceAdapter);
        int userTinhIndex = provinceList.indexOf(provinceUser);
        if (userTinhIndex != -1) {
            provinceList.remove(userTinhIndex);
            provinceList.add(0, provinceUser);
            provinceAdapter.notifyDataSetChanged();
        }
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvince = (String) parent.getItemAtPosition(position);
                if(!provinceList.get(0).contains("--")){
                    selectedProvince = provinceUser;
                }
                loadDistricts(selectedProvince);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerProvince.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Collections.sort(provinceList, ChangeInfomationActivity.this);
                }
                return false;
            }
        });
    }

    private void loadDistricts(String selectedProvince) {
        districtList = new ArrayList<>();
        districtList.add("--Chọn quận/huyện --");
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
        spinnerDistrict.setAdapter(districtAdapter);
        int userDistrict = districtList.indexOf(districtUser);
        if (userDistrict != -1) {
            districtList.remove(userDistrict);
            districtList.add(0, districtUser);
            districtAdapter.notifyDataSetChanged();
        }
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDistrict = (String) parent.getItemAtPosition(position);
                if(!districtList.get(0).contains("--")){
                    selectedDistrict = districtUser;
                }
                loadWards(selectedDistrict);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerDistrict.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Collections.sort(districtList, ChangeInfomationActivity.this);
                }
                return false;
            }
        });
    }

    private void loadWards(String selectedDistrict) {
        wardList = new ArrayList<>();
        wardList.add("-- Chọn xã/phường --");
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
        spinnerWard.setAdapter(wardAdapter);
        int userWard = wardList.indexOf(wardUser);
        if (userWard != -1) {
            wardList.remove(userWard);
            wardList.add(0, wardUser);
            wardAdapter.notifyDataSetChanged();
        }
        spinnerWard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Collections.sort(districtList, ChangeInfomationActivity.this);
                }
                return false;
            }
        });
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
        return city1.compareToIgnoreCase(city2);
    }
    private void UpdateInformation(){
        btnChinhSua = (Button) findViewById(R.id.btnChinhSua);
        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserInformation();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(btnChinhSua.getWindowToken(), 0);
                edSoNha.clearFocus();
                edHoVaTen.clearFocus();
                edSoDienThoai.clearFocus();
            }
        });
    }
    private  void UpdateUserInformation(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating...");
        progressDialog.show();
        try {
            int count = 0;
            DocumentReference docRef = db.collection("users").document(preferenceManager.getString(Constants.KEY_USER_ID));
            if(edHoVaTen.getText().toString().isEmpty()){
                edHoVaTen.requestFocus();
                layoutHoVaTen.setBoxStrokeColor(Color.parseColor("#ff5d6c"));
                layoutHoVaTen.setHelperText("Bắt buộc!");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                return;
            }
            else if(edSoNha.getText().toString().isEmpty()){
                edHoVaTen.requestFocus();
                layoutSoNha.setBoxStrokeColor(Color.parseColor("#ff5d6c"));
                layoutSoNha.setHelperText("Bắt buộc!");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                return;
            }
            String phoneNumber = edSoDienThoai.getText().toString();
            if(phoneNumber.isEmpty()){
                edSoDienThoai.requestFocus();
                layoutSoDienThoai.setBoxStrokeColor(Color.parseColor("#ff5d6c"));
                layoutSoDienThoai.setHelperText("Bắt buộc!");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                return;
            }
            else if(phoneNumber.length() != 10 || !phoneNumber.matches("[0-9]+")){
                edSoDienThoai.requestFocus();
                layoutSoDienThoai.setBoxStrokeColor(Color.parseColor("#ff5d6c"));
                layoutSoDienThoai.setHelperText("Số điện thoại sai!");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                return;
            }
            else {
                docRef.update(Constants.KEY_PHONE_NUMBER, edSoDienThoai.getText().toString());
            }
            docRef.update(Constants.KEY_NAME, edHoVaTen.getText().toString());
            docRef.update(Constants.KEY_HOUSE_NUMBER, edSoNha.getText().toString());
            docRef.update(Constants.KEY_CITY, spinnerProvince.getSelectedItem().toString());
            docRef.update(Constants.KEY_DISTRICT, spinnerDistrict.getSelectedItem().toString());
            docRef.update(Constants.KEY_WARD, spinnerWard.getSelectedItem().toString());
            docRef.update(Constants.KEY_GENDER, !rbNam.isChecked()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ChangeInfomationActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(ChangeInfomationActivity.this, "Failed to Updated", Toast.LENGTH_SHORT).show();
        }
    }
    private void GetDataOnFireBase(){
        DocumentReference docRef = db.collection("users").document(preferenceManager.getString(Constants.KEY_USER_ID));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        edHoVaTen.setText(document.getString(Constants.KEY_NAME));
                        edSoDienThoai.setText(document.getString(Constants.KEY_PHONE_NUMBER));
                        edSoNha.setText(document.getString(Constants.KEY_HOUSE_NUMBER));
                        provinceUser = document.getString(Constants.KEY_CITY);
                        districtUser = document.getString(Constants.KEY_DISTRICT);
                        wardUser = document.getString(Constants.KEY_WARD);
                        setUpSpinners();
                        if (Boolean.TRUE.equals(document.getBoolean(Constants.KEY_GENDER))){
                            rbNu.setChecked(true);
                        }
                        else {
                            rbNam.setChecked(true);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}