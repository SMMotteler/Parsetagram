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
import com.example.parsetagram.models.Comment;
import com.example.parsetagram.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>  {

    public static final String TAG = "PostsAdapter";
    private Context context;
    private List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        Log.i(TAG, "making PostsAdapter");
        this.context = context;
        this.comments = comments;
    }


    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder ");
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder " + position);
        Comment comment = comments.get(position);
        holder.bind(comment);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAuthor;
        private TextView tvBody;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvBody = itemView.findViewById(R.id.tvBody);

            Log.i(TAG, "creating viewholder ");

        }

        public void bind(Comment comment) {
            Log.i(TAG, "binding comment ");

            Log.i(TAG, "adapting post "+comment.getBody());
            tvAuthor.setText(comment.getAuthor().getUsername());
            tvBody.setText(comment.getBody());
        }


    }

}
