package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

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
