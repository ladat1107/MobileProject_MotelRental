package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.motel.mobileproject_motelrental.Custom.ConfirmationDialogListener;
import com.motel.mobileproject_motelrental.Custom.CustomDialog;
import com.motel.mobileproject_motelrental.Custom.CustomToast;
import com.motel.mobileproject_motelrental.databinding.ActivityAddDetailRoomBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class AddDetailRoomActivity extends AppCompatActivity {

    private ActivityAddDetailRoomBinding binding;
    private PreferenceManager preferenceManager;
    private int priceWifi = -1, pricePaking = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail_room);
        binding = ActivityAddDetailRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadDataBack();
        binding.btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfor();
            }
        });
        binding.tvMoCua.setOnClickListener(v -> showTimePickerDialog(binding.tvMoCua));
        binding.tvDongCua.setOnClickListener(v -> showTimePickerDialog(binding.tvDongCua));
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BasicInformationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        binding.imgTruTuLanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slTuLanh = Integer.parseInt(binding.tvSLTuLanh.getText().toString());
                if (slTuLanh != 0) {
                    binding.tvSLTuLanh.setText(String.valueOf(slTuLanh - 1));
                }
            }
        });
        binding.imgCongTuLanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slTuLanh = Integer.parseInt(binding.tvSLTuLanh.getText().toString());
                binding.tvSLTuLanh.setText(String.valueOf(slTuLanh + 1));
            }
        });
        binding.imgTruMayGiat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slMayGiat = Integer.parseInt(binding.tvSLMayGiat.getText().toString());
                if (slMayGiat != 0) {
                    binding.tvSLMayGiat.setText(String.valueOf(slMayGiat - 1));
                }
            }
        });
        binding.imgCongMayGiat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slMayGiat = Integer.parseInt(binding.tvSLMayGiat.getText().toString());
                binding.tvSLMayGiat.setText(String.valueOf(slMayGiat + 1));
            }
        });
        binding.imgTruMayLanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slMayLanh = Integer.parseInt(binding.tvSLMayLanh.getText().toString());
                if (slMayLanh != 0) {
                    binding.tvSLMayLanh.setText(String.valueOf(slMayLanh - 1));
                }
            }
        });
        binding.imgCongMayLanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slMayLanh = Integer.parseInt(binding.tvSLMayLanh.getText().toString());
                binding.tvSLMayLanh.setText(String.valueOf(slMayLanh + 1));
            }
        });
        binding.chkThoiGian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    binding.llThoiGian.setVisibility(View.VISIBLE);
                } else {
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
                String cleanString = input.replaceAll("[^\\d]", "");
                try {
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = formatNumber(parsed);
                    binding.edGiaBaixe.removeTextChangedListener(this);
                    binding.edGiaBaixe.setText(formatted);
                    binding.edGiaBaixe.setSelection(formatted.length());
                    binding.edGiaBaixe.addTextChangedListener(this);
                    pricePaking = parseNumber(formatted);
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
                    pricePaking = -1;
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
                String cleanString = input.replaceAll("[^\\d]", "");
                try {
                    double parsed = Double.parseDouble(cleanString);
                    // Format số theo định dạng tiền tệ của Việt Nam
                    String formatted = formatNumber(parsed);
                    // Hiển thị số đã định dạng trên EditText
                    binding.edGiaWifi.removeTextChangedListener(this);
                    binding.edGiaWifi.setText(formatted);
                    binding.edGiaWifi.setSelection(formatted.length());
                    binding.edGiaWifi.addTextChangedListener(this);
                    priceWifi = parseNumber(formatted);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        });
        binding.chkWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.llGiaWifi.setVisibility(View.VISIBLE);
                } else {
                    priceWifi = -1;
                    binding.llGiaWifi.setVisibility(View.GONE);
                }

            }
        });
    }

    private void loadDataBack() {
        if (preferenceManager.getInt(Constants.KEY_COUNT_AIRCONDITIONER) != -1) {
            binding.tvSLMayLanh.setText(String.valueOf(preferenceManager.getInt(Constants.KEY_COUNT_AIRCONDITIONER)));
        }
        if (preferenceManager.getInt(Constants.KEY_COUNT_WASHING_MACHINE) != -1) {
            binding.tvSLMayGiat.setText(String.valueOf(preferenceManager.getInt(Constants.KEY_COUNT_WASHING_MACHINE)));
        }
        if (preferenceManager.getInt(Constants.KEY_COUNT_FRIDGE) != -1) {
            binding.tvSLTuLanh.setText(String.valueOf(preferenceManager.getInt(Constants.KEY_COUNT_FRIDGE)));
        }
        binding.chkGac.setChecked(preferenceManager.getBoolean(Constants.KEY_GARET));
        binding.chkKhongChungChu.setChecked(preferenceManager.getBoolean(Constants.KEY_NO_HOST));
        if (preferenceManager.getInt(Constants.KEY_PRICE_WIFI) != -1) {
            binding.chkWifi.setChecked(true);
            binding.edGiaWifi.setText(formatNumber(preferenceManager.getInt(Constants.KEY_PRICE_WIFI)));
            binding.llGiaWifi.setVisibility(binding.chkWifi.isChecked() ? View.VISIBLE : View.GONE);
        }
        if (preferenceManager.getInt(Constants.KEY_PRICE_PARKING) != -1) {
            binding.chkChoDeXe.setChecked(true);
            binding.edGiaBaixe.setText(formatNumber(preferenceManager.getInt(Constants.KEY_PRICE_PARKING)));
            binding.llGiaBaiXe.setVisibility(binding.chkChoDeXe.isChecked() ? View.VISIBLE : View.GONE);

        }
        if (preferenceManager.getString(Constants.KEY_START_TIME) != null) {
            binding.chkThoiGian.setChecked(true);
            binding.tvMoCua.setText(preferenceManager.getString(Constants.KEY_START_TIME));
            binding.tvDongCua.setText(preferenceManager.getString(Constants.KEY_END_TIME));
            binding.llThoiGian.setVisibility(binding.chkThoiGian.isChecked() ? View.VISIBLE : View.GONE);
        }
    }

    private void checkInfor() {
        if (binding.chkThoiGian.isChecked() && (TextUtils.isEmpty(binding.tvMoCua.getText()) || TextUtils.isEmpty(binding.tvDongCua.getText()))) {
            CustomToast.makeText(AddDetailRoomActivity.this, "Chọn thời gian ra vào", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
        } else if (binding.chkWifi.isChecked() && TextUtils.isEmpty(binding.edGiaWifi.getText())) {
            CustomToast.makeText(AddDetailRoomActivity.this, "Thêm giá wifi mỗi tháng", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
        } else if (binding.chkChoDeXe.isChecked() && TextUtils.isEmpty(binding.edGiaBaixe.getText())) {
            CustomToast.makeText(AddDetailRoomActivity.this, "Thêm giá chỗ đậu xe", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
        } else {
            showConfirmationDialog();
        }
    }

    private String formatNumber(double number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,###", symbols);
        return df.format(number);
    }

    private int parseNumber(String numberString) {
        if (numberString.isEmpty())
            return 0;
        String numberWithoutDot = numberString.replace(".", "");
        try {
            int numberInt = Integer.parseInt(numberWithoutDot);
            return numberInt;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void showTimePickerDialog(TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // Tạo một TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfDay) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minuteOfDay);
                    textView.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }
    private void showConfirmationDialog() {
        CustomDialog.showConfirmationDialog(this, R.drawable.ld_notification, "XÁC NHẬN", "Xác nhận các tiện ích của phòng là đúng", false, new ConfirmationDialogListener() {

            @Override
            public void onOKClicked() {
                preferenceManager.putInt(Constants.KEY_COUNT_AIRCONDITIONER, Integer.parseInt(binding.tvSLMayLanh.getText().toString()));
                preferenceManager.putInt(Constants.KEY_COUNT_WASHING_MACHINE, Integer.parseInt(binding.tvSLMayGiat.getText().toString()));
                preferenceManager.putInt(Constants.KEY_COUNT_FRIDGE, Integer.parseInt(binding.tvSLTuLanh.getText().toString()));
                preferenceManager.putBoolean(Constants.KEY_GARET, binding.chkGac.isChecked());
                preferenceManager.putBoolean(Constants.KEY_NO_HOST, binding.chkKhongChungChu.isChecked());
                preferenceManager.putInt(Constants.KEY_PRICE_WIFI, priceWifi);
                preferenceManager.putInt(Constants.KEY_PRICE_PARKING, pricePaking);
                if (binding.chkThoiGian.isChecked()) {
                    preferenceManager.putString(Constants.KEY_START_TIME, binding.tvMoCua.getText().toString());
                    preferenceManager.putString(Constants.KEY_END_TIME, binding.tvDongCua.getText().toString());
                } else {
                    preferenceManager.putString(Constants.KEY_START_TIME, null);
                    preferenceManager.putString(Constants.KEY_END_TIME, null);
                }
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onCancelClicked() {
            }
        });
    }
}
