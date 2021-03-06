package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final String TAG = "CommentsAdapter";
    private static final int POST = 0, COMMENT = 1;

    private Context context;
    private List<Comment> comments;
    private Post post;

    public CommentsAdapter(Context context, List<Comment> comments, Post post) {
        this.context = context;
        this.comments = comments;
        this.post = post;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == POST) {
            View v1 = inflater.inflate(R.layout.item_detailed_post, parent, false);
            viewHolder = new DetailedPostViewHolder(v1);
        } else {    // if (viewType == COMMENT)
            View v2 = inflater.inflate(R.layout.item_comment, parent, false);
            viewHolder = new CommentViewHolder(v2);
        }

//        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
//        return new CommentsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            DetailedPostViewHolder dpvh = (DetailedPostViewHolder) holder;
            dpvh.bind(post);
        } else {
            CommentViewHolder cvh = (CommentViewHolder) holder;
            cvh.bind(comments.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return comments.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return POST;
        } else {
            return COMMENT;
        }
    }

    public void addAll(List<Comment> list) {
        comments.addAll(list);
        notifyDataSetChanged();
    }


    private class DetailedPostViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llUser;
        private final ImageView ivProfilePic;
        private final TextView tvUsername;
        private final ImageView ivImage;
        private final Button btnLike;
        private final Button btnComment;
        private final Button btnShare;
        private final Button btnFavorite;
        private final TextView tvDescription;
        private final TextView tvTimestamp;
        private final EditText etComment;
        private final Button btnPostComment;

        boolean liked, favorited;

        public DetailedPostViewHolder(@NonNull View itemView) {
            super(itemView);
            llUser = itemView.findViewById(R.id.llUser);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            btnShare = itemView.findViewById(R.id.btnShare);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            etComment = itemView.findViewById(R.id.etComment);
            btnPostComment = itemView.findViewById(R.id.btnPostComment);

            liked = false;
            favorited = false;
        }

        public void bind(Post post) {
            llUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "action_profile: " + tvUsername.getText());
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("userId", post.getUser().getObjectId());
                    context.startActivity(i);

                }
            });

            ParseFile profilePic = post.getUser().getParseFile("profilePicture");
            if (profilePic != null) {
                Glide.with(context)
                        .load(profilePic.getUrl())
                        .transform(new RoundedCorners(300))
                        .into(ivProfilePic);
            }

            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(post.getImage().getUrl())
                        .into(ivImage);
            }

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "btnLike onClick");
                    liked = !liked;

                    if (liked)
                        btnLike.setBackgroundResource(R.drawable.ufi_heart_active);
                    else
                        btnLike.setBackgroundResource(R.drawable.ufi_heart_icon);
                }
            });

            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "btnFavorite onClick");
                    favorited = !favorited;

                    if (favorited)
                        btnFavorite.setBackgroundResource(R.drawable.ufi_save_active);
                    else
                        btnFavorite.setBackgroundResource(R.drawable.ufi_save_icon);
                }
            });

            tvDescription.setText(post.getDescription());
            tvTimestamp.setText(TimeFormatter.getTimeDifference(post.getCreatedAt().toString()));

            btnPostComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "btnComment onClick");
                    String commentText = etComment.getText().toString();
                    postComment(commentText);
                }
            });
        }

        private void postComment(String commentText) {
            Log.i(TAG, "postComment putting a comment on post with id: " + post.getObjectId());

            Comment comment = new Comment();
            comment.setCommentText(commentText);
            comment.setUser(ParseUser.getCurrentUser());
            comment.setPost(post);

            comment.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Error saving comment", e);
                        Toast.makeText(context, "Error saving post.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.i(TAG, "Post saved successfully");
                    Toast.makeText(context, "Comment posted!", Toast.LENGTH_SHORT).show();
                    etComment.setText("");
                    comments.add(comment);
                    notifyDataSetChanged();
                }
            });
        }
    }


    private class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfilePic;
        TextView tvUsername;
        TextView tvComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvComment = itemView.findViewById(R.id.tvComment);
        }

        public void bind(Comment comment) {
            ParseFile profilePic = comment.getUser().getParseFile("profilePicture");
            if (profilePic != null) {
                ivProfilePic.setBackground(null);
                Glide.with(context)
                        .load(profilePic.getUrl())
                        .transform(new RoundedCorners(300))
                        .into(ivProfilePic);
            }

            ivProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "action_profile: " + tvUsername.getText());
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("userId", comment.getUser().getObjectId());
                    context.startActivity(i);

                }
            });

            tvUsername.setText(comment.getUser().getUsername());
            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "action_profile: " + tvUsername.getText());
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("userId", comment.getUser().getObjectId());
                    context.startActivity(i);

                }
            });

            tvComment.setText(comment.getCommentText());
        }
    }

}
