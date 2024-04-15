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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.motel.mobileproject_motelrental.Adapter.ChipAdapter;
import com.motel.mobileproject_motelrental.Adapter.InfoMotelAdapter;
import com.motel.mobileproject_motelrental.Interface.OnItemClickListener;
import com.motel.mobileproject_motelrental.Item.ChipItem;
import com.motel.mobileproject_motelrental.Item.InfoMotelItem;
import com.motel.mobileproject_motelrental.databinding.ActivityFillter2Binding;

import java.util.ArrayList;
import java.util.List;

public class Fillter2Activity extends AppCompatActivity {
    private ActivityFillter2Binding binding;
    private String TAG = "Fillter2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFillter2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String fillter = getIntent().getStringExtra("fillter");

        binding.sendback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fillter2Activity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        binding.btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fillter2Activity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });



        List<ChipItem> chipItemList = new ArrayList<>();
        chipItemList.add(new ChipItem(fillter));
        ChipAdapter adapter = new ChipAdapter(chipItemList);
        adapter.setOnChipItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Xóa mục khỏi FlowLayout
                binding.flowchip.removeViewAt(position);

                // Cập nhật lại danh sách chipItemList
                chipItemList.remove(position);
            }
        });
        adapter.attachToFlowLayout(binding.flowchip);

        FillList();
    }

    public void FillList(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewKetQua.setLayoutManager(layoutManager);

        List<InfoMotelItem> motelList = new ArrayList<>();
        InfoMotelAdapter adapterInfo = new InfoMotelAdapter(motelList);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("motels").orderBy("like", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String motelAddress = document.getString("motel number") + ", " + document.getString("ward") + ", " + document.getString("district") + ", " + document.getString("city");
                        int like = document.getLong("like").intValue();
                        int price;
                        Object priceObject = document.get("price");
                        if (priceObject instanceof Long) {
                            price = ((Long) priceObject).intValue();
                        } else if (priceObject instanceof Integer) {
                            price = (Integer) priceObject;
                        } else {
                            // Xử lý trường hợp khác nếu cần
                            price = 0; // Giá trị mặc định
                        }
                        String title = document.getString("title");
                        InfoMotelItem motel = new InfoMotelItem(id, R.drawable.imgroom, title, like, price, motelAddress, 0 );

                        // Thêm đối tượng Motel vào danh sách
                        motelList.add(motel);
                    }
                    binding.recyclerViewKetQua.setAdapter(adapterInfo);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
        adapterInfo.setOnItemRecycleClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String motelId = motelList.get(position).getId();
                // Xử lý sự kiện khi một item được click
                Intent intent = new Intent(Fillter2Activity.this, DetailRomeActivity.class);
                intent.putExtra("motelId", motelId);
                startActivity(intent);
            }
        });
    }
}