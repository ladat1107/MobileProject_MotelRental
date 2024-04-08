package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.motel.mobileproject_motelrental.databinding.ActivityLocationBinding;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private ActivityLocationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<String> dataList = new ArrayList<>();
        dataList.add("Item 1");
        dataList.add("Item 2");
        dataList.add("Item 3");
        dataList.add("Item 4");
        dataList.add("Item 5");
        dataList.add("Item 6");
        dataList.add("Item 7");
        dataList.add("Item 8");
        dataList.add("Item 8");
        dataList.add("Item 9");
        dataList.add("Item 10");
        dataList.add("Item 310");
// Thêm các mục dữ liệu khác nếu cần
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dataList);
        binding.cmbTinh.setAdapter(adapter);




    }
}