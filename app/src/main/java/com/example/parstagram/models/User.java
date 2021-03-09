package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseObject {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PROFILE_PICTURE = "profilePicture";
    public static final String KEY_ID = "objectId";


    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public void setPassword(String password) {
        put(KEY_PASSWORD, password);
    }

    public void setProfilePicture(ParseFile image) {
        put(KEY_PROFILE_PICTURE, image);
    }

    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public String getPassword() {
        return getString(KEY_PASSWORD);
    }

    public ParseFile getProfilePicture() {
        return getParseFile(KEY_PROFILE_PICTURE);
    }
}
