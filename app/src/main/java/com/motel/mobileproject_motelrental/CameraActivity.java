package com.motel.mobileproject_motelrental;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import com.motel.mobileproject_motelrental.Adapter.GalleryAdapter;
import com.motel.mobileproject_motelrental.databinding.ActivityCameraBinding;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
   // private List<Uri> selectedMedia = new ArrayList<>();
  //  private StorageReference storageReference;
    private ActivityCameraBinding binding;
    String encodedImage;
    private List<Uri> selectedMedia = new ArrayList<>();
    private GalleryAdapter galleryAdapter;

    // Phương thức để mở Gallery và chọn ảnh và video
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*, video/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_CODE);
    }

    // Xử lý kết quả trả về từ Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                // Nếu chọn nhiều ảnh và video
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    selectedMedia.add(clipData.getItemAt(i).getUri());
                }
            } else {
                // Nếu chỉ chọn một ảnh hoặc video
                selectedMedia.add(data.getData());
            }
            galleryAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.layoutImage.setOnClickListener(v-> {

            openGallery();
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        galleryAdapter = new GalleryAdapter(this, selectedMedia);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 items trên mỗi hàng
        recyclerView.setAdapter(galleryAdapter);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK){
            if(result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.imageProfile.setImageBitmap(bitmap);
                    binding.textAddImage.setVisibility(View.GONE);
                    encodedImage = endcodeImage(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private String endcodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }



}

