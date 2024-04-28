package com.motel.mobileproject_motelrental;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.motel.mobileproject_motelrental.Adapter.GalleryAdapter;
import com.motel.mobileproject_motelrental.Custom.ConfirmationDialogListener;
import com.motel.mobileproject_motelrental.Custom.CustomDialog;
import com.motel.mobileproject_motelrental.Custom.CustomToast;
import com.motel.mobileproject_motelrental.databinding.ActivityCameraBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraActivity extends AppCompatActivity implements GalleryAdapter.OnItemClickListener {
    private static final int STORAGE_PERMISSION_CODE = 1003;
    private ActivityCameraBinding binding;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private static final int REQUEST_VIDEO_PERMISSION = 1002;
    private static final String TAG = "CameraActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;
    private PreferenceManager preferenceManager;
    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private List<Uri> selectedUris = new ArrayList<>();
    private StorageReference storageRef;
    private FirebaseFirestore db;
    private List<Bitmap> selectedBitmaps = new ArrayList<>();
    List<String> listNameImage = new ArrayList<>();
    List<String> imageUrls = new ArrayList<>();
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
        requestVideoPermission();
        requestCameraPermission();
        requestStoragePermission();
        db = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new GalleryAdapter(selectedUris, this, this);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        if (preferenceManager.getString("motelIDTemp") != null) {
            getImagesForMotel();
        }
        binding.imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoRecorder();
            }
        });
        binding.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BasicInformationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        findViewById(R.id.tvThuVien).setOnClickListener(v -> openGalleryForMedia());
        binding.btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUris.size() < 2) {
                    CustomToast.makeText(CameraActivity.this, "Bạn phải chọn ít nhất 2 ảnh để đăng bài!", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
                    return;
                } else {
                    CustomDialog.showConfirmationDialog(CameraActivity.this, R.drawable.ld_notification, "XÁC NHẬN", "Thông tin về bài đăng đúng sự thật", false, new ConfirmationDialogListener() {
                        @Override
                        public void onOKClicked() {
                            uploadFiles();
                            if (preferenceManager.getString("motelIDTemp") != null) {
                                updateMotel();
                            } else {
                                insertMotel();
                            }

                        }

                        @Override
                        public void onCancelClicked() {

                        }
                    });
                }
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
        listNameImage.clear();
        for (Uri uri : selectedUris) {
            StorageReference fileRef = storageRef.child("Image/ImageMotel/" + System.currentTimeMillis());
            String fileName = fileRef.getName();
            listNameImage.add(fileName);
            fileRef.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý việc tải lên không thành công
                        CustomToast.makeText(this, "Tải lên thất bại: " + e.getMessage(), CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
                        return;
                    });
        }
        CustomToast.makeText(CameraActivity.this, "Tải lên thành công", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();
        selectedUris.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        adapter.deleteItem(position);
    }

    private void insertMotel() {
        Map<String, Object> data = upDataToMap();
        db.collection(Constants.KEY_COLLECTION_MOTELS).add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            clearPrefernce();
                            Intent intent = new Intent(CameraActivity.this, HomePageActivity.class);
                            startActivity(intent);
                            Log.d(TAG, "Dữ liệu thêm thành công");
                        } else {
                            Log.e(TAG, "Thêm thất bại", task.getException());
                            return;
                        }
                    }
                });
    }

    private void updateMotel() {
        Map<String, Object> data = upDataToMap();
        db.collection(Constants.KEY_COLLECTION_MOTELS).document(preferenceManager.getString("motelIDTemp")).update(data).addOnSuccessListener(aVoid -> {
                    clearPrefernce();
                    deleteOldImagesFromFirebase();
                    Intent intent = new Intent(CameraActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "Dữ liệu đã được cập nhật thành công.");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi khi cập nhật dữ liệu: " + e.getMessage());
                });
    }

    private void clearPrefernce() {
        preferenceManager.clearSpecificPreferences(
                "motelIDTemp",
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
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            CustomToast.makeText(this, "Không thể mở camera", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
        }
    }

    private void openVideoRecorder() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        } else {
            CustomToast.makeText(this, "Không thể mở ứng dụng quay video", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null && extras.containsKey("data")) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri imageUri = getImageUri(this, imageBitmap);
                selectedUris.add(imageUri);
                adapter.notifyDataSetChanged(); // Update RecyclerView
            } else {
                CustomToast.makeText(this, "Không có mục nào được chọn", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();

            }
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK && data != null) {
            Uri videoUri = data.getData();
            selectedUris.add(videoUri);
            adapter.notifyDataSetChanged(); // Update RecyclerView
        }
    }

    private Uri getImageUri(CameraActivity context, Bitmap bitmap) {
        /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);*/
        File imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(imagesDir, "image_" + System.currentTimeMillis() + ".jpg");

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void showErrorMessage(String message) {
        CustomToast.makeText(this, message, CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
    }



    /*private Uri saveImageToInternalStorage(Bitmap bitmap) {
        // Tạo thư mục để lưu ảnh trong bộ nhớ trong của ứng dụng
        File directory = new File(getFilesDir() + "/images");
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục nếu nó không tồn tại
        }
        // Tạo tên tệp ảnh duy nhất
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        File file = new File(directory, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            // Trả về Uri của tệp ảnh đã lưu
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    private void getImagesForMotel() {//Lấy ảnh từ FireBase rồi hiển thị lên recycleView
        DocumentReference docRef = db.collection(Constants.KEY_COLLECTION_MOTELS).document(preferenceManager.getString("motelIDTemp"));
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                imageUrls = (List<String>) documentSnapshot.get(Constants.KEY_IMAGE_LIST);
                if (imageUrls != null && !imageUrls.isEmpty()) {
                    if (adapter == null) {
                        adapter = new GalleryAdapter(selectedUris, CameraActivity.this, CameraActivity.this);
                        adapter.setOnItemClickListener(CameraActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        selectedUris.clear(); // Xóa dữ liệu cũ
                    }

                    for (String imageName : imageUrls) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference pathReference = storageReference.child("Image/ImageMotel/" + imageName);
                        pathReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                            // Chuyển mảng byte thành Bitmap
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            // Lưu ảnh vào bộ nhớ trong và nhận Uri của tệp ảnh đã lưu
                            Uri imageUri = getImageUri(this, bitmap);
                            // Kiểm tra xem Uri có hợp lệ không trước khi thêm vào danh sách
                            if (imageUri != null) {
                                selectedUris.add(imageUri);
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.e(TAG, "Không thể lưu ảnh vào bộ nhớ trong.");
                            }
                        }).addOnFailureListener(exception -> Log.e(TAG, "Error downloading image: " + exception.getMessage()));
                    }
                } else {
                    Log.d(TAG, "Không có ảnh nào.");
                }
            } else {
                Log.d(TAG, "Tài liệu không tồn tại.");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Lỗi khi lấy dữ liệu từ Firestore: " + e.getMessage());
        });
    }

    private Map<String, Object> upDataToMap() {
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_POST_AUTHOR, preferenceManager.getString(Constants.KEY_POST_AUTHOR));
        data.put(Constants.KEY_TITLE, preferenceManager.getString(Constants.KEY_TITLE));
        data.put(Constants.KEY_COUNT_LIKE, preferenceManager.getInt(Constants.KEY_COUNT_LIKE));
        data.put(Constants.KEY_COUNT_AIRCONDITIONER, preferenceManager.getInt(Constants.KEY_COUNT_AIRCONDITIONER));
        data.put(Constants.KEY_LATITUDE, preferenceManager.getFloat(Constants.KEY_LATITUDE));
        data.put(Constants.KEY_LONGTITUDE, preferenceManager.getFloat(Constants.KEY_LONGTITUDE));
        data.put(Constants.KEY_MOTEL_NUMBER, preferenceManager.getString(Constants.KEY_MOTEL_NUMBER));
        data.put(Constants.KEY_WARD_MOTEL, preferenceManager.getInt(Constants.KEY_WARD_MOTEL));
        data.put(Constants.KEY_DISTRICT_MOTEL, preferenceManager.getInt(Constants.KEY_DISTRICT_MOTEL));
        data.put(Constants.KEY_CITY_MOTEL, preferenceManager.getInt(Constants.KEY_CITY_MOTEL));
        data.put(Constants.KEY_CITY_NAME, preferenceManager.getString(Constants.KEY_CITY_NAME));
        data.put(Constants.KEY_DISTRICT_NAME, preferenceManager.getString(Constants.KEY_DISTRICT_NAME));
        data.put(Constants.KEY_WARD_NAME, preferenceManager.getString(Constants.KEY_WARD_NAME));
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
        data.put(Constants.KEY_STATUS_MOTEL, true);
        data.put(Constants.KEY_TYPE_ID, preferenceManager.getInt(Constants.KEY_TYPE_ID));
        data.put(Constants.KEY_IMAGE_LIST, listNameImage);
        return data;
    }

    private void deleteOldImagesFromFirebase() {
        for (String imageName : imageUrls) {
            StorageReference fileRef = storageRef.child("Image/ImageMotel/" + imageName);
            fileRef.delete().addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Ảnh đã được xóa");
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Xóa ảnh thất bại");
            });
        }
    }
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void requestVideoPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_VIDEO_PERMISSION);
        }
    }
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền camera đã được cấp, thực hiện các hành động cần thiết ở đây
            } else {
                showErrorMessage("Không thể mở ứng dụng Camera");
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền truy cập bộ nhớ đã được cấp, thực hiện các hành động cần thiết ở đây
            } else {
                showErrorMessage("Không có quyền bộ nhớ");
            }
        }
    }

}





