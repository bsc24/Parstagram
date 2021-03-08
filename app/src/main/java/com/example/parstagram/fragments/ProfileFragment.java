package com.example.parstagram.fragments;

import android.content.Context;
import android.util.Log;

import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    protected ParseUser user;

    public ProfileFragment() {
        super();
        user = ParseUser.getCurrentUser();
    }

    public ProfileFragment(ParseUser user) {
        super();
        this.user = user;
    }

    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> postsFound, ParseException e) {
                Log.i(TAG, "queryPosts, done");
                if (e != null) {
                    Log.e(TAG, "Problem getting posts", e);
                    return;
                }

                for(Post post: postsFound) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                posts.addAll(postsFound);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
