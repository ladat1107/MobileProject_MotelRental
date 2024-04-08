package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.motel.mobileproject_motelrental.Adapter.ImageAdapter;
import com.motel.mobileproject_motelrental.Item.Image;
import com.motel.mobileproject_motelrental.databinding.ActivityDetailRomeBinding;

import java.util.ArrayList;
import java.util.List;

public class DetailRomeActivity extends AppCompatActivity {
    private ActivityDetailRomeBinding binding;
    private List<Image> imageList;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailRomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        imageList = new ArrayList<>();
        imageList.add(new Image(R.drawable.imgroom));
        imageList.add(new Image(R.drawable.imgroom));
        imageList.add(new Image(R.drawable.imgroom));
        imageList.add(new Image(R.drawable.imgroom));

        adapter = new ImageAdapter(imageList, binding.viewPager2);
        binding.viewPager2.setAdapter(adapter);
    }
}
