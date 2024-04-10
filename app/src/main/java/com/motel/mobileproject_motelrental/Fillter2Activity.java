package com.motel.mobileproject_motelrental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.motel.mobileproject_motelrental.Adapter.ChipAdapter;
import com.motel.mobileproject_motelrental.Adapter.InfoMotelAdapter;
import com.motel.mobileproject_motelrental.Adapter.MotelAdapter;
import com.motel.mobileproject_motelrental.Item.ChipItem;
import com.motel.mobileproject_motelrental.Item.InfoMotelItem;
import com.motel.mobileproject_motelrental.Item.MotelItem;
import com.motel.mobileproject_motelrental.databinding.ActivityFillter2Binding;

import java.util.ArrayList;
import java.util.List;

public class Fillter2Activity extends AppCompatActivity {
    private ActivityFillter2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFillter2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.sendback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fillter2Activity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        List<ChipItem> chipItemList = new ArrayList<>();
        chipItemList.add(new ChipItem("Có gác"));
        chipItemList.add(new ChipItem("Gần trường học"));
        // Thêm các mục khác nếu cần

        ChipAdapter adapter = new ChipAdapter(chipItemList);
        adapter.attachToFlowLayout(binding.flowchip);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewKetQua.setLayoutManager(layoutManager);

        List<InfoMotelItem> infoMotelItemList = new ArrayList<>();
        infoMotelItemList.add(new InfoMotelItem(R.drawable.imgphong, "Phòng trọ giá rẻ", "100", "1.000.000", "123 Nguyễn Văn Cừ", "10"));
        infoMotelItemList.add(new InfoMotelItem(R.drawable.imgphong, "Phòng trọ giá rẻ", "100", "1.000.000", "123 Nguyễn Văn Cừ", "10"));
        infoMotelItemList.add(new InfoMotelItem(R.drawable.imgphong, "Phòng trọ giá rẻ", "100", "1.000.000", "123 Nguyễn Văn Cừ", "10"));
        infoMotelItemList.add(new InfoMotelItem(R.drawable.imgphong, "Phòng trọ giá rẻ", "100", "1.000.000", "123 Nguyễn Văn Cừ", "10"));
        infoMotelItemList.add(new InfoMotelItem(R.drawable.imgphong, "Phòng trọ giá rẻ", "100", "1.000.000", "123 Nguyễn Văn Cừ", "10"));
        infoMotelItemList.add(new InfoMotelItem(R.drawable.imgphong, "Phòng trọ giá rẻ", "100", "1.000.000", "123 Nguyễn Văn Cừ", "10"));
        infoMotelItemList.add(new InfoMotelItem(R.drawable.imgphong, "Phòng trọ giá rẻ", "100", "1.000.000", "123 Nguyễn Văn Cừ", "10"));

        InfoMotelAdapter adapterInfo = new InfoMotelAdapter(infoMotelItemList);
        binding.recyclerViewKetQua.setAdapter(adapterInfo);
    }
}