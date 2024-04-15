package com.motel.mobileproject_motelrental.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.motel.mobileproject_motelrental.CameraActivity;
import com.motel.mobileproject_motelrental.R;

import java.util.List;


public class GalleryAdapter extends  RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>  {
    private List<Uri> imageUris;
    private OnItemClickListener mListener;
    private StorageReference storageRef;
    private Context mContext; // Add a Context variable

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageView deleteButton;

        public GalleryViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            deleteButton = itemView.findViewById(R.id.btnDelete);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public GalleryAdapter(List<Uri> imageUris, OnItemClickListener listener, Context context) {
        this.imageUris = imageUris;
        mListener = listener;
        mContext = context; // Assign Context from the constructor

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ld_custom_item_image, parent, false);
        return new GalleryViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        Uri uri = imageUris.get(position);
        holder.imageView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    public void deleteItem(int position) {
        imageUris.remove(position);
        notifyItemRemoved(position);
    }

}
