package com.motel.mobileproject_motelrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.motel.mobileproject_motelrental.Adapter.CommentAdapter;
import com.motel.mobileproject_motelrental.Adapter.ImageAdapter;
import com.motel.mobileproject_motelrental.Item.CommentItem;
import com.motel.mobileproject_motelrental.Item.Image;
import com.motel.mobileproject_motelrental.databinding.ActivityDetailRomeBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailRomeActivity extends AppCompatActivity {
    private ActivityDetailRomeBinding binding;
    private List<Image> imageList;
    private ImageAdapter adapter;
    private String TAG = "DetailRomeActivity";
    private String motelId;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    boolean isFavorite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        motelId = getIntent().getStringExtra("motelId");

        FillImage();
        FillDetail();
        FillComment();

        binding.btnYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đổi icon khi click
                if (isFavorite) {
                    binding.btnYeuThich.setImageResource(R.drawable.img_love_red);
                    isFavorite = false;
                } else {
                    binding.btnYeuThich.setImageResource(R.drawable.img_love);
                    isFavorite = true;
                }
            }
        });
    }

    public void FillImage(){
        imageList = new ArrayList<>();
        imageList.add(new Image(R.drawable.imgroom));
        imageList.add(new Image(R.drawable.imgroom));
        imageList.add(new Image(R.drawable.imgroom));
        imageList.add(new Image(R.drawable.imgroom));
        adapter = new ImageAdapter(imageList, binding.viewPager2);
        binding.viewPager2.setAdapter(adapter);
    }

    public void FillDetail(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("motels").document(motelId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        // Hiển thị dữ liệu từ document lên giao diện
                        binding.txtTitle.setText(document.getString("title"));
                        long likeCount = document.getLong("like");
                        binding.txtLove.setText(likeCount + " lượt yêu thích");
                        binding.txtAddress.setText(document.getString("motel number") + ", " + document.getString("ward") + ", " + document.getString("district") + ", " + document.getString("city"));
                        binding.txtCharac.setText(document.getString("characteristic"));
                        long price = document.getLong("price");
                        String formattedMinValue = decimalFormat.format(price);
                        binding.txtPrice.setText(formattedMinValue + " /tháng");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

    public  void FillComment(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewBinhLuan.setLayoutManager(layoutManager);
        List<CommentItem> commentItemList = new ArrayList<>();
        commentItemList.add(new CommentItem(R.drawable.imgavatar, "Nguyễn Văn A", "20/10/2021", "Phòng đẹp, giá hợp lý"));
        commentItemList.add(new CommentItem(R.drawable.imgavatar, "Nguyễn Văn A", "20/10/2021", "Phòng đẹp, giá hợp lý"));
        commentItemList.add(new CommentItem(R.drawable.imgavatar, "Nguyễn Văn A", "20/10/2021", "Phòng đẹp, giá hợp lý"));
        commentItemList.add(new CommentItem(R.drawable.imgavatar, "Nguyễn Văn A", "20/10/2021", "Phòng đẹp, giá hợp lý"));
        commentItemList.add(new CommentItem(R.drawable.imgavatar, "Nguyễn Văn A", "20/10/2021", "Phòng đẹp, giá hợp lý"));
        commentItemList.add(new CommentItem(R.drawable.imgavatar, "Nguyễn Văn A", "20/10/2021", "Phòng đẹp, giá hợp lý"));

        CommentAdapter adapterCmt = new CommentAdapter(commentItemList);
        binding.recyclerViewBinhLuan.setAdapter(adapterCmt);
    }
}
