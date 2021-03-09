package com.example.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity";

    protected CommentsAdapter adapter;
    Post post;
    String postId;
    List<Comment> comments;

    private RecyclerView rvComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        comments = new ArrayList<>();

        setContentView(R.layout.activity_detail);


        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        Log.d(TAG, "Post ID being used: " + postId);

        rvComments = findViewById(R.id.rvDetailedPost);

        getPost();

    }

    private void queryComments() {
        Log.i(TAG, "queryComments");
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);

        query.include(Comment.KEY_USER);
        query.include(Comment.KEY_POST);
        query.whereEqualTo(Comment.KEY_POST, post);
        query.addAscendingOrder(Comment.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> commentsFound, ParseException e) {
                Log.i(TAG, "queryComments, done");
                if (e != null) {
                    Log.e(TAG, "Problem getting comments", e);
                    return;
                }

                Log.d(TAG, "number of comments found: " + commentsFound.size());
                for (int i = 0; i < commentsFound.size(); i++) {
                    Comment comment = (Comment) commentsFound.get(i);
                    Log.i(TAG, "Comment: " + comment.getCommentText() + ", username: " + comment.getUser().getUsername());
                }

                for (Comment comment: commentsFound) {
                    Log.i(TAG, "Comment: " + comment.getCommentText() + ", username: " + comment.getUser().getUsername());
                }

                adapter.addAll(commentsFound);

            }
        });
    }

    private void getPost() {
        Log.i(TAG, "getPost");
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_ID, postId);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> postsFound, ParseException e) {
                Log.i(TAG, "getPost, done");
                if (e != null) {
                    Log.e(TAG, "Problem getting post", e);
                    return;
                }

                if (postsFound.size() != 1) {
                    Log.e(TAG, "Expected to find 1 post, instead found " + postsFound.size());
                } else {
                    post = postsFound.get(0);
//                    setItems();
                    setupRecyclerView();
                    queryComments();
                }

            }
        });
    }

    private void setupRecyclerView() {
        adapter = new CommentsAdapter(this, comments, post);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(layoutManager);
        rvComments.setAdapter(adapter);
    }
}