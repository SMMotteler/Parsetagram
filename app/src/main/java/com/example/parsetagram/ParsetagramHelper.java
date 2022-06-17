package com.example.parsetagram;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.parsetagram.models.Post;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class ParsetagramHelper {

    public static void clickLike(Post post, ImageButton ibLike){
        if (post.isLikedByCurrentUser()){
            // unlike
            post.unlike();
            ibLike.setBackgroundResource(R.drawable.ufi_heart);
        }
        else{
            // like
            post.like();
            ibLike.setBackgroundResource(R.drawable.ufi_heart_active);

        }
    }

    public static void clickComment(Activity activity, Post post){
        Intent i = new Intent(activity, ComposeCommentActivity.class);
        i.putExtra("post", post);
        activity.startActivity(i);

    }

    public static void goMainActivity(Activity activity) {
        Toast.makeText(activity.getApplicationContext(), "Logged in!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    public static void loginUser(String username, String password, Activity activity) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    // TODO: better error handling
                    Toast.makeText(activity, "Issue with login :(", Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity(activity);
            }
        });
    }

    public static void checkLikes(Post post, ImageButton ibLike){
        if (post.isLikedByCurrentUser()){
            ibLike.setBackgroundResource(R.drawable.ufi_heart_active);
        } else{
            ibLike.setBackgroundResource(R.drawable.ufi_heart);

        }

    }

    public static String postPhoto(Post post){
        ParseFile image = post.getImage();
        if (image == null){
            return "android.resource://com.example.parsetagram/"+R.drawable.placeholder;
        }
        return image.getUrl();
    }

    public static String profilePic(ParseUser user){
        ParseFile image = (ParseFile)user.get("profilePic");
        if (image == null){
            return "android.resource://com.example.parsetagram/"+R.drawable.default_pic;
        }
        return image.getUrl();
    }


}


