package com.motel.mobileproject_motelrental.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.motel.mobileproject_motelrental.R;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>  {
    private List<Uri> mediaList;
    private Context context;

    public GalleryAdapter(Context context, List<Uri> mediaList) {
        this.context = context;
        this.mediaList = mediaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ld_custom_item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Uri mediaUri = mediaList.get(position);
        Glide.with(context)
                .load(mediaUri)
                .into(holder.imageView);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position >= 0 && position < mediaList.size()) { // Kiểm tra vị trí hợp lệ
                    mediaList.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
