package com.motel.mobileproject_motelrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.motel.mobileproject_motelrental.Adapter.CommentAdapter;
import com.motel.mobileproject_motelrental.Adapter.ImageAdapter;
import com.motel.mobileproject_motelrental.Adapter.TagAdapter;
import com.motel.mobileproject_motelrental.Item.CommentItem;
import com.motel.mobileproject_motelrental.Item.Image;
import com.motel.mobileproject_motelrental.Item.InfoMotelItem;
import com.motel.mobileproject_motelrental.Item.TagItem;
import com.motel.mobileproject_motelrental.databinding.ActivityDetailRomeBinding;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailRomeActivity extends AppCompatActivity {
    private ActivityDetailRomeBinding binding;
    private List<Image> imageList;
    private ImageAdapter adapter;
    private String TAG = "DetailRomeActivity";
    private String motelId;
    long likeCount;
    boolean likeStatus = true;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        motelId = getIntent().getStringExtra("motelId");
        CheckLiked(new CallBack() {
            @Override
            public void onLoaded(boolean islike) {
                if (islike == true) {
                    binding.btnYeuThich.setImageResource(R.drawable.img_love_red);
                    likeStatus = false;
                } else {
                    binding.btnYeuThich.setImageResource(R.drawable.img_love);
                    likeStatus = true;
                }
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageReference.child("avatar/" + "2024_04_19_05_45_49");

        pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(binding.usercmt);
        }).addOnFailureListener(exception -> {
        });

        FillDetail();
        FillComment();

        binding.btnYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đổi icon khi click
                if (likeStatus) {
                    binding.btnYeuThich.setImageResource(R.drawable.img_love_red);
                    likeCount++;
                    likeStatus = false;
                    insertListLike();
                } else {
                    binding.btnYeuThich.setImageResource(R.drawable.img_love);
                    likeCount--;
                    likeStatus = true;
                    deleteListLike();
                }
                binding.txtLove.setText(likeCount + " lượt yêu thích");
                updateLike();
            }
        });

        binding.sendcmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = "Thêm bình luận thành công!";
                addComments();
                binding.edtcmt.setText("");
                FillComment();
                InputMethodManager imm = (InputMethodManager) DetailRomeActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                Snackbar.make(DetailRomeActivity.this.getCurrentFocus(), label, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.bluenote)).show();
            }
        });
    }

    private void CheckLiked(CallBack islike) {

        db.collection(Constants.KEY_COLLECTION_LIKES)
                .whereEqualTo(Constants.KEY_MOTEL_LIKER, "hdUDaeIQeIbErYFNakZw")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString(Constants.KEY_LIKED_MOTEL).equals(motelId)) {
                                    likeStatus = true;
                                    binding.btnYeuThich.setImageResource(R.drawable.img_love_red);

                                    if (islike != null) {
                                        islike.onLoaded(likeStatus);
                                    }
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    // Interface để định nghĩa callback
    interface CallBack {
        void onLoaded(boolean islike);
    }


    private void updateLike() {
        Map<String, Object> data = updateDataToMap();
        db.collection(Constants.KEY_COLLECTION_MOTELS).document(motelId).update(data).addOnSuccessListener(aVoid -> {
            Log.d(TAG, "Dữ liệu đã được cập nhật thành công.");
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Lỗi khi cập nhật dữ liệu: " + e.getMessage());
        });
    }

    private Map<String, Object> updateDataToMap() {
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_COUNT_LIKE, likeCount);
        return data;
    }

    private void insertListLike() {
        Map<String, Object> data = insertDataToMap();
        db.collection(Constants.KEY_COLLECTION_LIKES).add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Dữ liệu thêm thành công");
                        } else {
                            Log.e(TAG, "Thêm thất bại", task.getException());
                            return;
                        }
                    }
                });
    }

    private void deleteListLike(){
        db.collection(Constants.KEY_COLLECTION_LIKES)
                .whereEqualTo(Constants.KEY_MOTEL_LIKER, "hdUDaeIQeIbErYFNakZw")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if(documentSnapshot.getString(Constants.KEY_LIKED_MOTEL).equals(motelId)){
                            db.collection(Constants.KEY_COLLECTION_LIKES).document(documentSnapshot.getId()).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Xóa dữ liệu thành công");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Lỗi khi xóa dữ liệu: " + e.getMessage());
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Xảy ra lỗi trong quá trình lấy dữ liệu hoặc xóa dữ liệu
                    Log.e(TAG, "Lỗi khi tìm kiếm tài liệu: " + e.getMessage());
                });
    }

    private Map<String, Object> insertDataToMap() {
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_MOTEL_LIKER, "hdUDaeIQeIbErYFNakZw");
        data.put(Constants.KEY_LIKED_MOTEL, motelId);
        data.put(Constants.KEY_LIKED_MOTEL_TITLE, binding.txtTitle.getText().toString());
        data.put(Constants.KEY_LIKED_MOTEL_LIKE_COUNT, likeCount);
        data.put(Constants.KEY_LIKED_MOTEL_IMAGE, imageList.get(0));
        data.put(Constants.KEY_LIKED_MOTEL_PRICE, binding.txtPrice.getText().toString());
        data.put(Constants.KEY_LIKED_MOTEL_ADDRESS, binding.txtAddress.getText().toString());
        data.put(Constants.KEY_LIKED_MOTEL_COMMENT_COUNT, binding.cmtcount.getText());
        return data;
    }


    public void FillDetail() {

        db.collection(Constants.KEY_COLLECTION_MOTELS).document(motelId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        // Hiển thị dữ liệu từ document lên giao diện
                        binding.txtTitle.setText(document.getString(Constants.KEY_TITLE));
                        likeCount = document.getLong(Constants.KEY_COUNT_LIKE);
                        binding.txtLove.setText(likeCount + " lượt yêu thích");
                        binding.txtAddress.setText(document.getString(Constants.KEY_MOTEL_NUMBER) + ", " + document.getString(Constants.KEY_WARD_NAME) + ", " + document.getString(Constants.KEY_DISTRICT_NAME) + ", " + document.getString(Constants.KEY_CITY_NAME));
                        binding.txtCharac.setText(document.getString(Constants.KEY_CHARACTERISTIC));
                        long price = document.getLong(Constants.KEY_PRICE);
                        String formattedMinValue = decimalFormat.format(price);
                        binding.txtPrice.setText(formattedMinValue + " VND/tháng");

                        imageList = new ArrayList<>();
                        List<String> imageUrls = (List<String>) document.get(Constants.KEY_IMAGE_LIST);
                        for (String url : imageUrls) {
                            imageList.add(new Image(url));
                        }
                        adapter = new ImageAdapter(imageList, binding.viewPager2);
                        binding.viewPager2.setAdapter(adapter);

                        binding.txtDescription.setText(document.getString(Constants.KEY_DESCRIPTION));

                        if (document.getLong(Constants.KEY_PRICE_PARKING) >= 0)
                            binding.bedroom.setText("Bãi đậu xe");
                        else binding.bedroom.setText("Không chỗ để xe");

                        if (document.getBoolean(Constants.KEY_NO_HOST) == true)
                            binding.bathroom.setText("Chung chủ");
                        else binding.bathroom.setText("Không chung chủ");

                        binding.area.setText(document.getLong(Constants.KEY_ACREAGE) + " m2");

                        List<TagItem> listTag = new ArrayList<>();
                        if (document.getLong(Constants.KEY_COUNT_FRIDGE) > 0)
                            listTag.add(new TagItem("Tủ lạnh"));
                        if (document.getLong(Constants.KEY_COUNT_AIRCONDITIONER) > 0)
                            listTag.add(new TagItem("Điều hòa"));
                        if (document.getLong(Constants.KEY_COUNT_WASHING_MACHINE) > 0)
                            listTag.add(new TagItem("Máy giặt"));
                        if (document.getBoolean(Constants.KEY_GARET) == true)
                            listTag.add(new TagItem("Gác lửng"));
                        if (document.getLong(Constants.KEY_PRICE_WIFI) >= 0)
                            listTag.add(new TagItem("Wifi"));
                        if (document.getString(Constants.KEY_START_TIME) == null)
                            listTag.add(new TagItem("Giờ giấc tự do"));

                        TagAdapter adapterTag = new TagAdapter(listTag);
                        adapterTag.attachToFlowLayout(binding.flowtag);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

    private void addComments() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();

        // Lấy ngày và giờ từ Calendar
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY); // Giờ theo định dạng 24 giờ
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // Định dạng thời gian thành chuỗi
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String time = sdf.format(new Date(year - 1900, month - 1, dayOfMonth, hourOfDay, minute, second));

        Map<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_COMMENTER, "hdUDaeIQeIbErYFNakZw");
        data.put(Constants.KEY_COMMENTER_NAME, "Nguyễn Văn B");
        data.put(Constants.KEY_COMMENTER_IMAGE, "2024_04_19_05_45_49");
        data.put(Constants.KEY_COMMENT_MOTEL, motelId);
        data.put(Constants.KEY_TIME_COMMENT, time);
        data.put(Constants.KEY_CONTENT_COMMENT, binding.edtcmt.getText().toString());

        db.collection(Constants.KEY_COLLECTION_COMMENTS).add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        } else {
                            Log.w(TAG, "Error writing document", task.getException());
                            return;
                        }
                    }
                });
    }

    public void FillComment() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewBinhLuan.setLayoutManager(layoutManager);
        List<CommentItem> commentItemList = new ArrayList<>();
        CommentAdapter adapterCmt = new CommentAdapter(commentItemList);

        db.collection(Constants.KEY_COLLECTION_COMMENTS)
                .orderBy(Constants.KEY_TIME_COMMENT, Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        commentItemList.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString(Constants.KEY_COMMENT_MOTEL).equals(motelId)) {
                                    String day = document.getString(Constants.KEY_TIME_COMMENT);
                                    String content = document.getString(Constants.KEY_CONTENT_COMMENT);
                                    String formattedTime = day;
                                    String avatar = document.getString(Constants.KEY_COMMENTER_IMAGE);
                                    String name = document.getString(Constants.KEY_COMMENTER_NAME);
                                    try {
                                        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                                        Date date = sdfInput.parse(day);

                                        SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                        formattedTime = sdfOutput.format(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    commentItemList.add(new CommentItem(avatar, name, formattedTime, content));
                                }
                            }
                            binding.recyclerViewBinhLuan.setAdapter(adapterCmt);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
