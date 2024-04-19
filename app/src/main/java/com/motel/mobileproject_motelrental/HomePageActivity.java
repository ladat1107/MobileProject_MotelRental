package com.motel.mobileproject_motelrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.motel.mobileproject_motelrental.Adapter.MotelAdapter;
import com.motel.mobileproject_motelrental.Interface.OnItemRecycleClickListener;
import com.motel.mobileproject_motelrental.Item.MotelItem;
import com.motel.mobileproject_motelrental.databinding.ActivityHomePageBinding;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    // add binding
    private ActivityHomePageBinding binding;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext()) ;
        binding.dangtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPrefernce();
                Intent intent = new Intent(getApplicationContext(), OwnerTypeOfRoomActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
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

        binding.viewallphobien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                startActivity(intent);
            }
        });

        binding.viewalldanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                startActivity(intent);
            }
        });

        binding.viewallyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
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
        motelItemList.add(new MotelItem(R.drawable.imgroom, "Phòng trọ 2", "Địa chỉ 1", 10));
        motelItemList.add(new MotelItem(R.drawable.imgroom, "Phòng trọ 3", "Địa chỉ 1", 10));
        motelItemList.add(new MotelItem(R.drawable.imgroom, "Phòng trọ 4", "Địa chỉ 1", 10));

        MotelAdapter adapterPhoBien = new MotelAdapter(motelItemList);
        MotelAdapter adapterYeuThich = new MotelAdapter(motelItemList);
        MotelAdapter adapterDanhGia = new MotelAdapter(motelItemList);

        binding.recyclerViewPhobien.setAdapter(adapterPhoBien);
        binding.recyclerViewYeuThich.setAdapter(adapterYeuThich);
        binding.recyclerViewDanhGia.setAdapter(adapterDanhGia);

        adapterPhoBien.setOnItemRecycleClickListener(new OnItemRecycleClickListener() {
            @Override
            public void onItemClick(int position) {
                // Xử lý sự kiện khi một item được click
                Intent intent = new Intent(HomePageActivity.this, DetailRomeActivity.class);
                startActivity(intent);
            }
        });

        adapterYeuThich.setOnItemRecycleClickListener(new OnItemRecycleClickListener() {
            @Override
            public void onItemClick(int position) {
                // Xử lý sự kiện khi một item được click
                Intent intent = new Intent(HomePageActivity.this, DetailRomeActivity.class);
                startActivity(intent);
            }
        });

        adapterDanhGia.setOnItemRecycleClickListener(new OnItemRecycleClickListener() {
            @Override
            public void onItemClick(int position) {
                // Xử lý sự kiện khi một item được click
                Intent intent = new Intent(HomePageActivity.this, DetailRomeActivity.class);
                startActivity(intent);
            }
        });

        binding.btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.map){
                return true;
            } else if(id == R.id.message){
                startActivity(new Intent(getApplicationContext(), Fillter2Activity.class));
                finish();
                return true;
            } else if(id == R.id.love){
                startActivity(new Intent(getApplicationContext(), Fillter2Activity.class));
                finish();
                return true;
            } else if(id == R.id.user){
                startActivity(new Intent(getApplicationContext(), Fillter2Activity.class));
                finish();
                return true;
            }
            return false;
        });
    }
    private void clearPrefernce() {
        preferenceManager.clearSpecificPreferences(
                Constants.KEY_TITLE,
                Constants.KEY_COUNT_LIKE,
                Constants.KEY_COUNT_AIRCONDITIONER,
                Constants.KEY_LATITUDE,
                Constants.KEY_LONGTITUDE,
                Constants.KEY_MOTEL_NUMBER,
                Constants.KEY_WARD_MOTEL,
                Constants.KEY_DISTRICT_MOTEL,
                Constants.KEY_CITY_MOTEL,
                Constants.KEY_PRICE,
                Constants.KEY_ELECTRICITY_PRICE,
                Constants.KEY_WATER_PRICE,
                Constants.KEY_EMPTY_DAY,
                Constants.KEY_ACREAGE,
                Constants.KEY_CHARACTERISTIC,
                Constants.KEY_DESCRIPTION,
                Constants.KEY_COUNT_FRIDGE,
                Constants.KEY_COUNT_WASHING_MACHINE,
                Constants.KEY_GARET,
                Constants.KEY_NO_HOST,
                Constants.KEY_PRICE_WIFI,
                Constants.KEY_PRICE_PARKING,
                Constants.KEY_START_TIME,
                Constants.KEY_END_TIME,
                Constants.KEY_STATUS_MOTEL,
                Constants.KEY_IMAGE_LIST,
                Constants.KEY_TYPE_ID
        );
    }
}