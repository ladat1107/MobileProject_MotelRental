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

    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private List<Uri> selectedUris = new ArrayList<>();
    private StorageReference storageRef;

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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Sửa đổi ở đây: Truyền 'this' (Context của hoạt động) vào Adapter
        adapter = new GalleryAdapter(selectedUris, this, this);
        recyclerView.setAdapter(adapter);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        findViewById(R.id.tvThuVien).setOnClickListener(v -> openGalleryForMedia());
        binding.btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFiles();
            }
        });
    }

    private void openGalleryForMedia() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Cho phép tất cả các loại tệp
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        pickMedia.launch(intent);
    }

    private void uploadFiles() {
        if (selectedUris.isEmpty()) {
            Toast.makeText(this, "Không có tệp nào được chọn!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Uri uri : selectedUris) {
            StorageReference fileRef = storageRef.child("uploads/" + System.currentTimeMillis());
            fileRef.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Lấy URL của nội dung đã tải lên
                        Toast.makeText(CameraActivity.this, "Tải lên thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý việc tải lên không thành công
                        Toast.makeText(CameraActivity.this, "Tải lên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

        // Xóa danh sách selectedUris sau khi tải lên
        selectedUris.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        adapter.deleteItem(position);
    }
}
//    private void openGalleryForMedia() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        pickMedia.launch(intent);
//    }
//
//    private void uploadFiles() {
//        for (Uri uri : selectedUris) {
//            StorageReference fileRef = storageRef.child("Image/ImageMotel/" + System.currentTimeMillis());
//            UploadTask uploadTask = fileRef.putFile(uri);
//
//            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//                    return fileRef.getDownloadUrl();
//                }
//            }).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                    if (downloadUri != null) {
//                        Log.d("CameraActivity", "File uploaded: " + downloadUri.toString());
//                    }
//                } else {
//                    Toast.makeText(CameraActivity.this, "Upload failed: " + task.getException(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
     /* private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private static final int REQUEST_CODE = 1;
    private GalleryAdapter adapter;
    private List<Uri> selectedUris = new ArrayList<>();*/


