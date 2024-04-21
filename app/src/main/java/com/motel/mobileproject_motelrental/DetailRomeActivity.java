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
import com.motel.mobileproject_motelrental.Adapter.TagAdapter;
import com.motel.mobileproject_motelrental.Item.CommentItem;
import com.motel.mobileproject_motelrental.Item.Image;
import com.motel.mobileproject_motelrental.Item.TagItem;
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
    long likeCount;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    boolean isFavorite = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        motelId = getIntent().getStringExtra("motelId");

        FillDetail();
        FillComment();

        binding.btnYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đổi icon khi click
                if (isFavorite) {
                    binding.btnYeuThich.setImageResource(R.drawable.img_love_red);
                    likeCount ++;
                    isFavorite = false;
                } else {
                    binding.btnYeuThich.setImageResource(R.drawable.img_love);
                    likeCount --;
                    isFavorite = true;
                }
                binding.txtLove.setText(likeCount + " lượt yêu thích");
            }
        });
    }

    public void FillDetail(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                        binding.txtAddress.setText(document.getString(Constants.KEY_MOTEL_NUMBER) + ", " + document.getLong(Constants.KEY_WARD_MOTEL) + ", " + document.getLong(Constants.KEY_DISTRICT_MOTEL) + ", " + document.getLong(Constants.KEY_CITY_MOTEL));
                        binding.txtCharac.setText(document.getString(Constants.KEY_CHARACTERISTIC));
                        long price = document.getLong(Constants.KEY_PRICE);
                        String formattedMinValue = decimalFormat.format(price);
                        binding.txtPrice.setText(formattedMinValue + " VND/tháng");

                        imageList = new ArrayList<>();
                        List<String> imageUrls = (List<String>) document.get(Constants.KEY_IMAGE_LIST);
                        for(String url : imageUrls){
                            imageList.add(new Image(url));
                        }
                        adapter = new ImageAdapter(imageList, binding.viewPager2);
                        binding.viewPager2.setAdapter(adapter);

                        binding.txtDescription.setText(document.getString(Constants.KEY_DESCRIPTION));

                        if(document.getLong(Constants.KEY_PRICE_PARKING) >= 0) binding.bedroom.setText("Bãi đậu xe");
                        else binding.bedroom.setText("Không có bãi đậu xe");

                        if(document.getBoolean(Constants.KEY_NO_HOST) == true) binding.bathroom.setText("Chung chủ");
                        else binding.bathroom.setText("Không chung chủ");

                        binding.area.setText(document.getLong(Constants.KEY_ACREAGE) + " m2");

                        List<TagItem> listTag = new ArrayList<>();
                        if(document.getLong(Constants.KEY_COUNT_FRIDGE) > 0) listTag.add(new TagItem("Tủ lạnh"));
                        if(document.getLong(Constants.KEY_COUNT_AIRCONDITIONER) > 0) listTag.add(new TagItem("Điều hòa"));
                        if(document.getLong(Constants.KEY_COUNT_WASHING_MACHINE) > 0) listTag.add(new TagItem("Máy giặt"));
                        if(document.getBoolean(Constants.KEY_GARET) == true) listTag.add(new TagItem("Gác lửng"));

                        /////////////////////////////
                        //if(document.getLong("Wireless") >= 0) listTag.add(new TagItem("Wifi"));
                        ////////////////////////////
                        if (document.getString(Constants.KEY_START_TIME) == null) listTag.add(new TagItem("Giờ giấc tự do"));

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
