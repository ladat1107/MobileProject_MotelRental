package com.motel.mobileproject_motelrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.motel.mobileproject_motelrental.Adapter.ChipAdapter;
import com.motel.mobileproject_motelrental.Adapter.InfoMotelAdapter;
import com.motel.mobileproject_motelrental.Adapter.MotelAdapter;
import com.motel.mobileproject_motelrental.Interface.OnItemClickListener;
import com.motel.mobileproject_motelrental.Item.ChipItem;
import com.motel.mobileproject_motelrental.Item.InfoMotelItem;
import com.motel.mobileproject_motelrental.Item.MotelItem;
import com.motel.mobileproject_motelrental.databinding.ActivityFillter2Binding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fillter2Activity extends AppCompatActivity {
    private ActivityFillter2Binding binding;
    private String TAG = "Fillter2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFillter2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String fillter = getIntent().getStringExtra("fillter");
        String[] receivedArray = getIntent().getStringArrayExtra("listChip");

        if(receivedArray == null){
            receivedArray = new String[1];
            receivedArray[0] = fillter;
        }

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

        FillChip(receivedArray);
        FillList(fillter);
    }

    public void FillChip(String[] receivedArray){
        List<ChipItem> chipItemList = new ArrayList<>();
        if (receivedArray != null) {
            for (String chip : receivedArray) {
                chipItemList.add(new ChipItem(chip));
            }
        } else {
            chipItemList.add(new ChipItem("Tất cả"));
        }

        ChipAdapter adapter = new ChipAdapter(chipItemList);
        adapter.setOnChipItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                binding.flowchip.removeViewAt(position);
                chipItemList.remove(position);
            }
        });
        adapter.attachToFlowLayout(binding.flowchip);
    }

    public void FillList(String fillter){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewKetQua.setLayoutManager(layoutManager);
        List<InfoMotelItem> motelList = new ArrayList<>();
        InfoMotelAdapter adapterInfo = new InfoMotelAdapter(motelList);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query;

        if(fillter.equals("Phòng trọ")){
            query = db.collection("motels").whereEqualTo("typeID", 1);
        } else if(fillter.equals("Chung cư")){
            query = db.collection("motels").whereEqualTo("typeID", 2);
        }else if(fillter.equals("Ở ghép")){
            query = db.collection("motels").whereEqualTo("typeID", 4);
        }else if(fillter.equals("Được yêu thích")){
            query = db.collection("motels").orderBy("like", Query.Direction.DESCENDING);
        }else if(fillter.equals("Bình luận nhiều")){
            FillListBinhLuan(adapterInfo, motelList);
            return;
        }else {
            query = db.collection("motels");
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String motelAddress = document.getString("motel number") + ", " + document.getString("ward") + ", " + document.getString("district") + ", " + document.getString("city");
                        int like = document.getLong("like").intValue();
                        long price = document.getLong("price");
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

    public void FillListBinhLuan(InfoMotelAdapter adapterBinhLuan, List<InfoMotelItem> motelList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewKetQua.setLayoutManager(layoutManager);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<String> sortedMotelIDs = new ArrayList<>();

        db.collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Integer> commentCounts = new HashMap<>();

                            // Đếm số lượng comment cho mỗi motelID
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String motelID = document.getString("motelID");
                                if (commentCounts.containsKey(motelID)) {
                                    commentCounts.put(motelID, commentCounts.get(motelID) + 1);
                                } else {
                                    commentCounts.put(motelID, 1);
                                }
                            }

                            // Sắp xếp các motel dựa trên số lượng comment
                            List<Map.Entry<String, Integer>> sortedComments = new ArrayList<>(commentCounts.entrySet());
                            Collections.sort(sortedComments, new Comparator<Map.Entry<String, Integer>>() {
                                @Override
                                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                    return o2.getValue().compareTo(o1.getValue());
                                }
                            });

                            for (Map.Entry<String, Integer> entry : sortedComments) {
                                String motelID = entry.getKey();
                                sortedMotelIDs.add(motelID);
                            }

                            // Lấy thông tin chi tiết của các motel từ Firestore
                            for (String motelID : sortedMotelIDs) {
                                db.collection("motels")
                                        .document(motelID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        String id = document.getId();
                                                        String motelAddress = document.getString("motel number") + ", " + document.getString("ward") + ", " + document.getString("district") + ", " + document.getString("city");
                                                        int like = document.getLong("like").intValue();
                                                        long price = document.getLong("price");
                                                        String title = document.getString("title");
                                                        InfoMotelItem motel = new InfoMotelItem(id, R.drawable.imgroom, title, like, price, motelAddress, 0 );

                                                        // Thêm đối tượng Motel vào danh sách
                                                        motelList.add(motel);

                                                        // Kiểm tra nếu đã lấy thông tin của tất cả các motel thì cập nhật giao diện
                                                        if (motelList.size() == sortedMotelIDs.size()) {
                                                            adapterBinhLuan.notifyDataSetChanged();
                                                        }
                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });
                            }

                            // Nếu không có motel nào trong danh sách motelIDs, cập nhật giao diện ngay lập tức
                            if (sortedMotelIDs.isEmpty()) {
                                adapterBinhLuan.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // Gán adapter vào RecyclerView
        binding.recyclerViewKetQua.setAdapter(adapterBinhLuan);

        adapterBinhLuan.setOnItemRecycleClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String motelId = motelList.get(position).getId();
                Intent intent = new Intent(Fillter2Activity.this, DetailRomeActivity.class);
                intent.putExtra("motelId", motelId);
                startActivity(intent);
            }
        });
    }
}