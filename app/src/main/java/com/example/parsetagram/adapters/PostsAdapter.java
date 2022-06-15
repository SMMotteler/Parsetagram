package com.example.parsetagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parsetagram.PostDetailActivity;
import com.example.parsetagram.R;
import com.example.parsetagram.models.Post;
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

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            ivPostPhoto = itemView.findViewById(R.id.ivPostPhoto);

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
            tvUsername.setText(post.getUser().getUsername());
            tvCaption.setText(post.getDescription());
            ParseFile image = post.getImage();
            if (image == null){
                ivPostPhoto.setVisibility(View.GONE);
            }
            if (image != null) {
                ivPostPhoto.setVisibility(View.VISIBLE);
                Log.i(TAG, "Image: "+ image.getUrl());
                Glide.with(context).load(image.getUrl()).into(ivPostPhoto);

            }
        }


    }


}

