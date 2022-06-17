package com.example.parsetagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parsetagram.R;
import com.example.parsetagram.models.BitmapScaler;
import com.example.parsetagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ComposeFragment extends BaseFragment {

    private static final int RESULT_OK = -1;
    private EditText etDescription;
    private Button btTakePhoto;
    private ImageView ivPhoto;
    private Button btSubmit;

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDescription = view.findViewById(R.id.etDescription);
        btTakePhoto = view.findViewById(R.id.btTakePhoto);
        ivPhoto = view.findViewById(R.id.ivPhoto);
        btSubmit = view.findViewById(R.id.btSubmit);

        btTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "photo button clicked!");
                launchCamera();
            }
        });

        // queryPosts();
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(getContext(), "Your post needs a description!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || ivPhoto.getDrawable()==null){
                    Toast.makeText(getContext(), "There is no image!",
                            Toast.LENGTH_SHORT).show();
                    return;

                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
            }
        });
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
                ivPhoto.setImageBitmap(BitmapFactory.decodeFile(resizedFile.getAbsolutePath()));
                Log.i(TAG, "bitmap size: "+BitmapFactory.decodeFile(resizedFile.getAbsolutePath()).getByteCount());
                Log.i(TAG, "original size: "+rawTakenImage.getByteCount());
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!= null){
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "ERROR WHILE SAVING", e);
                    return;
                }
                Toast.makeText(getContext(), "Save was successful", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Save was successful!");
                etDescription.getText().clear();
                ivPhoto.setImageResource(0);
            }
        });
    }

}