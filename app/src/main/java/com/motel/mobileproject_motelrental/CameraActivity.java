package com.motel.mobileproject_motelrental;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.motel.mobileproject_motelrental.Adapter.GalleryAdapter;
import com.motel.mobileproject_motelrental.Custom.CustomToast;
import com.motel.mobileproject_motelrental.databinding.ActivityCameraBinding;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraActivity extends AppCompatActivity implements GalleryAdapter.OnItemClickListener {
    private ActivityCameraBinding binding;
    private static final String TAG = "CameraActivity";
    private PreferenceManager preferenceManager;
    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private List<Uri> selectedUris = new ArrayList<>();
    private StorageReference storageRef;
    private FirebaseFirestore db;
    List<String> listNameImage = new ArrayList<>();
    private final ActivityResultLauncher<Intent> pickMedia = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            ClipData clipData = result.getData().getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    selectedUris.add(clipData.getItemAt(i).getUri());
                }
            } else {
                selectedUris.add(result.getData().getData());
            }
            adapter.notifyDataSetChanged(); // Update RecyclerView
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Truyền 'this' (Context của hoạt động) vào Adapter
        adapter = new GalleryAdapter(selectedUris, this, this);
        adapter.setOnItemClickListener(this); // Đặt sự kiện click vào Adapter
        recyclerView.setAdapter(adapter);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BasicInformationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        findViewById(R.id.tvThuVien).setOnClickListener(v -> openGalleryForMedia());
        findViewById(R.id.btnTiepTuc).setOnClickListener(v -> uploadFiles());
    }

    private void openGalleryForMedia() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Cho phép tất cả các loại tệp
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        pickMedia.launch(intent);
    }

    private void uploadFiles() {
        listNameImage.clear();
        if (selectedUris.isEmpty()) {
            Toast.makeText(this, "Không có tệp nào được chọn!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Uri uri : selectedUris) {
            StorageReference fileRef = storageRef.child("uploads/" + System.currentTimeMillis());
            String fileName = fileRef.getName();
            listNameImage.add(fileName);
            fileRef.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {

                    })
                    .addOnFailureListener(e -> {
                        // Xử lý việc tải lên không thành công
                        Toast.makeText(CameraActivity.this, "Tải lên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    });
        }
        CustomToast.makeText(CameraActivity.this, "Tải lên thành công", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();
        saveStringListToFirestore();
        selectedUris.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        adapter.deleteItem(position);
    }

    private void saveStringListToFirestore() {
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_POST_AUTHOR, "ktXlIZkvn91cRBkiZcK1");
        data.put(Constants.KEY_TITLE, preferenceManager.getString(Constants.KEY_TITLE));
        data.put(Constants.KEY_COUNT_LIKE, preferenceManager.getInt(Constants.KEY_COUNT_LIKE));
        data.put(Constants.KEY_COUNT_AIRCONDITIONER, preferenceManager.getInt(Constants.KEY_COUNT_AIRCONDITIONER));
        data.put(Constants.KEY_LATITUDE, preferenceManager.getFloat(Constants.KEY_LATITUDE));
        data.put(Constants.KEY_LONGTITUDE, preferenceManager.getFloat(Constants.KEY_LONGTITUDE));
        data.put(Constants.KEY_MOTEL_NUMBER, preferenceManager.getString(Constants.KEY_MOTEL_NUMBER));
        data.put(Constants.KEY_WARD_MOTEL, preferenceManager.getInt(Constants.KEY_WARD_MOTEL));
        data.put(Constants.KEY_DISTRICT_MOTEL, preferenceManager.getInt(Constants.KEY_DISTRICT_MOTEL));
        data.put(Constants.KEY_CITY_MOTEL, preferenceManager.getInt(Constants.KEY_CITY_MOTEL));
        data.put(Constants.KEY_PRICE, preferenceManager.getInt(Constants.KEY_PRICE));
        data.put(Constants.KEY_ELECTRICITY_PRICE, preferenceManager.getInt(Constants.KEY_ELECTRICITY_PRICE));
        data.put(Constants.KEY_WATER_PRICE, preferenceManager.getInt(Constants.KEY_WATER_PRICE));
        data.put(Constants.KEY_EMPTY_DAY, preferenceManager.getString(Constants.KEY_EMPTY_DAY));
        data.put(Constants.KEY_ACREAGE, preferenceManager.getFloat(Constants.KEY_ACREAGE));
        data.put(Constants.KEY_CHARACTERISTIC, preferenceManager.getString(Constants.KEY_CHARACTERISTIC));
        data.put(Constants.KEY_DESCRIPTION, preferenceManager.getString(Constants.KEY_DESCRIPTION));
        data.put(Constants.KEY_COUNT_FRIDGE, preferenceManager.getInt(Constants.KEY_COUNT_FRIDGE));
        data.put(Constants.KEY_COUNT_WASHING_MACHINE, preferenceManager.getInt(Constants.KEY_COUNT_WASHING_MACHINE));
        data.put(Constants.KEY_GARET, preferenceManager.getBoolean(Constants.KEY_GARET));
        data.put(Constants.KEY_NO_HOST, preferenceManager.getBoolean(Constants.KEY_NO_HOST));
        data.put(Constants.KEY_PRICE_WIFI, preferenceManager.getInt(Constants.KEY_PRICE_WIFI));
        data.put(Constants.KEY_PRICE_PARKING, preferenceManager.getInt(Constants.KEY_PRICE_PARKING));
        data.put(Constants.KEY_START_TIME, preferenceManager.getString(Constants.KEY_START_TIME));
        data.put(Constants.KEY_END_TIME, preferenceManager.getString(Constants.KEY_END_TIME));
        data.put(Constants.KEY_STATUS_MOTEL, preferenceManager.getInt(Constants.KEY_STATUS_MOTEL));
        data.put(Constants.KEY_STATUS_MOTEL, true);
        data.put(Constants.KEY_IMAGE_LIST, listNameImage);
        // Tạo một tài liệu mới trong collection "example"
        db.collection("images").add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            clearPrefernce();
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        } else {
                            Log.w(TAG, "Error writing document", task.getException());
                        }
                    }
                });
    }

    private void clearPrefernce() {
        preferenceManager.clearSpecificPreferences(
                Constants.KEY_POST_AUTHOR,
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
                Constants.KEY_STATUS_MOTEL,
                Constants.KEY_IMAGE_LIST
        );

    }
}


