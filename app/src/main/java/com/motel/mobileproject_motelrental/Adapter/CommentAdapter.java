package com.motel.mobileproject_motelrental.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.motel.mobileproject_motelrental.Item.CommentItem;
import com.motel.mobileproject_motelrental.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<CommentItem> commentItemList;

    public CommentAdapter(List<CommentItem> commentItemList) {
        this.commentItemList = commentItemList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_binh_luan, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentItem commentItem = commentItemList.get(position);
        holder.bind(commentItem);
    }

    @Override
    public int getItemCount() {
        return commentItemList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatarImageView;
        private TextView nameTextView, dayTextView, contentTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.cmtavatar);
            nameTextView = itemView.findViewById(R.id.cmtname);
            dayTextView = itemView.findViewById(R.id.cmtday);
            contentTextView = itemView.findViewById(R.id.cmtcontent);
        }

        public void bind(CommentItem commentItem) {
            avatarImageView.setImageResource(commentItem.getAvatarResource());
            nameTextView.setText(commentItem.getName());
            dayTextView.setText(commentItem.getDay());
            contentTextView.setText(commentItem.getContent());
        }
    }
}