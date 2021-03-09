package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity";

    protected CommentsAdapter adapter;
    Post post;
    String postId;
    List<Comment> comments;

    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvTimestamp;
    private EditText etComment;
    private Button btnComment;
    private RecyclerView rvComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        comments = new ArrayList<>();

        setContentView(R.layout.activity_detail);

        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        etComment = findViewById(R.id.etComment);
        btnComment = findViewById(R.id.btnPostComment);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        Log.d(TAG, "Post ID being used: " + postId);
        getPost();

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnComment onClick");
                String commentText = etComment.getText().toString();
                postComment(commentText);
            }
        });

//        adapter = new CommentsAdapter(this, comments);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        rvComments.setLayoutManager(layoutManager);
//        rvComments.setAdapter(adapter);
    }

    private void queryComments() {
        Log.i(TAG, "queryComments");
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);

        query.include(Comment.KEY_USER);
        query.include(Comment.KEY_POST);
        query.whereEqualTo(Comment.KEY_POST, post);
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
                    setItems();
//                    queryComments();
                }

            }
        });
    }

    private void setItems() {
        Log.i(TAG, "setItems");
        tvUsername.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this)
                    .load(post.getImage().getUrl())
                    .into(ivImage);
        }

        tvDescription.setText(post.getDescription());
        tvTimestamp.setText(TimeFormatter.getTimeDifference(post.getCreatedAt().toString()));
    }

    private void postComment(String commentText) {
        Log.i(TAG, "postComment putting a comment on post with id: " + postId);

        Comment comment = new Comment();
        comment.setCommentText(commentText);
        comment.setUser(ParseUser.getCurrentUser());
        comment.setPost(post);

        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Context context = getApplicationContext();
                if (e != null) {
                    Log.e(TAG, "Error saving comment", e);
                    Toast.makeText(context, "Error saving post.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i(TAG, "Post saved successfully");
                Toast.makeText(context, "Comment posted!", Toast.LENGTH_SHORT).show();
                etComment.setText("");
            }
        });
    }

}