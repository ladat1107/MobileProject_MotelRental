package com.motel.mobileproject_motelrental.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.motel.mobileproject_motelrental.Interface.OnItemRecycleClickListener;
import com.motel.mobileproject_motelrental.Item.MotelItem;
import com.motel.mobileproject_motelrental.R;

import java.util.List;

public class MotelAdapter extends RecyclerView.Adapter<MotelAdapter.MotelViewHolder> {
    private List<MotelItem> motelItemList;
    private OnItemRecycleClickListener onItemRecycleClickListener;
    public MotelAdapter(List<MotelItem> motelItemList) {
        this.motelItemList = motelItemList;
    }

    @NonNull
    @Override
    public MotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_motel, parent, false);
        return new MotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MotelViewHolder holder, int position) {
        MotelItem motelItem = motelItemList.get(position);
        holder.bind(motelItem);

        final int itemPosition = position; // Khai báo một biến final khác để lưu trữ giá trị của position

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemRecycleClickListener != null) {
                    onItemRecycleClickListener.onItemClick(itemPosition); // Sử dụng biến final này trong phương thức onClick
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return motelItemList.size();
    }

    public static class MotelViewHolder extends RecyclerView.ViewHolder {
        private ImageView motelImage;
        private TextView title;
        private TextView address;
        private TextView likeCount;

        public MotelViewHolder(@NonNull View itemView) {
            super(itemView);
            motelImage = itemView.findViewById(R.id.img_motel);
            title = itemView.findViewById(R.id.txt_title_motel);
            address = itemView.findViewById(R.id.txt_dia_chi_motel);
            likeCount = itemView.findViewById(R.id.txt_like);
        }

        public void bind(MotelItem motelItem) {
            motelImage.setImageResource(motelItem.getMotelImage());
            title.setText(motelItem.getTitle());
            address.setText(motelItem.getAddress());
            likeCount.setText(motelItem.getLikeCount() + " lượt yêu thích.");
        }
    }
    public void setOnItemRecycleClickListener(OnItemRecycleClickListener onItemRecycleClickListener) {
        this.onItemRecycleClickListener = onItemRecycleClickListener;
    }
}
