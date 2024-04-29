package com.motel.mobileproject_motelrental;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.motel.mobileproject_motelrental.Adapter.InfoMotelAdapter;
import com.motel.mobileproject_motelrental.Interface.OnItemClickListener;
import com.motel.mobileproject_motelrental.Item.InfoMotelItem;

import java.util.ArrayList;
import java.util.List;


public class ListFavoriteActivity extends AppCompatActivity {
    Button btnBack;
    BottomNavigationView bottomNavigation;
    FloatingActionButton btnhome;
    RecyclerView rvFavorite;
    PreferenceManager preferenceManager;
    List<String> listLikeMotel = new ArrayList<>();
    TextView tvDanhSachTrong;
    NestedScrollView nestedScrollView;
    List<InfoMotelItem> motelList = new ArrayList<>();
    InfoMotelAdapter adapterInfo = new InfoMotelAdapter(motelList);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_page);
        tvDanhSachTrong = findViewById(R.id.tvDanhSachTrong);
        nestedScrollView = findViewById(R.id.nsvScrollView);
        rvFavorite = findViewById(R.id.recyclerViewFavorite);
        preferenceManager = new PreferenceManager(getApplicationContext());
        GetBack();
        MenuClick();
        ListYeuThich();
        adapterInfo.setOnItemRecycleClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String motelId = motelList.get(position).getId();
                // Xử lý sự kiện khi một item được click
                Intent intent = new Intent(ListFavoriteActivity.this, DetailRomeActivity.class);
                intent.putExtra("motelId", motelId);
                startActivity(intent);
            }
        });
    }
    private void GetBack(){
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void MenuClick(){
        btnhome = findViewById(R.id.btnhome);
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListFavoriteActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.map){
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                finish();
                return true;
            } else if(id == R.id.message){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
            } else if(id == R.id.love){
                startActivity(new Intent(getApplicationContext(), ListFavoriteActivity.class));
                finish();
                return true;
            } else if(id == R.id.user){
                startActivity(new Intent(getApplicationContext(), AccountPageActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
    public void ListYeuThich(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_LIKES).whereEqualTo(Constants.KEY_MOTEL_LIKER, preferenceManager.getString(Constants.KEY_USER_ID))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listLikeMotel.add(document.getString(Constants.KEY_LIKED_MOTEL));
                                if(listLikeMotel.isEmpty()){
                                    tvDanhSachTrong.setVisibility(View.VISIBLE);
                                    nestedScrollView.setVisibility(View.GONE);
                                }
                                else {
                                    tvDanhSachTrong.setVisibility(View.GONE);
                                    nestedScrollView.setVisibility(View.VISIBLE);
                                }
                                FillList(document.getString(Constants.KEY_LIKED_MOTEL));
                            }
                        }
                    }
                });
    }
    public void FillList(String motelId){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvFavorite.setLayoutManager(layoutManager);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_MOTELS).document(motelId).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.getBoolean(Constants.KEY_STATUS_MOTEL) == true) {
                                String id = document.getId();
                                String motelAddress = document.getString(Constants.KEY_MOTEL_NUMBER) + ", " + document.getString(Constants.KEY_WARD_NAME) + ", " + document.getString(Constants.KEY_DISTRICT_NAME) + ", " + document.getString(Constants.KEY_CITY_NAME);
                                int like = document.getLong(Constants.KEY_COUNT_LIKE).intValue();
                                long price = document.getLong(Constants.KEY_PRICE);
                                String title = document.getString(Constants.KEY_TITLE);
                                List<String> imageUrls = (List<String>) document.get(Constants.KEY_IMAGE_LIST);
                                String imgRes = imageUrls.get(0);
                                InfoMotelItem motel = new InfoMotelItem(id, imgRes, title, like, price, motelAddress, 0, true);
                                motelList.add(motel);
                            }
                        }
                        rvFavorite.setAdapter(adapterInfo);
                        adapterInfo.notifyDataSetChanged();
                    }
                });
    }
}