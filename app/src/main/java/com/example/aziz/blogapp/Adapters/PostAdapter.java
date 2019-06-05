package com.example.aziz.blogapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aziz.blogapp.Activities.PostDetailActivity;
import com.example.aziz.blogapp.Models.Comment;
import com.example.aziz.blogapp.Models.Like;
import com.example.aziz.blogapp.Models.Post;
import com.example.aziz.blogapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;


    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        holder.tvTitle.setText(mData.get(position).getDescription());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);
        long time = (Long) mData.get(position).getTimeStamp();
        holder.tvDate.setText(timestampToString(time).concat(timestampTotime((time))));

        holder.tvName.setText(mData.get(position).getUserName());
        holder.iniRvComment();
        holder.iniRvLike();
//        holder.initLikes();

    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy ", calendar).toString();
        return date;


    }

    private String timestampTotime(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm a", calendar).toString();
        return date;


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final String COMMENT_KEY = "Comment";
        final String Likes_KEY = "Like";
        FirebaseAuth firebaseAuth;
        FirebaseUser firebaseUser;
        FirebaseDatabase firebaseDatabase;
        List<Comment> listComment;
        TextView tvTitle, tvName, tvDate, tvLikes, tvComments, tvLikeBtn;
        ImageView imgPost;
        CircleImageView imgPostProfile;

        public MyViewHolder(View itemView) {
            super(itemView);


            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();

            tvTitle = itemView.findViewById(R.id.row_post_title);
            imgPost = itemView.findViewById(R.id.row_post_img);
            tvName = itemView.findViewById(R.id.row_post_profile_name);
            tvDate = itemView.findViewById(R.id.row_post_profile_date);
            tvLikes = itemView.findViewById(R.id.post_like_counter);
            tvComments = itemView.findViewById(R.id.post_comment_counter);
            tvLikeBtn = itemView.findViewById(R.id.post_like_button);
            imgPostProfile = itemView.findViewById(R.id.row_post_profile_img);

            tvLikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initLikes();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("postImage", mData.get(position).getPicture());
                    postDetailActivity.putExtra("description", mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey", mData.get(position).getPostKey());
                    postDetailActivity.putExtra("userPhoto", mData.get(position).getUserPhoto());
                    long timestamp = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("postDate", timestamp);
                    mContext.startActivity(postDetailActivity);


                }
            });

        }

        private void iniRvComment() {

            DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(mData.get(getAdapterPosition()).getPostKey());
            commentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listComment = new ArrayList<>();
                    int count = 0;
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        count++;
                    }
                    String commentsn = String.valueOf(count) + " ";
                    tvComments.setText(commentsn.concat(mContext.getString(R.string.Comments)));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        private void iniRvLike() {

            DatabaseReference commentRef = firebaseDatabase.getReference(Likes_KEY).child(mData.get(getAdapterPosition()).getPostKey());

            commentRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    setLikeButtonBlue(false);
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            commentRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int c = 0;
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Like like = snap.getValue(Like.class);
                        if (Objects.requireNonNull(Objects.requireNonNull(snap.getValue(Like.class)).getUserID()).equals(firebaseUser.getUid())) {
                            setLikeButtonBlue(true);
                        }
                        c++;
                    }
                    String likesn = String.valueOf(c) + " ";
                    tvLikes.setText(likesn.concat(mContext.getString(R.string.Likes)));
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        public void initLikes() {

            DatabaseReference Likequery = firebaseDatabase.getReference(Likes_KEY).child(mData.get(getAdapterPosition()).getPostKey());
            Likequery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    showData(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        void setLikeButtonBlue(boolean isLiked) {
            if (isLiked) {
                tvLikeBtn.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_like_blue_icon), null, null, null);
            } else {
                tvLikeBtn.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_like_gray_icon), null, null, null);
            }
        }

        void showData(DataSnapshot dataSnapshot) {
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                Like like = snap.getValue(Like.class);
                if (like.getUserID().equals(firebaseUser.getUid())) {
                    firebaseDatabase.getReference(Likes_KEY).child(mData.get(getAdapterPosition()).getPostKey()).child(like.getLikeKey()).removeValue();
                    return;
                }
            }


            DatabaseReference LikeReference = firebaseDatabase.getReference(Likes_KEY).child(mData.get(getAdapterPosition()).getPostKey()).push();

            String key = LikeReference.getKey();
            String uid = firebaseUser.getUid();
            Like like = new Like(uid, key);
            LikeReference.setValue(like).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

    }


}
