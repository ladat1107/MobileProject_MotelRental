package com.motel.mobileproject_motelrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.motel.mobileproject_motelrental.databinding.ActivityBasicInformationBinding;
import com.motel.mobileproject_motelrental.databinding.ActivityCameraBinding;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class BasicInformationActivity extends AppCompatActivity {

    private ActivityBasicInformationBinding binding;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Calendar myCalendar = Calendar.getInstance();
    private FirebaseFirestore db;
    String motelID = "";
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String formattedDate = dateFormat.format(myCalendar.getTime());
            binding.etNgayChuyenToi.setText(formattedDate);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);
        binding = ActivityBasicInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            motelID = intent.getStringExtra("motelID");
        }

        binding.btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.etTieuDe.getText())) {
                    binding.etTieuDe.getBackground().setState(new int[]{android.R.attr.state_empty});
                    Toast.makeText(BasicInformationActivity.this, "Vui lòng điền đầy đủ thông tin tiêu đề", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etDienTich.getText())) {
                    binding.etDienTich.getBackground().setState(new int[]{android.R.attr.state_empty});
                    Toast.makeText(BasicInformationActivity.this, "Vui lòng điền diện tích", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etGiaThue.getText())) {
                    binding.etGiaThue.getBackground().setState(new int[]{android.R.attr.state_empty});
                    Toast.makeText(BasicInformationActivity.this, "Vui lòng điền thông tin giá thuê", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etGiaDien.getText())) {
                    binding.etGiaDien.getBackground().setState(new int[]{android.R.attr.state_empty});
                    Toast.makeText(BasicInformationActivity.this, "Vui lòng điền thông tin giá điện", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etGiaNuoc.getText())) {
                    binding.etGiaNuoc.getBackground().setState(new int[]{android.R.attr.state_empty});
                    Toast.makeText(BasicInformationActivity.this, "Vui lòng điền thông tin giá nước", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etNgayChuyenToi.getText())) {
                    binding.etNgayChuyenToi.getBackground().setState(new int[]{android.R.attr.state_empty});
                    Toast.makeText(BasicInformationActivity.this, "Vui lòng chọn ngày chuyển tới", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etNoiBat.getText())) {
                    binding.etNoiBat.getBackground().setState(new int[]{android.R.attr.state_empty});
                    Toast.makeText(BasicInformationActivity.this, "Vui lòng điền đầy đủ thông tin nổi bật", Toast.LENGTH_SHORT).show();
                } else {
                    updateMotel(motelID);
                }
            }
        });
        binding.etGiaNuoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.isEmpty()) return;
                String cleanString = input.replaceAll("[^\\d]", "");
                try {
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = formatNumber(parsed);
                    binding.etGiaNuoc.removeTextChangedListener(this);
                    binding.etGiaNuoc.setText(formatted);
                    binding.etGiaNuoc.setSelection(formatted.length());
                    binding.etGiaNuoc.addTextChangedListener(this);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        binding.etGiaDien.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.isEmpty()) return;
                String cleanString = input.replaceAll("[^\\d]", "");
                try {
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = formatNumber(parsed);
                    binding.etGiaDien.removeTextChangedListener(this);
                    binding.etGiaDien.setText(formatted);
                    binding.etGiaDien.setSelection(formatted.length());
                    binding.etGiaDien.addTextChangedListener(this);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        binding.etGiaThue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.isEmpty()) return;
                String cleanString = input.replaceAll("[^\\d]", "");
                try {
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = formatNumber(parsed);
                    binding.etGiaThue.removeTextChangedListener(this);
                    binding.etGiaThue.setText(formatted);
                    binding.etGiaThue.setSelection(formatted.length());
                    binding.etGiaThue.addTextChangedListener(this);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        binding.btnLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BasicInformationActivity.this, d,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateMotel(String motelID) {
        DocumentReference docRef = db.collection("images").document(motelID);
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_TITLE, binding.etMoTa.getText().toString());
        updates.put(Constants.KEY_ACREAGE, binding.etDienTich.getText().toString());
        updates.put(Constants.KEY_PRICE, String.valueOf(parseNumber(binding.etGiaThue.getText().toString())));
        updates.put(Constants.KEY_ELECTRICITY_PRICE, String.valueOf(parseNumber(binding.etGiaDien.getText().toString())));
        updates.put(Constants.KEY_WATER_PRICE, String.valueOf(parseNumber(binding.etGiaNuoc.getText().toString())));
        updates.put(Constants.KEY_EMPTY_DAY, binding.etNgayChuyenToi.getText().toString());
        updates.put(Constants.KEY_CHARACTERISTIC, binding.etNoiBat.getText().toString());
        updates.put(Constants.KEY_DESCRIPTION, binding.etMoTa.getText().toString());
        docRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BasicInformationActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BasicInformationActivity.this, LocationActivity.class);
                        intent.putExtra("motelID", motelID);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private String formatNumber(double number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,###", symbols);
        return df.format(number);
    }

    private double parseNumber(String numberString) {
        try {
            double number = Double.parseDouble(numberString);
            return number;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
}