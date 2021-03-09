package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_COMMENT_TEXT = "commentText";
    public static final String KEY_POST = "post";
    public static final String KEY_USER = "user";


    public void setCommentText(String commentText) {
        put(KEY_COMMENT_TEXT, commentText);
    }

    public void setPost(Post post) {
        put(KEY_POST, post);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }


    public String getCommentText() {
        return getString(KEY_COMMENT_TEXT);
    }

    public ParseObject getPost() {
        return getParseObject(KEY_POST);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

}
