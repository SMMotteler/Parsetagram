package com.example.parsetagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    Button btLogout;
    EditText etDescription;
    Button btTakePhoto;
    ImageView ivPhoto;
    Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLogout = findViewById(R.id.btLogout);
        etDescription = findViewById(R.id.etDescription);
        btTakePhoto = findViewById(R.id.btTakePhoto);
        ivPhoto = findViewById(R.id.ivPhoto);
        btSubmit = findViewById(R.id.btSubmit);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginActivity();
            }
        });
        
        // queryPosts();
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(MainActivity.this, "Your post needs a description!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser);
            }
        });
    }

    private void savePost(String description, ParseUser currentUser) {
        Post post = new Post();
        post.setDescription(description);
        // post.setImage();
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!= null){
                    Log.e(TAG, "ERROR WHILE SAVING", e);
                    return;
                }
            }
        });
        // Log.i(TAG, "Save was successful!");
        etDescription.getText().clear();
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "issue with getting posts", e);
                    return;
                }
                for (Post post: posts){
                    Log.i(TAG, "Post: "+post.getDescription()+", user: "+post.getUser().getUsername());
                }
            }
        });
    }

    private void goLoginActivity() {
        ParseUser.logOutInBackground();
        ParseUser currentUser = ParseUser.getCurrentUser(); // currentUser will be null
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}