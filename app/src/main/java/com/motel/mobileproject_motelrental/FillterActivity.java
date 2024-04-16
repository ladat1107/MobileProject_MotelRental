package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.material.slider.RangeSlider;
import com.motel.mobileproject_motelrental.databinding.ActivityFillterBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FillterActivity extends AppCompatActivity {
    private ActivityFillterBinding binding;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    int gia = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFillterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sliderGia.setValueFrom(0);
        binding.sliderGia.setValueTo(500);

        List<String> listDichVu = new ArrayList<>();
        listDichVu.add("Tất cả");
        listDichVu.add("Phòng trọ");
        listDichVu.add("Chung cư");
        listDichVu.add("Nhà nguyên căn");
        listDichVu.add("Ở ghép");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listDichVu);
        binding.cmbDichVu.setAdapter(adapter);
        binding.sliderGia.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(RangeSlider slider, float value, boolean fromUser) {
                // Lấy giá trị từ RangeSlider
                int minValue = binding.sliderGia.getValues().get(0).intValue() * 100000;
                int maxValue = binding.sliderGia.getValues().get(1).intValue() * 100000;

                // Format giá trị
                String formattedMinValue = decimalFormat.format(minValue);
                String formattedMaxValue = decimalFormat.format(maxValue);

                // Cập nhật giá trị cho EditText tương ứng
                binding.editGiaFrom.setText(formattedMinValue);
                binding.editGiaTo.setText(formattedMaxValue);
            }
        });

        binding.btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> listChip = new ArrayList<>();
                if (binding.chiptulanh.isChecked()) {
                    listChip.add("Tủ lạnh");
                }
                if (binding.chipmaylanh.isChecked()) {
                    listChip.add("Máy lạnh");
                }
                if (binding.chipmaygiat.isChecked()) {
                    listChip.add("Máy giặt");
                }
                if (binding.chipwifi.isChecked()) {
                    listChip.add("Wifi");
                }
                if (binding.chipgac.isChecked()) {
                    listChip.add("Có gác");
                }
                if (binding.chipgio.isChecked()) {
                    listChip.add("Giờ giấc quy định");
                }
                if (binding.chipdexe.isChecked()) {
                    listChip.add("Chỗ để xe");
                }

                String[] arrListChip = new String[listChip.size()];
                for (int i = 0; i < listChip.size(); i++) {
                    String chip = listChip.get(i);
                    arrListChip[i] = chip;
                }

                Intent intent = new Intent(FillterActivity.this, Fillter2Activity.class);
                intent.putExtra("listChip", arrListChip);
                intent.putExtra("fillter", "fillter");
                startActivity(intent);
            }
        });
    }
}