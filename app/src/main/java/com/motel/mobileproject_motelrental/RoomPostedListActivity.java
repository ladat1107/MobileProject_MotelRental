package com.motel.mobileproject_motelrental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.motel.mobileproject_motelrental.Adapter.InfoMotelAdapter;
import com.motel.mobileproject_motelrental.Interface.OnItemClickListener;
import com.motel.mobileproject_motelrental.Item.InfoMotelItem;
import com.motel.mobileproject_motelrental.databinding.ActivityFillter2Binding;
import com.motel.mobileproject_motelrental.databinding.ActivityRoomPostedListBinding;

import java.util.ArrayList;
import java.util.List;

public class RoomPostedListActivity extends AppCompatActivity {

    private ActivityRoomPostedListBinding binding;
    private String TAG = "ActivityRoomPostedListBinding";
    int putType = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_posted_list);
        binding = ActivityRoomPostedListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.sendback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomPostedListActivity.this, AccountPageActivity.class);
                startActivity(intent);
            }
        });
    }

    public void FillList(String fillter, String[] receivedArray) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewKetQua.setLayoutManager(layoutManager);
        List<InfoMotelItem> motelList = new ArrayList<>();
        InfoMotelAdapter adapterInfo = new InfoMotelAdapter(motelList);
        Query query = db.collection("motels").whereEqualTo(Constants.KEY_USER_ID, "");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int sl = 0;
                    /*for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String motelAddress = document.getString("motel number") + ", " + document.getString("ward") + ", " + document.getString("district") + ", " + document.getString("city");
                        int like = document.getLong("like").intValue();
                        long price = document.getLong("price");
                        String title = document.getString("title");
                        InfoMotelItem motel = new InfoMotelItem(id, R.drawable.imgroom, title, like, price, motelAddress, 0);
                        motelList.add(motel);

                        sl++;
                    }*/
                    binding.recyclerViewKetQua.setAdapter(adapterInfo);
                    adapterInfo.notifyDataSetChanged();
                    binding.txtSoKQ.setText("Tìm thấy " + sl + " kết quả");
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        adapterInfo.setOnItemRecycleClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
               /* String motelId = motelList.get(position).getId();
                // Xử lý sự kiện khi một item được click
                Intent intent = new Intent(RoomPostedListActivity.this, DetailRomeActivity.class);
                intent.putExtra("motelId", motelId);
                startActivity(intent);*/
            }
        });


    }


}