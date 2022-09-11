package com.appdoptame.appdoptame.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.PostRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.LikeListener;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.DateTextGetter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<Post>           posts;
    private final Context        context;
    private final User           userSession;

    public PostAdapter(Context context, List<Post> posts){
        this.inflater    = LayoutInflater.from(context);
        this.context     = context;
        this.posts       = posts;
        this.userSession = UserRepositoryFS.getInstance().getUserSession();
    }

    public PostAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
        this.context  = context;
        this.posts    = new ArrayList<>();
        this.userSession = UserRepositoryFS.getInstance().getUserSession();
    }

    public void setPosts(List<Post> posts){
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n") @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.userName.setText(post.getUser().getName());
        holder.time.setText(
                        DateTextGetter.getDateText(post.getDate()) +
                        " ● " +
                        post.getPet().getCity() +
                        ", " +
                        post.getPet().getDepartment());
        holder.description.setText(post.getPet().getDescription());
        if(holder.imageAdapter == null){
            holder.imageAdapter = new PostImageAdapter(context, post.getPet().getImages());
            holder.imageView.setAdapter(holder.imageAdapter);
            holder.imageView.setPageTransformer(new ViewPager2.PageTransformer() {
                private static final float MIN_SCALE = 0.85f;
                private static final float MIN_ALPHA = 0.6f;

                @Override
                public void transformPage(@NonNull View page, float position) {
                    if (position <-1) page.setAlpha(0);
                    else if (position <=1){ // [-1,1]
                        page.setScaleX(Math.max(MIN_SCALE,1-Math.abs(position)));
                        page.setScaleY(Math.max(MIN_SCALE,1-Math.abs(position)));
                        page.setAlpha(Math.max(MIN_ALPHA,1-Math.abs(position)));
                    }
                    else page.setAlpha(0);
                }
            });
        }
        holder.likeCount.setText(String.valueOf(post.getLikes().size()));
        holder.commentCount.setText(String.valueOf(post.getComments().size()));

        if(post.getLikes().contains(userSession.getID())){
            holder.likeButton.setBackgroundColor(AppDoptameApp.getColorById(R.color.blue));
        } else {
            holder.likeButton.setBackgroundColor(AppDoptameApp.getColorById(R.color.orange));
        }

        holder.userImage.setImageBitmap(null);
        Glide.with(AppDoptameApp.getContext())
                .load(post.getUser().getImage())
                .placeholder(R.drawable.user_icon_orange)
                .error(R.drawable.user_icon_orange)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView         userName;
        TextView         time;
        TextView         description;
        TextView         adoptButton;
        LinearLayout     likeButton;
        TextView         likeCount;
        LinearLayout     commentButton;
        TextView         commentCount;
        LinearLayout     shareButton;
        TextView         shareCount;
        CircleImageView  userImage;
        ViewPager2       imageView;
        PostImageAdapter imageAdapter;

        ViewHolder(View itemView) {
            super(itemView);

            userName      = itemView.findViewById(R.id.post_item_user_name);
            time          = itemView.findViewById(R.id.post_item_time);
            description   = itemView.findViewById(R.id.post_item_description);
            adoptButton   = itemView.findViewById(R.id.post_item_adopt_button);
            likeButton    = itemView.findViewById(R.id.post_item_like_button);
            likeCount     = itemView.findViewById(R.id.post_item_like_count);
            commentButton = itemView.findViewById(R.id.post_item_comment_button);
            commentCount  = itemView.findViewById(R.id.post_item_comment_count);
            shareButton   = itemView.findViewById(R.id.post_item_share_button);
            shareCount    = itemView.findViewById(R.id.post_item_share_count);
            userImage     = itemView.findViewById(R.id.post_item_user_image);
            imageView     = itemView.findViewById(R.id.post_item_image_view);

            likeButton.setOnClickListener(v -> {
                Post post = posts.get(getAdapterPosition());
                PostRepositoryFS.getInstance().like(post, new LikeListener() {
                    @Override
                    public void onLike() {
                        likeCount.setText(String.valueOf(post.getLikes().size()));
                        likeButton.setBackgroundColor(AppDoptameApp.getContext().getResources().getColor(R.color.blue));
                    }

                    @Override
                    public void onDislike() {
                        likeCount.setText(String.valueOf(post.getLikes().size()));
                        likeButton.setBackgroundColor(AppDoptameApp.getContext().getResources().getColor(R.color.orange));
                    }

                    @Override
                    public void onFailure() {
                        likeCount.setText(String.valueOf(post.getLikes().size()));
                        if(post.getLikes().contains(userSession.getID())){
                            likeButton.setBackgroundColor(AppDoptameApp.getColorById(R.color.blue));
                        } else {
                            likeButton.setBackgroundColor(AppDoptameApp.getColorById(R.color.orange));
                        }
                    }
                });
            });
        }
    }
}