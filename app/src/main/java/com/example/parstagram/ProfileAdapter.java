package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parstagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "ProfileAdapter";
    private static final int USER = 0, POST = 1;

    private Context context;
    private List<Post> posts;
    private ParseUser user;

    public ProfileAdapter(Context context, List<Post> posts, ParseUser user) {
        this.context = context;
        this.posts = posts;
        this.user = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == USER) {
            viewHolder = new UserViewHolder(inflater.inflate(R.layout.item_user, parent, false));
        } else {
            viewHolder = new PostViewHolder(inflater.inflate(R.layout.item_profile_post, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ProfileAdapter.UserViewHolder uvh = (ProfileAdapter.UserViewHolder) holder;
            uvh.bind(user);
        } else {
            ProfileAdapter.PostViewHolder pvh = (ProfileAdapter.PostViewHolder) holder;
            pvh.bind(posts.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return posts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return USER;
        } else {
            return POST;
        }
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProfilePic;
        private TextView tvUsername;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }

        public void bind(ParseUser user) {
            ParseFile image = user.getParseFile("profilePicture");
            if (image != null) {
                ivProfilePic.setBackground(null);
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivProfilePic);
            }
            tvUsername.setText(user.getUsername());
        }
    }

    private class PostViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPostImage;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
        }

        public void bind(Post post) {
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(post.getImage().getUrl())
                        .into(ivPostImage);
            }

            ivPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "ivPostImage onClick");
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("postId", post.getObjectId());
                    context.startActivity(i);
                }
            });
        }
    }
}
