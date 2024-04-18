package com.motel.mobileproject_motelrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private PreferenceManager preferenceManager;
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
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadDataBack();
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        binding.btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) BasicInformationActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                checkInfoMotel();
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

    private void checkInfoMotel() {
        if (TextUtils.isEmpty(binding.etTieuDe.getText())) {
            binding.etTieuDe.requestFocus();
            addTextError(this,binding.etTieuDe,"Vui lòng điền đầy đủ thông tin tiêu đề");
            binding.etTieuDe.getBackground().setState(new int[]{android.R.attr.state_empty});
        } else if (TextUtils.isEmpty(binding.etDienTich.getText())) {
            binding.etDienTich.requestFocus();
            addTextError(this,binding.etDienTich,"Vui lòng điền đầy đủ thông tin tiêu đề");
            binding.etDienTich.getBackground().setState(new int[]{android.R.attr.state_empty});
        } else if (TextUtils.isEmpty(binding.etGiaThue.getText())) {
            binding.etGiaThue.requestFocus();
            addTextError(this,binding.etGiaThue,"Vui lòng điền đầy đủ thông tin tiêu đề");
            binding.etGiaThue.getBackground().setState(new int[]{android.R.attr.state_empty});
        } else if (TextUtils.isEmpty(binding.etGiaDien.getText())) {
            binding.etGiaDien.requestFocus();
            addTextError(this,binding.etGiaDien,"Vui lòng điền đầy đủ thông tin tiêu đề");
            binding.etGiaDien.getBackground().setState(new int[]{android.R.attr.state_empty});
        } else if (TextUtils.isEmpty(binding.etGiaNuoc.getText())) {
            binding.etGiaNuoc.requestFocus();
            addTextError(this,binding.etGiaNuoc,"Vui lòng điền đầy đủ thông tin tiêu đề");
            binding.etGiaNuoc.getBackground().setState(new int[]{android.R.attr.state_empty});
        } else if (TextUtils.isEmpty(binding.etNgayChuyenToi.getText())) {
            binding.etNgayChuyenToi.requestFocus();
            addTextError(this,binding.etNgayChuyenToi,"Vui lòng điền đầy đủ thông tin tiêu đề");
            binding.etNgayChuyenToi.getBackground().setState(new int[]{android.R.attr.state_empty});
        } else if (TextUtils.isEmpty(binding.etNoiBat.getText())) {
            binding.etNoiBat.requestFocus();
            addTextError(this,binding.etNoiBat,"Vui lòng điền đầy đủ thông tin tiêu đề");
            binding.etNoiBat.getBackground().setState(new int[]{android.R.attr.state_empty});
        } else if (Double.valueOf(binding.etDienTich.getText().toString()) < 0) {
            binding.etDienTich.requestFocus();
            addTextError(this,binding.etDienTich,"Vui lòng điền đầy đủ thông tin tiêu đề");
            binding.etDienTich.getBackground().setState(new int[]{android.R.attr.state_empty});
        } else if (parseNumber(binding.etGiaThue.getText().toString()) < 0) {
            binding.etGiaThue.requestFocus();
            addTextError(this,binding.etGiaThue,"Vui lòng điền đầy đủ thông tin tiêu đề");
            binding.etGiaThue.getBackground().setState(new int[]{android.R.attr.state_empty});
        } else if (parseNumber(binding.etGiaDien.getText().toString()) < 0) {
            binding.etGiaDien.requestFocus();
            addTextError(this,binding.etGiaDien,"Vui lòng điền đầy đủ thông tin tiêu đề");
            binding.etGiaDien.getBackground().setState(new int[]{android.R.attr.state_empty});
        } else if (parseNumber(binding.etGiaNuoc.getText().toString()) < 0) {
            binding.etGiaNuoc.requestFocus();
            addTextError(this,binding.etGiaNuoc,"Vui lòng điền đầy đủ thông tin tiêu đề");
            binding.etGiaNuoc.getBackground().setState(new int[]{android.R.attr.state_empty});
        } else {
            updateMotel();
        }
    }

    private void loadDataBack() {
        if (preferenceManager.getString(Constants.KEY_TITLE) != null)
            binding.etTieuDe.setText(preferenceManager.getString(Constants.KEY_TITLE));
        if (preferenceManager.getFloat(Constants.KEY_ACREAGE) != -1)
            binding.etDienTich.setText(String.valueOf(preferenceManager.getFloat(Constants.KEY_ACREAGE)));
        if (preferenceManager.getInt(Constants.KEY_PRICE) != -1)
            binding.etGiaThue.setText(formatNumber(preferenceManager.getInt(Constants.KEY_PRICE)));
        if (preferenceManager.getInt(Constants.KEY_ELECTRICITY_PRICE) != -1)
            binding.etGiaDien.setText(formatNumber(preferenceManager.getInt(Constants.KEY_ELECTRICITY_PRICE)));
        if (preferenceManager.getInt(Constants.KEY_WATER_PRICE) != -1)
            binding.etGiaNuoc.setText(formatNumber(preferenceManager.getInt(Constants.KEY_WATER_PRICE)));
        if (preferenceManager.getString(Constants.KEY_EMPTY_DAY) != null)
            binding.etNgayChuyenToi.setText(preferenceManager.getString(Constants.KEY_EMPTY_DAY));
        if (preferenceManager.getString(Constants.KEY_CHARACTERISTIC) != null)
            binding.etNoiBat.setText(preferenceManager.getString(Constants.KEY_CHARACTERISTIC));
        if (preferenceManager.getString(Constants.KEY_DESCRIPTION) != null)
            binding.etMoTa.setText(preferenceManager.getString(Constants.KEY_DESCRIPTION));
    }

    private void updateMotel() {
        preferenceManager.putString(Constants.KEY_TITLE, binding.etTieuDe.getText().toString());
        preferenceManager.putFloat(Constants.KEY_ACREAGE, Float.valueOf(binding.etDienTich.getText().toString()));
        preferenceManager.putInt(Constants.KEY_PRICE, parseNumber(binding.etGiaThue.getText().toString()));
        preferenceManager.putInt(Constants.KEY_ELECTRICITY_PRICE, parseNumber(binding.etGiaDien.getText().toString()));
        preferenceManager.putInt(Constants.KEY_WATER_PRICE, parseNumber(binding.etGiaNuoc.getText().toString()));
        preferenceManager.putString(Constants.KEY_EMPTY_DAY, binding.etNgayChuyenToi.getText().toString());
        preferenceManager.putString(Constants.KEY_CHARACTERISTIC, binding.etNoiBat.getText().toString());
        preferenceManager.putString(Constants.KEY_DESCRIPTION, binding.etMoTa.getText().toString());
        Intent intent = new Intent(getApplicationContext(), AddDetailRoomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private String formatNumber(double number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,###", symbols);
        return df.format(number);
    }

    private int parseNumber(String numberString) {
        String numberWithoutDot = numberString.replace(".", "");
        try {
            int numberInt = Integer.parseInt(numberWithoutDot);
            return numberInt;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static void addTextError(Context context, EditText editText, String errorMessage) {
        // Lấy LinearLayout cha của EditText
        ViewGroup parentLayout = (ViewGroup) editText.getParent();
        if (parentLayout instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) parentLayout;
            boolean errorTextViewExists = false;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View childView = linearLayout.getChildAt(i);
                if (childView instanceof LinearLayout) {
                    LinearLayout errorLayout = (LinearLayout) childView;
                    if (errorLayout.getChildCount() > 0 && errorLayout.getChildAt(0) instanceof TextView) {
                        errorTextViewExists = true;
                        break;
                    }
                }
            }
            if (!errorTextViewExists) {
                // Tạo một TextView mới để hiển thị thông báo lỗi
                TextView errorTextView = new TextView(context);
                errorTextView.setText(errorMessage);
                errorTextView.setTextColor(Color.RED);
                // Thiết lập layout cho TextView
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.START; // Đặt vị trí ở bên trái của màn hình
                errorTextView.setLayoutParams(params);
                // Tạo một LinearLayout mới để chứa TextView
                LinearLayout errorLayout = new LinearLayout(context);
                errorLayout.setOrientation(LinearLayout.HORIZONTAL); // Thiết lập layout theo chiều ngang
                errorLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                errorLayout.addView(errorTextView);
                // Thêm LinearLayout chứa TextView vào LinearLayout cha
                linearLayout.addView(errorLayout, linearLayout.indexOfChild(editText) + 1); // Thêm ngay phía dưới EditText
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayout.removeView(errorLayout);
                    }
                });
            }
        }
    }

}