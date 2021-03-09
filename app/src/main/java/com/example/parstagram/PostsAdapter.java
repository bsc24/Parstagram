package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG = "PostsAdapter";

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llPost;
        private TextView tvUsername;
        private ImageView ivImage;
        private Button btnLike;
        private Button btnComment;
        private Button btnShare;
        private Button btnFavorite;
        private TextView tvDescription;
        private TextView tvTimestamp;

        boolean liked, favorited;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llPost = itemView.findViewById(R.id.llPost);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            btnShare = itemView.findViewById(R.id.btnShare);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);

            liked = false;
            favorited = false;
        }

        public void bind(Post post) {
            llPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Container onClick");
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("postId", post.getObjectId());
                    context.startActivity(i);
                }
            });

            tvUsername.setText(post.getUser().getUsername());

            /*
            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "action_profile: " + tvUsername.getText());
                    Fragment fragment = new ProfileFragment(post.getUser());

                }
            });
             */

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

        }
    }
}
