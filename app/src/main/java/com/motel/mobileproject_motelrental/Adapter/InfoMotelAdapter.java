package com.motel.mobileproject_motelrental.Adapter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.motel.mobileproject_motelrental.Item.InfoMotelItem;
import com.motel.mobileproject_motelrental.R;

public class InfoMotelAdapter extends RecyclerView.Adapter<InfoMotelAdapter.MotelViewHolder> {
    private List<InfoMotelItem> infoMotelItemList;

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
    }

    @Override
    public int getItemCount() {
        return infoMotelItemList.size();
    }

    public static class MotelViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txtTitle, txtLike, txtPrice, txtAddress, txtComment;

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
            txtLike.setText(motelItem.getLikeCount());
            txtPrice.setText(motelItem.getPrice());
            txtAddress.setText(motelItem.getAddress());
            txtComment.setText(motelItem.getCommentCount());
        }
    }
}
