package com.motel.mobileproject_motelrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.motel.mobileproject_motelrental.Adapter.MotelAdapter;
import com.motel.mobileproject_motelrental.Interface.OnItemClickListener;
import com.motel.mobileproject_motelrental.Item.MotelItem;
import com.motel.mobileproject_motelrental.databinding.ActivityHomePageBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {

    // add binding
    private ActivityHomePageBinding binding;
    private String TAG = "HomePageActivity";
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext()) ;
        //displayavatar();
        binding.imageProfile.setImageBitmap(getBitmapFromEncodedString(preferenceManager.getString(Constants.KEY_IMAGE)));
        getToken();
        setContentView(binding.getRoot());
        binding.tvUserName.setText(preferenceManager.getString(Constants.KEY_NAME ));
        binding.dangtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPrefernce();
                preferenceManager.putInt(Constants.KEY_COUNT_LIKE,0);
                Intent intent = new Intent(getApplicationContext(), OwnerTypeOfRoomActivity.class);
                startActivity(intent);
            }
        });
        binding.timtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fillter = "Phòng trọ";
                int putType = 0;
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                intent.putExtra("putType", putType);
                intent.putExtra("fillter", fillter);
                startActivity(intent);
            }
        });

        binding.chungcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fillter = "Chung cư";
                int putType = 0;
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                intent.putExtra("fillter", fillter);
                intent.putExtra("putType", putType);
                startActivity(intent);
            }
        });

        binding.oghep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fillter = "Ở ghép";
                int putType = 0;
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                intent.putExtra("fillter", fillter);
                intent.putExtra("putType", putType);
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
                String fillter = "Gần đây";
                int putType = 0;
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                intent.putExtra("fillter", fillter);
                intent.putExtra("putType", putType);
                startActivity(intent);
            }
        });

        binding.viewalldanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fillter = "Bình luận nhiều";
                int putType = 0;
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                intent.putExtra("fillter", fillter);
                intent.putExtra("putType", putType);
                startActivity(intent);
            }
        });

        binding.viewallyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fillter = "Được yêu thích";
                int putType = 0;
                Intent intent = new Intent(HomePageActivity.this, Fillter2Activity.class);
                intent.putExtra("putType", putType);
                intent.putExtra("fillter", fillter);
                startActivity(intent);
            }
        });

        FillListBinhLuan();
        FillListPhoBien();
        FillListYeuThich();
        MenuClick();
    }

    public void MenuClick(){
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            } else if(id == R.id.love){
                /*startActivity(new Intent(getApplicationContext(), Fillter2Activity.class));
                finish();*/
                return true;
            } else if(id == R.id.user){
                startActivity(new Intent(getApplicationContext(), AccountPageActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    public void FillListYeuThich(){
        LinearLayoutManager layoutManagerYeuThich = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewYeuThich.setLayoutManager(layoutManagerYeuThich);
        List<MotelItem> motelList = new ArrayList<>();
        MotelAdapter adapterYeuThich = new MotelAdapter(motelList);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_MOTELS)
                .orderBy(Constants.KEY_COUNT_LIKE, Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                       if(document.getBoolean(Constants.KEY_STATUS_MOTEL) == true){
                           String id = document.getId();
                           String motelAddress = document.getString(Constants.KEY_MOTEL_NUMBER) + ", "
                                   + document.getString(Constants.KEY_WARD_NAME) + ", "
                                   + document.getString(Constants.KEY_DISTRICT_NAME) + ", "
                                   + document.getString(Constants.KEY_CITY_NAME);
                           int like = document.getLong(Constants.KEY_COUNT_LIKE).intValue();
                           String title = document.getString(Constants.KEY_TITLE);
                           List<String> imageUrls = (List<String>) document.get(Constants.KEY_IMAGE_LIST);
                           String imgRes = imageUrls.get(1);

                           MotelItem motel = new MotelItem(id, imgRes, title, motelAddress, like);

                           // Thêm đối tượng Motel vào danh sách
                           motelList.add(motel);
                       }
                    }
                    // Cập nhật giao diện
                    binding.recyclerViewYeuThich.setAdapter(adapterYeuThich);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

        adapterYeuThich.setOnItemRecycleClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String motelId = motelList.get(position).getId();
                Intent intent = new Intent(HomePageActivity.this, DetailRomeActivity.class);
                intent.putExtra("motelId", motelId);
                startActivity(intent);
            }
        });
    }

    public void FillListBinhLuan(){
        LinearLayoutManager layoutManagerBinhLuan = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewDanhGia.setLayoutManager(layoutManagerBinhLuan);

        List<MotelItem> motelList = new ArrayList<>();
        MotelAdapter adapterBinhLuan = new MotelAdapter(motelList);

        List<String> sortedMotelIDs = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Constants.KEY_COLLECTION_COMMENTS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Integer> commentCounts = new HashMap<>();

                            // Đếm số lượng comment cho mỗi motelID
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getString(Constants.KEY_COMMENT_MOTEL);
                                if (commentCounts.containsKey(id)) {
                                    commentCounts.put(id, commentCounts.get(id) + 1);
                                } else {
                                    commentCounts.put(id, 1);
                                }
                            }

                            // Sắp xếp các motel dựa trên số lượng comment
                            List<Map.Entry<String, Integer>> sortedComments = new ArrayList<>(commentCounts.entrySet());
                            Collections.sort(sortedComments, new Comparator<Map.Entry<String, Integer>>() {
                                @Override
                                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                    return o2.getValue().compareTo(o1.getValue()); // Sắp xếp từ lớn đến nhỏ
                                }
                            });

                            // Tạo danh sách chứa chỉ các motelID đã sắp xếp

                            for (Map.Entry<String, Integer> entry : sortedComments) {
                                String id = entry.getKey();
                                sortedMotelIDs.add(id);
                            }

                            // Lấy thông tin chi tiết của các motel từ Firestore
                            for (String id : sortedMotelIDs) {
                                    db.collection(Constants.KEY_COLLECTION_MOTELS)
                                            .document(id)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.getBoolean(Constants.KEY_STATUS_MOTEL) == true && document.exists()) {
                                                            String id = document.getId();
                                                            String motelAddress = document.getString(Constants.KEY_MOTEL_NUMBER) + ", "
                                                                    + document.getString(Constants.KEY_WARD_NAME) + ", "
                                                                    + document.getString(Constants.KEY_DISTRICT_NAME) + ", "
                                                                    + document.getString(Constants.KEY_CITY_NAME);
                                                            int like = document.getLong(Constants.KEY_COUNT_LIKE).intValue();
                                                            String title = document.getString(Constants.KEY_TITLE);
                                                            List<String> imageUrls = (List<String>) document.get(Constants.KEY_IMAGE_LIST);
                                                            String imgRes = imageUrls.get(1);

                                                            MotelItem motel = new MotelItem(id, imgRes, title, motelAddress, like);

                                                            // Thêm đối tượng Motel vào danh sách
                                                            motelList.add(motel);

                                                            // Kiểm tra nếu đã lấy thông tin của tất cả các motel thì cập nhật giao diện
                                                            if (motelList.size() == sortedMotelIDs.size()) {
                                                                // Cập nhật giao diện
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
        binding.recyclerViewDanhGia.setAdapter(adapterBinhLuan);

        adapterBinhLuan.setOnItemRecycleClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String motelId = motelList.get(position).getId();
                Intent intent = new Intent(HomePageActivity.this, DetailRomeActivity.class);
                intent.putExtra("motelId", motelId);
                startActivity(intent);
            }
        });
    }

    public void FillListPhoBien(){
        LinearLayoutManager layoutManagerPhoBien = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewPhobien.setLayoutManager(layoutManagerPhoBien);
        List<MotelItem> motelList = new ArrayList<>();
        MotelAdapter adapterPhoBien = new MotelAdapter(motelList);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_MOTELS)
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getBoolean(Constants.KEY_STATUS_MOTEL) == true){
                            String id = document.getId();
                            String motelAddress = document.getString(Constants.KEY_MOTEL_NUMBER) + ", " + document.getString(Constants.KEY_WARD_NAME) + ", " + document.getString(Constants.KEY_DISTRICT_NAME) + ", " + document.getString(Constants.KEY_CITY_NAME);
                            int like = document.getLong(Constants.KEY_COUNT_LIKE).intValue();
                            String title = document.getString(Constants.KEY_TITLE);

                            List<String> imageUrls = (List<String>) document.get(Constants.KEY_IMAGE_LIST);
                            String imgRes = imageUrls.get(1);

                            MotelItem motel = new MotelItem(id, imgRes, title, motelAddress, like);

                            // Thêm đối tượng Motel vào danh sách
                            motelList.add(motel);
                        }

                    }
                    // Cập nhật giao diện
                    binding.recyclerViewPhobien.setAdapter(adapterPhoBien);
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
                Constants.KEY_TYPE_ID,
                Constants.KEY_CITY_NAME,
                Constants.KEY_DISTRICT_NAME,
                Constants.KEY_WARD_NAME
        );
    }
    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token).addOnFailureListener(e -> showToast("Unable update token"));
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);;
        return bitmap;
    }
}