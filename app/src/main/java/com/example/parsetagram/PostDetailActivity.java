package com.example.parsetagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parsetagram.adapters.CommentsAdapter;
import com.example.parsetagram.models.Comment;
import com.example.parsetagram.models.Post;
import com.example.parsetagram.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {
    RecyclerView rvComments;
    CommentsAdapter adapter;
    List<Comment> allComments;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        TextView tvUsername = findViewById(R.id.tvUsername);
        TextView tvDate = findViewById(R.id.tvDate);
        ImageView ivPhoto = findViewById(R.id.ivPhoto);
        ImageView ivProfilePic = findViewById(R.id.ivProfilePic);
        ImageButton ibLike = findViewById(R.id.ibLike);
        ImageButton ibComment = findViewById(R.id.ibComment);
        TextView tvLikes = findViewById(R.id.tvLikes);
        TextView tvCaption = findViewById(R.id.tvCaption);
        rvComments = findViewById(R.id.rvComments);

        post = getIntent().getParcelableExtra("clickedPost");

        ParsetagramHelper.checkLikes(post, ibLike);

        tvUsername.setText(post.getUser().getUsername());
        tvDate.setText(post.getUpdatedAt().toString());
        tvCaption.setText(post.getDescription());
        tvLikes.setText(post.getLikedBy().size()+" likes");
        Glide.with(this).load(ParsetagramHelper.postPhoto(post)).into(ivPhoto);
        Glide.with(this).load(ParsetagramHelper.profilePic(post.getUser())).transform(new CircleCrop()).into(ivProfilePic);

        ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to the compose comment activity
                ParsetagramHelper.clickComment(PostDetailActivity.this, post);
            }
        });

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParsetagramHelper.clickLike(post, ibLike);
                tvLikes.setText(post.getLikesCount());

            }
        });

        allComments = new ArrayList<>();
        adapter = new CommentsAdapter(this, allComments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(linearLayoutManager);

        queryComments();

    }

    @Override
    protected void onRestart() {
        // fires when we come back from future activities
        super.onRestart();

        queryComments();
    }

    private void queryComments() {
        // load all comments for this post
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.whereEqualTo(Comment.KEY_POST, post);
        query.addDescendingOrder("createdAt");
        query.include(Comment.KEY_AUTHOR);
        // start an asynchronous call for comments
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e("Detail Activity", "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every comment description to logcat
                for (Comment comment : comments) {
                    Log.i("Detail Activity", "Post: " + comment.getBody() + ", username: " + (comment.getAuthor()).getUsername());
                }

                // save received comments to list and notify adapter of new data
                allComments.clear();
                allComments.addAll(comments);

                adapter.notifyDataSetChanged();
            }
        });

    }

}