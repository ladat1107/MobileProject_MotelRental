package com.motel.mobileproject_motelrental.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.motel.mobileproject_motelrental.Constants;
import com.motel.mobileproject_motelrental.Item.CommentItem;
import com.motel.mobileproject_motelrental.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.motel.mobileproject_motelrental.databinding.LayoutRepCommentBinding;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<CommentItem> commentItemList;
    boolean islike = false;
    boolean viewCmt = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "CommentAdapter";

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

        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islike == false) {
                    holder.imgLike.setImageResource(R.drawable.imglikeblue);
                    islike = true;
                } else {
                    holder.imgLike.setImageResource(R.drawable.img_like);
                    islike = false;
                }
            }
        });

        holder.cmtRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewCmt == false) {
                    holder.llrepcomment.setVisibility(View.VISIBLE);
                    holder.cmtRep.setImageResource(R.drawable.messageblue);
                    viewCmt = true;

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference pathReference = storageReference.child("avatar/" + commentItem.getAvatarResource());

                    pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        Picasso.get().load(uri).into(holder.userRep);
                    }).addOnFailureListener(exception -> {});

                    List<CommentItem> commentItemList = new ArrayList<>();
                    RepCommentAdapter adapterCmt = new RepCommentAdapter(commentItemList);

                    db.collection(Constants.KEY_COLLECTION_REP_COMMENTS)
                            .orderBy(Constants.KEY_REP_COMMENT_TIME, Query.Direction.ASCENDING)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    commentItemList.clear();
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (document.getString(Constants.KEY_REP_COMMENT_ID).equals(commentItem.getId())) {
                                                String id = document.getId();
                                                String day = document.getString(Constants.KEY_REP_COMMENT_TIME);
                                                String content = document.getString(Constants.KEY_REP_COMMENT_CONTENT);
                                                String formattedTime = day;
                                                String avatar = document.getString(Constants.KEY_REP_COMMENTER_IMAGE);
                                                String name = document.getString(Constants.KEY_REP_COMMENTER_NAME);
                                                try {
                                                    SimpleDateFormat sdfInput = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                                                    Date date = sdfInput.parse(day);

                                                    SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                                    formattedTime = sdfOutput.format(date);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                commentItemList.add(new CommentItem(id, avatar, name, formattedTime, content));
                                            }
                                        }
                                        holder.recyclerViewRepComment.setAdapter(adapterCmt);
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                }
                            });
                } else {
                    holder.llrepcomment.setVisibility(View.GONE);
                    holder.recyclerViewRepComment.setAdapter(null);
                    holder.cmtRep.setImageResource(R.drawable.message);
                    viewCmt = false;
                }
            }
        });

        holder.sendRepCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String repContent = holder.edtRep.getText().toString();
                addRepComment(commentItem, repContent);
                holder.edtRep.setText("");

                //Fill lại comment
                List<CommentItem> commentItemList = new ArrayList<>();
                RepCommentAdapter adapterCmt = new RepCommentAdapter(commentItemList);

                db.collection(Constants.KEY_COLLECTION_REP_COMMENTS)
                        .orderBy(Constants.KEY_REP_COMMENT_TIME, Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                commentItemList.clear();
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getString(Constants.KEY_REP_COMMENT_ID).equals(commentItem.getId())) {
                                            String id = document.getId();
                                            String day = document.getString(Constants.KEY_REP_COMMENT_TIME);
                                            String content = document.getString(Constants.KEY_REP_COMMENT_CONTENT);
                                            String formattedTime = day;
                                            String avatar = document.getString(Constants.KEY_REP_COMMENTER_IMAGE);
                                            String name = document.getString(Constants.KEY_REP_COMMENTER_NAME);
                                            try {
                                                SimpleDateFormat sdfInput = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                                                Date date = sdfInput.parse(day);

                                                SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                                formattedTime = sdfOutput.format(date);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            commentItemList.add(new CommentItem(id, avatar, name, formattedTime, content));
                                        }
                                    }
                                    holder.recyclerViewRepComment.setAdapter(adapterCmt);
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentItemList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatarImageView, imgLike, cmtRep, sendRepCmt;
        private TextView nameTextView, dayTextView, contentTextView;
        private LinearLayout llrepcomment;
        private EditText edtRep;
        private RoundedImageView userRep;
        private RecyclerView recyclerViewRepComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.cmtavatar);
            imgLike = itemView.findViewById(R.id.cmtlike);
            cmtRep = itemView.findViewById(R.id.cmtrep);
            nameTextView = itemView.findViewById(R.id.cmtname);
            dayTextView = itemView.findViewById(R.id.cmtday);
            contentTextView = itemView.findViewById(R.id.cmtcontent);
            llrepcomment = itemView.findViewById(R.id.llrepcomment);
            edtRep = itemView.findViewById(R.id.edtrep);
            userRep = itemView.findViewById(R.id.userrep);
            sendRepCmt = itemView.findViewById(R.id.sendrepcmt);
            recyclerViewRepComment = itemView.findViewById(R.id.recyclerViewRepComment);

            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewRepComment.setLayoutManager(layoutManager);
        }

        public void bind(CommentItem commentItem) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference pathReference = storageReference.child("avatar/" + commentItem.getAvatarResource());

            pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(avatarImageView);
            }).addOnFailureListener(exception -> {});

            nameTextView.setText(commentItem.getName());
            dayTextView.setText(commentItem.getDay());
            contentTextView.setText(commentItem.getContent());
        }
    }

    public void addRepComment(CommentItem commentItem, String repContent){
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();

        // Lấy ngày và giờ từ Calendar
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY); // Giờ theo định dạng 24 giờ
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // Định dạng thời gian thành chuỗi
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String time = sdf.format(new Date(year - 1900, month - 1, dayOfMonth, hourOfDay, minute, second));

        Map<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_REP_COMMENTER, "hdUDaeIQeIbErYFNakZw");
        data.put(Constants.KEY_REP_COMMENTER_NAME, "Nguyễn Văn B");
        data.put(Constants.KEY_REP_COMMENTER_IMAGE, commentItem.getAvatarResource());

        data.put(Constants.KEY_REP_COMMENT_ID, commentItem.getId());
        data.put(Constants.KEY_REP_COMMENT_NAME, commentItem.getName());
        data.put(Constants.KEY_REP_COMMENT_TIME, time);
        data.put(Constants.KEY_REP_COMMENT_CONTENT, repContent);
        //còn xử lý
        data.put(Constants.KEY_REP_COMMENT_LIKE, 0);

        db.collection(Constants.KEY_COLLECTION_REP_COMMENTS).add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        } else {
                            Log.w(TAG, "Error writing document", task.getException());
                            return;
                        }
                    }
                });
    }
}