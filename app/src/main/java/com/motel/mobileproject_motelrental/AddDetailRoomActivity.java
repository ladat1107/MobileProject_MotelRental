package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.motel.mobileproject_motelrental.databinding.ActivityAddDetailRoomBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class AddDetailRoomActivity extends AppCompatActivity {

    private ActivityAddDetailRoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail_room);
        binding = ActivityAddDetailRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.edMoCua.setOnClickListener(v -> showTimePickerDialog(binding.edMoCua));
        binding.edDongCua.setOnClickListener(v -> showTimePickerDialog(binding.edDongCua));
        binding.imgTruTuLanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slTuLanh = Integer.parseInt(binding.tvSLTuLanh.getText().toString());
                if(slTuLanh!=0){
                    binding.tvSLTuLanh.setText(String.valueOf(slTuLanh-1) );
                }
            }
        });
        binding.imgCongTuLanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slTuLanh = Integer.parseInt(binding.tvSLTuLanh.getText().toString());
                binding.tvSLTuLanh.setText(String.valueOf(slTuLanh+1));
            }
        });
        binding.imgTruMayGiat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slMayGiat = Integer.parseInt(binding.tvSLMayGiat.getText().toString());
                if(slMayGiat!=0){
                    binding.tvSLMayGiat.setText(String.valueOf(slMayGiat-1) );
                }
            }
        });
        binding.imgCongMayGiat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slMayGiat = Integer.parseInt(binding.tvSLMayGiat.getText().toString());
                binding.tvSLMayGiat.setText(String.valueOf(slMayGiat+1));
            }
        });
        binding.imgTruMayLanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slMayLanh = Integer.parseInt(binding.tvSLMayLanh.getText().toString());
                if(slMayLanh!=0){
                    binding.tvSLMayLanh.setText(String.valueOf(slMayLanh-1) );
                }
            }
        });
        binding.imgCongMayLanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slMayLanh = Integer.parseInt(binding.tvSLMayLanh.getText().toString());
                binding.tvSLMayLanh.setText(String.valueOf(slMayLanh+1));
            }
        });
        binding.chkThoiGian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    binding.llThoiGian.setVisibility(View.VISIBLE);
                }
                else{
                    binding.llThoiGian.setVisibility(View.GONE);
                }
            }
        });
        binding.edGiaBaixe.addTextChangedListener(new TextWatcher() {
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
                // Loại bỏ ký tự không phải là số
                String cleanString = input.replaceAll("[^\\d]", "");
                // Định dạng lại số
                try {
                    double parsed = Double.parseDouble(cleanString);
                    // Format số theo định dạng tiền tệ của Việt Nam
                    String formatted = formatNumber(parsed);
                    // Hiển thị số đã định dạng trên EditText
                    binding.edGiaBaixe.removeTextChangedListener(this);
                    binding.edGiaBaixe.setText(formatted);
                    binding.edGiaBaixe.setSelection(formatted.length());
                    binding.edGiaBaixe.addTextChangedListener(this);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        binding.chkChoDeXe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.llGiaBaiXe.setVisibility(View.VISIBLE); // Hiển thị layout khi checkbox được tick
                } else {
                    binding.llGiaBaiXe.setVisibility(View.GONE); // Ẩn layout khi checkbox không được tick

                }
            }
        });
        binding.edGiaWifi.addTextChangedListener(new TextWatcher() {
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
                // Loại bỏ ký tự không phải là số
                String cleanString = input.replaceAll("[^\\d]", "");
                // Định dạng lại số
                try {
                    double parsed = Double.parseDouble(cleanString);
                    // Format số theo định dạng tiền tệ của Việt Nam
                    String formatted = formatNumber(parsed);
                    // Hiển thị số đã định dạng trên EditText
                    binding.edGiaWifi.removeTextChangedListener(this);
                    binding.edGiaWifi.setText(formatted);
                    binding.edGiaWifi.setSelection(formatted.length());
                    binding.edGiaWifi.addTextChangedListener(this);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        binding.chkWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.llGiaWifi.setVisibility(View.VISIBLE); // Hiển thị layout khi checkbox được tick
                } else {
                    binding.llGiaWifi.setVisibility(View.GONE); // Ẩn layout khi checkbox không được tick
                }

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
    private void showTimePickerDialog(TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Tạo một TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfDay) -> {
                    String amPm = (hourOfDay < 12) ? "AM" : "PM";
                    String time = String.format("%02d:%02d %s", (hourOfDay % 12 == 0) ? 12 : hourOfDay % 12, minuteOfDay, amPm);
                    //binding.edMoCua.setText(time);
                    textView.setText(time);
                }, hour, minute, false);

        // Hiển thị TimePickerDialog
        timePickerDialog.show();
    }
}