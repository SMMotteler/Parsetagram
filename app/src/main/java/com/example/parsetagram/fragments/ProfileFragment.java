package com.example.parsetagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parsetagram.LoginActivity;
import com.example.parsetagram.MainActivity;
import com.example.parsetagram.ParsetagramHelper;
import com.example.parsetagram.R;
import com.example.parsetagram.adapters.PostsAdapter;
import com.example.parsetagram.models.BitmapScaler;
import com.example.parsetagram.models.Post;
import com.example.parsetagram.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends BaseFragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    TextView tvUsername;
    ImageView ivProfilePhoto;
    RecyclerView rvFilteredFeed;
    PostsAdapter adapter;
    List<Post> userPosts;
    Button btLogout;

    public String photoFileName = "photo.jpg";


    public User user = (User)(User.getCurrentUser());

    public ProfileFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btLogout = view.findViewById(R.id.btLogout);
        tvUsername = view.findViewById(R.id.tvUsername);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);
        rvFilteredFeed = view.findViewById(R.id.rvFilteredFeed);
        userPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), userPosts);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginActivity();
            }
        });


        ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take new photo
                launchCamera();
            }
        });

        user.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                user = (User) object;
                displayUserInfo();
            }
        });

        if(user.hasSameId(ParseUser.getCurrentUser())){
            btLogout.setVisibility(View.VISIBLE);
        }
        else{
            btLogout.setVisibility(View.GONE);
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvFilteredFeed.setAdapter(adapter);
        rvFilteredFeed.setLayoutManager(linearLayoutManager);

        queryPosts(user);
    }

    public void displayUserInfo(){
        tvUsername.setText(user.getUsername());
        ParseFile profilePhoto = user.getProfilePicture();
        if (profilePhoto != null){
            Glide.with(getContext()).load(user.getProfilePicture().getUrl()).transform(new CircleCrop()).into(ivProfilePhoto);
        }
        else{
            Toast.makeText(getContext(), "nopic", Toast.LENGTH_SHORT).show();
            Glide.with(getContext()).load(ParsetagramHelper.profilePic(user)).transform(new CircleCrop()).into(ivProfilePhoto);

        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                // Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // resize the photo
                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
// by this point we have the camera photo on disk
                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
// See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
                Bitmap resizedBitmap = rawTakenImage;
                // while the size of the bitmap is larger than 10 MB, shrink it to 1/4 of its current size
                // - once it is smaller than 10 MB, it is acceptable to be saved to the imageview
                while (resizedBitmap.getByteCount() >= 1000000 * 10) {
                    resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, resizedBitmap.getWidth()/2);
                }
// Configure byte output stream
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
// Compress the image further
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
// Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
                File resizedFile = getPhotoFileUri(photoFileName + "_resized");
                try {
                    resizedFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(resizedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
// Write the bytes of the bitmap to file
                try {
                    fos.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Load the resized image into a preview
                Bitmap takenImage = BitmapFactory.decodeFile(resizedFile.getAbsolutePath());
                Glide.with(getContext()).load(takenImage).circleCrop().into(ivProfilePhoto);
                user.setProfilePicture(new ParseFile(resizedFile));
                user.saveInBackground();
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void queryPosts(User user) {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        query.include(Post.KEY_LIKED_BY);
        // make sure only posts by this user are made
        query.whereEqualTo(Post.KEY_USER, user);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                userPosts.clear();
                userPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void goLoginActivity() {
        MainActivity activity = (MainActivity)getContext();
        activity.performLogout();
    }

}