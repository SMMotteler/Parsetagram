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
import com.example.parsetagram.adapters.CommentsAdapter;
import com.example.parsetagram.adapters.PostsAdapter;
import com.example.parsetagram.models.Comment;
import com.example.parsetagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
        ImageButton ibLike = findViewById(R.id.ibLike);
        ImageButton ibComment = findViewById(R.id.ibComment);
        TextView tvLikes = findViewById(R.id.tvLikes);
        rvComments = findViewById(R.id.rvComments);

        post = getIntent().getParcelableExtra("clickedPost");

        tvUsername.setText(post.getUser().getUsername());
        tvDate.setText(post.getUpdatedAt().toString());
        Glide.with(this).load(post.getImage().getUrl()).into(ivPhoto);

        ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to the compose comment activity
                Intent i = new Intent(PostDetailActivity.this, ComposeCommentActivity.class);
                i.putExtra("post", post);
                startActivity(i);
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

    private void populate(Post post) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("objectId", getIntent().getStringExtra("id"));
        query.findInBackground(new FindCallback<Post>() {
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    Log.i("Detail", "found post!, "+posts.size());
                    // Log.i("Detail", "post key "+getIntent().getStringExtra("id"));
                    Post post = posts.get(0);
                    populate(post);
                    // row of Object Id "U8mCwTHOaC"
                } else {
                    // error
                    Toast.makeText(PostDetailActivity.this, "error getting post!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}