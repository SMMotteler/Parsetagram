package com.example.parsetagram;

import com.example.parsetagram.models.Comment;
import com.example.parsetagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("EtUmV66E9Uz5vKYFClJKv2IJHg0cODaH7zvuEhKN")
                .clientKey("eF1e0zM2zv6iLPEuZHE7PKpe5LIsk4LkQ7ALd5vS")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
