package com.motel.mobileproject_motelrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.motel.mobileproject_motelrental.Adapter.MotelAdapter;
import com.motel.mobileproject_motelrental.Interface.OnItemClickListener;
import com.motel.mobileproject_motelrental.Item.MotelItem;
import com.motel.mobileproject_motelrental.databinding.ActivityHomePageBinding;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    // add binding
    private ActivityHomePageBinding binding;
    private String TAG = "HomePageActivity";
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
                String fillter = "Phổ biến";
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                intent.putExtra("fillter", fillter);
                startActivity(intent);
            }
        });

        binding.viewalldanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fillter = "Đánh giá cao";
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                intent.putExtra("fillter", fillter);
                startActivity(intent);
            }
        });

        binding.viewallyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fillter = "Được yêu thích";
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                intent.putExtra("fillter", fillter);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManagerPhoBien = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerYeuThich = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerDanhGia = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        binding.recyclerViewPhobien.setLayoutManager(layoutManagerPhoBien);
        binding.recyclerViewYeuThich.setLayoutManager(layoutManagerYeuThich);
        binding.recyclerViewDanhGia.setLayoutManager(layoutManagerDanhGia);

        List<MotelItem> motelList = new ArrayList<>();
        MotelAdapter adapterPhoBien = new MotelAdapter(motelList);
        MotelAdapter adapterYeuThich = new MotelAdapter(motelList);
        MotelAdapter adapterDanhGia = new MotelAdapter(motelList);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("motels").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String motelAddress = document.getString("motel number") + ", " + document.getString("ward") + ", " + document.getString("district") + ", " + document.getString("city");
                        int like = document.getLong("like").intValue();
                        String title = document.getString("title");
                        MotelItem motel = new MotelItem(id, R.drawable.imgroom, title, motelAddress, like);

                        // Thêm đối tượng Motel vào danh sách
                        motelList.add(motel);
                    }

                    // Cập nhật giao diện
                    binding.recyclerViewPhobien.setAdapter(adapterPhoBien);
                    binding.recyclerViewYeuThich.setAdapter(adapterYeuThich);
                    binding.recyclerViewDanhGia.setAdapter(adapterDanhGia);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

        adapterPhoBien.setOnItemRecycleClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String motelId = motelList.get(position).getId();
                Intent intent = new Intent(HomePageActivity.this, DetailRomeActivity.class);
                intent.putExtra("motelId", motelId);
                startActivity(intent);
            }
        });

        adapterYeuThich.setOnItemRecycleClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String motelId = motelList.get(position).getId();
                // Xử lý sự kiện khi một item được click
                Intent intent = new Intent(HomePageActivity.this, DetailRomeActivity.class);
                startActivity(intent);
            }
        });

        adapterDanhGia.setOnItemRecycleClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String motelId = motelList.get(position).getId();
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