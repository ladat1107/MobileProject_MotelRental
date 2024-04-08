package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.motel.mobileproject_motelrental.Adapter.MotelAdapter;
import com.motel.mobileproject_motelrental.Item.MotelItem;
import com.motel.mobileproject_motelrental.databinding.ActivityHomePageBinding;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    // add binding
    private ActivityHomePageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigation.setItemIconTintList(null);

        binding.timtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                startActivity(intent);
            }
        });

        binding.chungcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                startActivity(intent);
            }
        });

        binding.oghep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                startActivity(intent);
            }
        });

        binding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, FillterActivity.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManagerPhoBien = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerYeuThich = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerDanhGia = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        binding.recyclerViewPhobien.setLayoutManager(layoutManagerPhoBien);
        binding.recyclerViewYeuThich.setLayoutManager(layoutManagerYeuThich);
        binding.recyclerViewDanhGia.setLayoutManager(layoutManagerDanhGia);

        List<MotelItem> motelItemList = new ArrayList<>();
        motelItemList.add(new MotelItem(R.drawable.imgroom, "Phòng trọ 1", "Địa chỉ 1", 10));
        motelItemList.add(new MotelItem(R.drawable.imgroom, "Phòng trọ 2", "Địa chỉdddddddddddddddddddddddd 1", 10));
        motelItemList.add(new MotelItem(R.drawable.imgroom, "Phòng trọ 3", "Địa chỉ 1", 10));
        motelItemList.add(new MotelItem(R.drawable.imgroom, "Phòng trọ 4", "Địa chỉ 1", 10));

        MotelAdapter adapterPhoBien = new MotelAdapter(motelItemList);
        MotelAdapter adapterYeuThich = new MotelAdapter(motelItemList);
        MotelAdapter adapterDanhGia = new MotelAdapter(motelItemList);

        binding.recyclerViewPhobien.setAdapter(adapterPhoBien);
        binding.recyclerViewYeuThich.setAdapter(adapterYeuThich);
        binding.recyclerViewDanhGia.setAdapter(adapterDanhGia);

    }
}