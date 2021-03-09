package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.parstagram.models.Post;
import com.example.parstagram.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "ProfileActivity";

    private RecyclerView rvProfile;
    protected ProfileAdapter adapter;

    //Context context;
    List<Post> posts;
    ParseUser user;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        posts = new ArrayList<>();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        rvProfile = findViewById(R.id.rvProfile);

        queryUsers(userId);

//        queryPosts();

    }

    protected void queryUsers(String userId) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);

        query.whereEqualTo(User.KEY_ID, userId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> usersFound, ParseException e) {
                Log.i(TAG, "queryUsers, done");
                if (e != null) {
                    Log.e(TAG, "Problem getting posts", e);
                    return;
                }

                if (usersFound.size() != 1) {
                    Log.e(TAG, "Expected to find 1 user, instead found " + usersFound.size());
                } else {
                    Log.i(TAG, "Found user: " + usersFound.get(0).getUsername());
                    user = usersFound.get(0);

                    setRecyclerView();
                    queryUserPosts();
//                    setItems();
//                    queryComments();
                }

            }
        });

    }

    protected void loadMoreData(Date createdAt) {
        Log.i(TAG, "loadMoreData is loading data older than date: " + createdAt.toString());
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.whereLessThan(Post.KEY_CREATED_AT, createdAt);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> postsFound, ParseException e) {
                Log.i(TAG, "loadMoreData, done");
                if (e != null) {
                    Log.e(TAG, "Problem getting posts", e);
                    return;
                }

                for(Post post: postsFound) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                adapter.addAll(postsFound);
                //swipeContainer.setRefreshing(false);
                //adapter.notifyDataSetChanged();
            }
        });
    }


    protected void queryUserPosts() {
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
//                setRecyclerView();
                //swipeContainer.setRefreshing(false);
            }
        });
    }

    private void setRecyclerView() {
        adapter = new ProfileAdapter(this, posts, user);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvProfile.setLayoutManager(layoutManager);
        rvProfile.setAdapter(adapter);
    }
}