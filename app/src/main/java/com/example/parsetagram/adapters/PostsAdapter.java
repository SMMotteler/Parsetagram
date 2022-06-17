package com.example.parsetagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parsetagram.MainActivity;
import com.example.parsetagram.PostDetailActivity;
import com.example.parsetagram.ParsetagramHelper;
import com.example.parsetagram.R;
import com.example.parsetagram.models.Post;
import com.example.parsetagram.models.User;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>  {

    public static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        Log.i(TAG, "making PostsAdapter");
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder ");
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder " + position);
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvUsername;
        private TextView tvCaption;
        private ImageView ivPostPhoto;
        private ImageButton ibLike;
        private ImageButton ibComment;
        private ImageView ivProfilePic;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
//            tvUsername = itemView.findViewById(R.id.tvUsername);
//            tvCaption = itemView.findViewById(R.id.tvCaption);
//            ivPostPhoto = itemView.findViewById(R.id.ivPostPhoto);
//            ibLike = itemView.findViewById(R.id.ibLike);
//            ibComment = itemView.findViewById(R.id.ibComment);

            Log.i(TAG, "creating viewholder ");

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent showDetails = new Intent(context, PostDetailActivity.class);
                showDetails.putExtra("clickedPost", post);
                context.startActivity(showDetails);

            }
        }

        public void bind(Post post) {
            Log.i(TAG, "binding post ");

            Log.i(TAG, "adapting post "+post.getDescription());
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            ivPostPhoto = itemView.findViewById(R.id.ivPostPhoto);
            ibLike = itemView.findViewById(R.id.ibLike);
            ibComment = itemView.findViewById(R.id.ibComment);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);


            tvUsername.setText(post.getUser().getUsername());
            tvCaption.setText(post.getDescription());

            Glide.with(context).load(ParsetagramHelper.postPhoto(post)).into(ivPostPhoto);
            Glide.with(context).load(ParsetagramHelper.profilePic(post.getUser())).transform(new CircleCrop()).into(ivProfilePic);
            ParsetagramHelper.checkLikes(post, ibLike);

            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParsetagramHelper.clickLike(post, ibLike);

                    // tvLikes.setText(post.getLikedBy().size()+" likes");

                }
            });

            ivProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity)context;
                    activity.goToProfileTab((User)post.getUser());

                }
            });

            ibComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // go to the compose comment activity
                    MainActivity activity = (MainActivity)context;
                    ParsetagramHelper.clickComment(activity, post);
                }
            });

        }


    }


}

