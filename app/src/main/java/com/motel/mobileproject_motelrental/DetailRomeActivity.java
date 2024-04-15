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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.motel.mobileproject_motelrental.Adapter.CommentAdapter;
import com.motel.mobileproject_motelrental.Adapter.ImageAdapter;
import com.motel.mobileproject_motelrental.Item.CommentItem;
import com.motel.mobileproject_motelrental.Item.Image;
import com.motel.mobileproject_motelrental.databinding.ActivityDetailRomeBinding;

import java.util.ArrayList;
import java.util.List;

public class DetailRomeActivity extends AppCompatActivity {
    private ActivityDetailRomeBinding binding;
    private List<Image> imageList;
    private ImageAdapter adapter;
    private String TAG = "DetailRomeActivity";


    boolean isFavorite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        imageList = new ArrayList<>();
        imageList.add(new Image(R.drawable.imgroom));
        imageList.add(new Image(R.drawable.imgroom));
        imageList.add(new Image(R.drawable.imgroom));
        imageList.add(new Image(R.drawable.imgroom));

        adapter = new ImageAdapter(imageList, binding.viewPager2);
        binding.viewPager2.setAdapter(adapter);
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("motels").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getId().equals("6CPkXvPbYlD09EFNnp1A")) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            binding.txtTitle.setText(document.getString("title"));
                            binding.txtLove.setText(document.getString("like") + " lượt yêu thích");
                            binding.txtAddress.setText(document.getString("motel number") + ", " + document.getString("ward") + ", " + document.getString("district") + ", " + document.getString("city"));
                            binding.txtCharac.setText(document.getString("characteristics"));
                            binding.txtPrice.setText(document.getString("price"));
                        }
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

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
