package com.motel.mobileproject_motelrental;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.motel.mobileproject_motelrental.Adapter.InfoMotelAdapter;
import com.motel.mobileproject_motelrental.Constants;
import com.motel.mobileproject_motelrental.Interface.OnItemClickListener;
import com.motel.mobileproject_motelrental.Interface.OnItemLongClickListener;
import com.motel.mobileproject_motelrental.Item.InfoMotelItem;
import com.motel.mobileproject_motelrental.databinding.ActivityRoomPostedListBinding;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomPostedListActivity extends AppCompatActivity {

    private ActivityRoomPostedListBinding binding;
    private String TAG = "ActivityRoomPostedListBinding";
    int putType = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_posted_list);
        binding = ActivityRoomPostedListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        FillList();
        binding.sendback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomPostedListActivity.this, AccountPageActivity.class);
                startActivity(intent);
            }
        });
    }
    public void FillList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewKetQua.setLayoutManager(layoutManager);
        List<InfoMotelItem> motelList = new ArrayList<>();
        InfoMotelAdapter adapterInfo = new InfoMotelAdapter(motelList);
        Query query = db.collection("images").whereEqualTo(Constants.KEY_POST_AUTHOR, "hdUDaeIQeIbErYFNakZw");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int sl = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String motelAddress = document.getString(Constants.KEY_MOTEL_NUMBER) + ", " + document.getLong(Constants.KEY_WARD) + ", " + document.getLong(Constants.KEY_DISTRICT) + ", " + document.getLong(Constants.KEY_CITY);
                        int like = document.getLong(Constants.KEY_COUNT_LIKE).intValue();
                        long price = document.getLong(Constants.KEY_PRICE);
                        String title = document.getString(Constants.KEY_TITLE);
                        InfoMotelItem motel = new InfoMotelItem(id, R.drawable.imgroom, title, like, price, motelAddress, 0);
                        motelList.add(motel);
                        sl++;
                    }
                    binding.recyclerViewKetQua.setAdapter(adapterInfo);
                    adapterInfo.notifyDataSetChanged();
                    binding.txtSoKQ.setText("Bạn có " + sl + " bài đăng");
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        adapterInfo.setOnItemRecycleClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
               String motelId = motelList.get(position).getId();
                // Xử lý sự kiện khi một item được click
                Intent intent = new Intent(RoomPostedListActivity.this, DetailRomeActivity.class);
                intent.putExtra("motelId", motelId);
                startActivity(intent);
            }
        });
        adapterInfo.setOnItemLongClickListener(new OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onItemLongClick(int position) {
                String motelId = motelList.get(position).getId();
                DocumentReference docRef = db.collection("images").document(motelId);
                PopupMenu popupMenu = new PopupMenu(RoomPostedListActivity.this, binding.recyclerViewKetQua.getChildAt(position),Gravity.RIGHT);
                popupMenu.inflate(R.menu.ld_menu_item);

                try {
                    popupMenu.getClass().getDeclaredMethod("setForceShowIcon", boolean.class).invoke(popupMenu, true);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_update) {
                            Intent intent = new Intent(RoomPostedListActivity.this, OwnerTypeOfRoomActivity.class);
                            intent.putExtra("motelId", motelId);
                            startActivity(intent);
                            return true;
                        } else if (id == R.id.action_view) {
                            Intent intent = new Intent(RoomPostedListActivity.this, DetailRomeActivity.class);
                            intent.putExtra("motelId", motelId);
                            startActivity(intent);
                            return true;
                        } else if (id == R.id.action_hide) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put(Constants.KEY_STATUS_MOTEL, false);
                            docRef.update(updates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Document updated successfully");
                                            } else {
                                                Log.w(TAG, "Error updating document", task.getException());
                                            }
                                        }
                                    });
                            return true;
                        } else if (id == R.id.action_display) {
                            // Tạo một đối tượng Map chứa trường và giá trị mà bạn muốn cập nhật
                            Map<String, Object> updates = new HashMap<>();
                            updates.put(Constants.KEY_STATUS_MOTEL, true);
                            docRef.update(updates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Document updated successfully");
                                            } else {
                                                Log.w(TAG, "Error updating document", task.getException());
                                            }
                                        }
                                    });
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


    }


}