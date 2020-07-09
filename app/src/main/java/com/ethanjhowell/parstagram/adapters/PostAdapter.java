package com.ethanjhowell.parstagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.ethanjhowell.parstagram.R;
import com.ethanjhowell.parstagram.models.Post;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostAdapter extends PagedListAdapter<Post, PostAdapter.ViewHolder> {
    private static final String TAG = PostAdapter.class.getCanonicalName();
    public static final DiffUtil.ItemCallback<Post> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Post>() {
                @Override
                public boolean areItemsTheSame(Post oldItem, Post newItem) {
                    return oldItem.getObjectId().equals(newItem.getObjectId());
                }

                @Override
                public boolean areContentsTheSame(@NotNull Post oldItem, @NotNull Post newItem) {
                    return areItemsTheSame(oldItem, newItem);
                }
            };
    private Context context;
    private List<Post> posts;

    public PostAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        posts = new ArrayList<>();
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View postView = inflater.inflate(R.layout.item_post, parent, false);

        // Return a new holder instance
        return new ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(Objects.requireNonNull(getItem(position)));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvCaption;
        private TextView tvUsernameDetail;
        private TextView tvUsernameHero;
        private TextView tvRelativeTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvUsernameDetail = itemView.findViewById(R.id.tvUsernameDetail);
            tvUsernameHero = itemView.findViewById(R.id.tvUsernameHero);
            tvRelativeTime = itemView.findViewById(R.id.tvRelativeTime);
        }

        public void bind(Post post) {
            Log.d(TAG, "bind: binding post " + post.getObjectId() + " " + post.getRelativeCreatedAt());
            tvUsernameHero.setText(post.getUser().getUsername());
            tvUsernameDetail.setText(post.getUser().getUsername());

            tvCaption.setText(post.getCaption());

            tvRelativeTime.setText(post.getRelativeCreatedAt());

            Glide.with(context)
                    .load(post.getImage().getUrl())
                    .transform(new CenterInside())
                    .into(ivImage);

            ivImage.setOnClickListener(view -> Toast.makeText(context, post.getCreatedAt().toString(), Toast.LENGTH_SHORT).show());
        }

    }
}
