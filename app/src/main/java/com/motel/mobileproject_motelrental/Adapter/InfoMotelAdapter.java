package com.motel.mobileproject_motelrental.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.motel.mobileproject_motelrental.Item.InfoMotelItem;
import com.motel.mobileproject_motelrental.Interface.OnItemClickListener;
import com.motel.mobileproject_motelrental.R;

import java.text.DecimalFormat;
import java.util.List;

public class InfoMotelAdapter extends RecyclerView.Adapter<InfoMotelAdapter.MotelViewHolder> {
    private List<InfoMotelItem> infoMotelItemList;
    private OnItemClickListener onItemClickListener;
    public InfoMotelAdapter(List<InfoMotelItem> motelItemList) {
        this.infoMotelItemList = motelItemList;
    }

    @NonNull
    @Override
    public MotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_motel_layout, parent, false);
        return new MotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MotelViewHolder holder, int position) {
        InfoMotelItem motelItem = infoMotelItemList.get(position);
        holder.bind(motelItem);

        final int itemPosition = position; // Khai báo một biến final khác để lưu trữ giá trị của position

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(itemPosition); // Sử dụng biến final này trong phương thức onClick
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoMotelItemList.size();
    }

    public class MotelViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txtTitle, txtLike, txtPrice, txtAddress, txtComment;
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        public MotelViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_infor_motel);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtLike = itemView.findViewById(R.id.txtLike);
            txtPrice = itemView.findViewById(R.id.txtGia);
            txtAddress = itemView.findViewById(R.id.txtDiaChi);
            txtComment = itemView.findViewById(R.id.txtBinhLuan);
        }

        public void bind(InfoMotelItem motelItem) {
            imageView.setImageResource(motelItem.getImageResource());
            txtTitle.setText(motelItem.getTitle());
            txtLike.setText(String.valueOf(motelItem.getLikeCount()) + " lượt yêu thích");
            String formattedMinValue = decimalFormat.format(motelItem.getPrice());
            txtPrice.setText(formattedMinValue);
            txtAddress.setText(motelItem.getAddress());
            txtComment.setText(String.valueOf(motelItem.getCommentCount()));
        }
    }
    public void setOnItemRecycleClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
