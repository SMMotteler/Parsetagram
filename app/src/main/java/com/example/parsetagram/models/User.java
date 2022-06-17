package com.example.parsetagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {

    public static final String KEY_PROFILE_PICTURE = "profilePic";

    public ParseFile getProfilePicture() {return (ParseFile) getParseFile(KEY_PROFILE_PICTURE);}

    public void setProfilePicture(ParseFile parseFile) {put(KEY_PROFILE_PICTURE, parseFile);}

}
