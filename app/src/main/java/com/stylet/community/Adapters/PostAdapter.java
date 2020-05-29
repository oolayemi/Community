package com.stylet.community.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stylet.community.Activities.CommentsActivity;
import com.stylet.community.Models.Post;
import com.stylet.community.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.blog_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {

        holder.postImage.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));

        holder.main_blog_post.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        holder.userName.setText(mData.get(position).getUsername());
        Glide.with(mContext).load(mData.get(position).getUserphoto()).into(holder.userImage);
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.postImage);

        long timestamp = (long) mData.get(position).getTimestamp();
        String date = timestampToString(timestamp);

        holder.postDate.setText(date);

        //holder.postDate.setText(mData.get(position).getTimestamp().toString());
        holder.postDesc.setText(mData.get(position).getDescription());
        holder.postTitle.setText(mData.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView userName;
        TextView postDate;
        TextView postTitle;
        ImageView postImage;

        TextView postDesc;

        TextView blogcommentlabel;
        ImageView blogcommentimage;
        CardView main_blog_post;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.blog_user_image);
            userName = itemView.findViewById(R.id.blog_user_name);
            postDate = itemView.findViewById(R.id.blog_date);
            postTitle = itemView.findViewById(R.id.post_title);
            postImage = itemView.findViewById(R.id.blog_image);
            postDesc = itemView.findViewById(R.id.blog_desc);
            blogcommentlabel = itemView.findViewById(R.id.blog_comment_count);
            blogcommentimage = itemView.findViewById(R.id.blog_comment_icon);
            main_blog_post = itemView.findViewById(R.id.main_blog_post);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentActivity();
                }
            });

            blogcommentlabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentActivity();
                }
            });

            blogcommentimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentActivity();
                }
            });



        }
        public void commentActivity(){
            Intent commentactivity = new Intent(mContext, CommentsActivity.class);

            int position = getAdapterPosition();

            commentactivity.putExtra("title", mData.get(position).getTitle());
            commentactivity.putExtra("postImage", mData.get(position).getPicture());
            commentactivity.putExtra("description", mData.get(position).getDescription());
            commentactivity.putExtra("postKey", mData.get(position).getPostKey());
            commentactivity.putExtra("userPhoto", mData.get(position).getUserphoto());
            commentactivity.putExtra("userName", mData.get(position).getUsername());

            long timestamp = (long) mData.get(position).getTimestamp();
            commentactivity.putExtra("postDate", timestamp);
            mContext.startActivity(commentactivity);

        }


    }private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("MMM dd, yyyy",calendar).toString();
        return date;


    }
}
