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
import com.google.firebase.firestore.DocumentSnapshot;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fillter2Activity extends AppCompatActivity {
    private ActivityFillter2Binding binding;
    private String TAG = "Fillter2Activity";
    int putType =0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFillter2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        putType = getIntent().getIntExtra("putType", 0);
        String fillter;
        String[] receivedArray;

        if(putType == 0){
            fillter = getIntent().getStringExtra("fillter");
            receivedArray = new String[1];
            receivedArray[0] = fillter;
        } else {
            receivedArray = getIntent().getStringArrayExtra("infoFill");
            fillter = receivedArray[5];
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
        FillList(fillter, receivedArray);
    }

    public void FillChip(String[] receivedArray){
        List<ChipItem> chipItemList = new ArrayList<>();
        if (receivedArray.length >= 4) {
            for (int i = 4; i < receivedArray.length; i++) {
                String chip = receivedArray[i];

                if(chip.equals("Tất cả")) {
                }
                else {
                    chipItemList.add(new ChipItem(chip));
                }
            }
        } else {
            String chip = receivedArray[0];
            chipItemList.add(new ChipItem(chip));
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

    public void FillList(String fillter, String[] receivedArray){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewKetQua.setLayoutManager(layoutManager);
        List<InfoMotelItem> motelList = new ArrayList<>();
        InfoMotelAdapter adapterInfo = new InfoMotelAdapter(motelList);

        Query query = db.collection(Constants.KEY_COLLECTION_MOTELS);

        if(putType == 0){
            if(fillter.equals("Phòng trọ")){
                query = query.whereEqualTo(Constants.KEY_TYPE_ID, 1);
            } else if(fillter.equals("Chung cư")) {
                query = query.whereEqualTo(Constants.KEY_TYPE_ID, 2);
            } else if(fillter.equals("Nhà nguyên căn")) {
                query = query.whereEqualTo(Constants.KEY_TYPE_ID, 3);
            } else if(fillter.equals("Ở ghép")) {
                query = query.whereEqualTo(Constants.KEY_TYPE_ID, 4);
            }else if(fillter.equals("Được yêu thích")){
                query = db.collection(Constants.KEY_COLLECTION_MOTELS).orderBy("like", Query.Direction.DESCENDING);
            }else if(fillter.equals("Bình luận nhiều")){
                //FillListBinhLuan(adapterInfo, motelList);
                return;
            } else {
                query = db.collection(Constants.KEY_COLLECTION_MOTELS);
            }
            handleHomePage(query, adapterInfo, motelList);
            return;
        }

        if(receivedArray[4].equals("Giá tăng dần")){
            query = query.orderBy(Constants.KEY_PRICE, Query.Direction.ASCENDING);
        } else if(receivedArray[4].equals("Giá giảm dần")){
            query = query.orderBy(Constants.KEY_PRICE, Query.Direction.DESCENDING);
        }else {
            query = db.collection(Constants.KEY_COLLECTION_MOTELS);
        }

        handelFilterPage(query, fillter, receivedArray, adapterInfo, motelList);

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

        binding.imgfilterblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fillter2Activity.this, FillterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void handleHomePage(Query query, InfoMotelAdapter adapterInfo, List<InfoMotelItem> motelList){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int sl = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getBoolean(Constants.KEY_STATUS_MOTEL) == true){
                            String id = document.getId();
                            String motelAddress = document.getString(Constants.KEY_MOTEL_NUMBER) + ", " + document.getLong(Constants.KEY_WARD_MOTEL) + ", " + document.getLong(Constants.KEY_DISTRICT_MOTEL) + ", " + document.getLong(Constants.KEY_CITY_MOTEL);
                            int like = document.getLong(Constants.KEY_COUNT_LIKE).intValue();
                            long price = document.getLong(Constants.KEY_PRICE);
                            String title = document.getString(Constants.KEY_TITLE);

                            List<String> imageUrls = (List<String>) document.get(Constants.KEY_IMAGE_LIST);
                            String imgRes = imageUrls.get(0);

                            InfoMotelItem motel = new InfoMotelItem(id, imgRes, title, like, price, motelAddress, 0 );
                            motelList.add(motel);

                            sl++;
                        }
                    }
                    binding.recyclerViewKetQua.setAdapter(adapterInfo);
                    binding.txtSoKQ.setText("Tìm thấy " + sl + " kết quả");
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

    public void handelFilterPage(Query query, String fillter, String[] receivedArray, InfoMotelAdapter adapterInfo, List<InfoMotelItem> motelList){

        int tinh = Integer.parseInt(receivedArray[0]);
        int quan = Integer.parseInt(receivedArray[1]);

        int minValue = Integer.parseInt(receivedArray[2]);
        int maxValue = Integer.parseInt(receivedArray[3]);

        long type = 0;
        if(fillter.equals("Phòng trọ")){
            type = 1;
        } else if(fillter.equals("Chung cư")) {
            type = 2;
        } else if(fillter.equals("Nhà nguyên căn")) {
            type = 3;
        } else if(fillter.equals("Ở ghép")) {
            type = 4;
        }

        List<String> listChipType = new ArrayList<>();

        for(int i = 6; i<receivedArray.length; i++){
            if(receivedArray[i].equals("Tủ lạnh")){
                listChipType.add("fridge");
            } else if(receivedArray[i].equals("Máy lạnh")){
                listChipType.add("air conditioning");
            } else if(receivedArray[i].equals("Máy giặt")){
                listChipType.add("washing machine");
            } else if(receivedArray[i].equals("Wifi")){
                listChipType.add("Wireless");
            } else if(receivedArray[i].equals("Có gác")){
                listChipType.add("garet");
            } else if(receivedArray[i].equals("Giờ giấc quy định")){
                listChipType.add("starttime");
            } else if(receivedArray[i].equals("Chỗ để xe")){
                listChipType.add("car park");
            }
        }
        long finalType = type;
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    int sl = 0;
                    for(QueryDocumentSnapshot document : task.getResult()){
                        int checkStatus = 0, checkType = 0, checkAddress = 0, checkPrice = 0, checkChip = 0;
                        if (putType != 0) {

                            Boolean status = document.getBoolean(Constants.KEY_STATUS_MOTEL);
                            if(status == false){
                                checkStatus = 1;
                            }

                            long typeID = document.getLong(Constants.KEY_TYPE_ID);
                            if (typeID != finalType) {
                                checkType = 1;
                            }

                            if(finalType == 0) checkType = 0;

                            long city = document.getLong(Constants.KEY_CITY_MOTEL);
                            long district = document.getLong(Constants.KEY_DISTRICT_MOTEL);

                            if(tinh == 0){
                                checkAddress = 0;
                            } else if(quan == 0){
                                if(city != tinh){
                                    checkAddress = 1;
                                }
                            }
                            else {
                                if(city != tinh || district != quan){
                                    checkAddress = 1;
                                }
                            }

                            long price = document.getLong(Constants.KEY_PRICE);
                            if (price < minValue || price > maxValue) {
                                checkPrice = 1;
                            } else {
                                for (String item : listChipType) {
                                    if(document.get(item) == null){
                                        checkChip = 1;
                                        break;
                                    }
                                    int value = document.getLong(item).intValue();
                                    switch (item) {
                                        case Constants.KEY_COUNT_FRIDGE:
                                        case Constants.KEY_COUNT_AIRCONDITIONER:
                                        case Constants.KEY_COUNT_WASHING_MACHINE:
                                            if (value == 0) {
                                                checkChip = 1;
                                                break;
                                            }
                                            break;
                                        case Constants.KEY_PRICE_WIFI:
                                        case Constants.KEY_PRICE_PARKING:
                                            if (value < 0) {
                                                checkChip = 1;
                                                break;
                                            }
                                            break;
                                        case Constants.KEY_GARET:
                                            if (document.getBoolean(Constants.KEY_GARET) != true) {
                                                checkChip = 1;
                                                break;
                                            }
                                            break;
                                        case Constants.KEY_START_TIME:
                                            if (document.getString(Constants.KEY_START_TIME) != null) {
                                                checkChip = 1;
                                                break;
                                            }
                                            break;
                                    }
                                    if (checkChip == 1) {
                                        break;
                                    }
                                }
                            }
                        }

                        if(checkChip == 0 && checkPrice == 0 && checkAddress == 0 && checkType == 0 && checkStatus == 0){
                            String id = document.getId();
                            String motelAddress = document.getString(Constants.KEY_MOTEL_NUMBER) + ", " + document.getLong(Constants.KEY_WARD_MOTEL) + ", " + document.getLong(Constants.KEY_DISTRICT_MOTEL) + ", " + document.getLong(Constants.KEY_CITY_MOTEL);
                            int like = document.getLong(Constants.KEY_COUNT_LIKE).intValue();
                            long price = document.getLong(Constants.KEY_PRICE);
                            String title = document.getString(Constants.KEY_TITLE);

                            List<String> imageUrls = (List<String>) document.get(Constants.KEY_IMAGE_LIST);
                            String imgRes = imageUrls.get(0);

                            InfoMotelItem motel = new InfoMotelItem(id, imgRes, title, like, price, motelAddress, 0 );
                            motelList.add(motel);

                            sl++;
                        }
                    }
                    binding.recyclerViewKetQua.setAdapter(adapterInfo);
                    adapterInfo.notifyDataSetChanged();
                    binding.txtSoKQ.setText("Tìm thấy " + sl + " kết quả");
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

  /*  public void FillListBinhLuan(InfoMotelAdapter adapterBinhLuan, List<InfoMotelItem> motelList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewKetQua.setLayoutManager(layoutManager);

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
    }*/
}
