package com.motel.mobileproject_motelrental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.motel.mobileproject_motelrental.Item.CommentItem;
import com.motel.mobileproject_motelrental.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RepCommentAdapter extends RecyclerView.Adapter<RepCommentAdapter.RepCommentViewHolder> {
    private List<CommentItem> commentList;
   // private Context context;

    public RepCommentAdapter(List<CommentItem> commentList) {
        this.commentList = commentList;
    }

    @Override
    public RepCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rep_comment, parent, false);
        return new RepCommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RepCommentViewHolder holder, int position) {
        CommentItem comment = commentList.get(position);
        holder.bind(comment);

        holder.cmtrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle reply button click
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class RepCommentViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView cmtavatar;
        public TextView cmtname;
        public TextView cmtday;
        public TextView cmtcontent;
        public ImageView cmtlike;
        public ImageView cmtrep;

        public RepCommentViewHolder(View view) {
            super(view);
            cmtavatar = view.findViewById(R.id.cmtavatar);
            cmtname = view.findViewById(R.id.cmtname);
            cmtday = view.findViewById(R.id.cmtday);
            cmtcontent = view.findViewById(R.id.cmtcontent);
            cmtlike = view.findViewById(R.id.cmtlike);
            cmtrep = view.findViewById(R.id.cmtrep);
        }

        public void bind(CommentItem commentItem) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference pathReference = storageReference.child("avatar/" + commentItem.getAvatarResource());

            pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(cmtavatar);
            }).addOnFailureListener(exception -> {});

            cmtname.setText(commentItem.getName());
            cmtday.setText(commentItem.getDay());
            cmtcontent.setText(commentItem.getContent());
        }
    }
}
