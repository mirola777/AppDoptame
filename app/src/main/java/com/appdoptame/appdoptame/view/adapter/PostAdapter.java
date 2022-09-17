package com.appdoptame.appdoptame.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.PetRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.PostRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LikeListener;
import com.appdoptame.appdoptame.data.listener.PostDeleterListener;
import com.appdoptame.appdoptame.data.listener.PostInserterListener;
import com.appdoptame.appdoptame.data.observer.PostObserver;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.DateTextGetter;
import com.appdoptame.appdoptame.util.PetAgeGetter;
import com.appdoptame.appdoptame.util.UserNameGetter;
import com.appdoptame.appdoptame.view.dialog.DialogPostSettings;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.bumptech.glide.Glide;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements PostDeleterListener, PostInserterListener {

    private final LayoutInflater inflater;
    private List<Post>           posts;
    private final Context        context;
    private final User           userSession;

    public PostAdapter(Context context, List<Post> posts){
        this.inflater    = LayoutInflater.from(context);
        this.context     = context;
        this.posts       = posts;
        this.userSession = UserRepositoryFS.getInstance().getUserSession();

        PostObserver.attachPostDeleterListener(this);
        PostObserver.attachPostInserterListener(this);
    }

    public PostAdapter(Context context){
        this(context, new ArrayList<>());
    }

    public void onDetach(){
        PostObserver.detachPostDeleterListener(this);
        PostObserver.detachPostInserterListener(this);
    }

    public void setPosts(List<Post> posts){
        this.posts = posts;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(this::notifyDataSetChanged);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_post, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n") @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.petBreed.setText(post.getPet().getBreed());
        holder.petAge.setText(PetAgeGetter.get(post.getPet()));
        holder.petName.setText(post.getPet().getName());
        holder.userName.setText(UserNameGetter.get(post.getUser()));
        holder.time.setText(
                        DateTextGetter.getDateText(post.getDate()) +
                        " ⚬ " +
                        post.getPet().getCity() +
                        ", " +
                        post.getPet().getDepartment());
        holder.description.setText(post.getPet().getDescription());
        holder.imageAdapter.setData(post.getPet().getImages());
        holder.likeCount.setText(String.valueOf(post.getLikes().size()));
        holder.commentCount.setText(String.valueOf(post.getComments().size()));

        if(post.getLikes().contains(userSession.getID())){
            holder.likeImage.setImageResource(R.drawable.ic_like_selected);
            holder.likeButton.setBackgroundColor(AppDoptameApp.getColorById(R.color.blue));
        } else {
            holder.likeImage.setImageResource(R.drawable.ic_like);
            holder.likeButton.setBackgroundColor(AppDoptameApp.getColorById(R.color.orange));
        }

        holder.userImage.setImageBitmap(null);
        Glide.with(AppDoptameApp.getContext())
                .load(post.getUser().getImage())
                .placeholder(R.drawable.user_icon_orange)
                .error(R.drawable.user_icon_orange)
                .into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onDeleted(String postID) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            int position = Iterables.indexOf(posts, input -> input.getID().equals(postID));
            if(position != -1) {
                posts.remove(position);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    notifyItemRemoved(position);
                });
            }
        });
    }

    @Override
    public void onInserted(Post post) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            posts.add(0, post);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                notifyItemInserted(0);
            });
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView         userName;
        TextView         time;
        TextView         description;
        TextView         petName;
        TextView         petAge;
        TextView         petBreed;
        LinearLayout     adoptButton;
        LinearLayout     likeButton;
        TextView         likeCount;
        ImageView        likeImage;
        LinearLayout     commentButton;
        TextView         commentCount;
        LinearLayout     shareButton;
        TextView         shareCount;
        CircleImageView  userImage;
        ViewPager2       imageView;
        ImageButton      optionsButton;
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
            likeImage     = itemView.findViewById(R.id.post_item_like_image);
            petName       = itemView.findViewById(R.id.post_item_pet_name);
            petAge        = itemView.findViewById(R.id.post_item_pet_age);
            petBreed      = itemView.findViewById(R.id.post_item_pet_breed);
            optionsButton = itemView.findViewById(R.id.post_item_options);

            optionsButton.setOnClickListener(v -> {
                Post post = posts.get(getAdapterPosition());
                DialogPostSettings dialog = new DialogPostSettings(post);
                FragmentController.showDialog(dialog);
            });

            imageAdapter = new PostImageAdapter(context);
            imageView.setAdapter(imageAdapter);
            /*
            imageView.setPageTransformer(new ViewPager2.PageTransformer() {
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


             */
            adoptButton.setOnClickListener(v -> {
                PetRepositoryFS.getInstance().changeState(posts.get(getAdapterPosition()).getPet(), new CompleteListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("aaaa");
                    }

                    @Override
                    public void onFailure() {
                        System.out.println("aaaddda");
                    }
                });
            });


            likeButton.setOnClickListener(v -> {
                Post post = posts.get(getAdapterPosition());
                PostRepositoryFS.getInstance().like(post, new LikeListener() {
                    @Override
                    public void onLike() {
                        likeCount.setText(String.valueOf(post.getLikes().size()));
                        likeButton.setBackgroundColor(AppDoptameApp.getContext().getResources().getColor(R.color.blue));
                        likeImage.setImageResource(R.drawable.ic_like_selected);
                    }

                    @Override
                    public void onDislike() {
                        likeCount.setText(String.valueOf(post.getLikes().size()));
                        likeButton.setBackgroundColor(AppDoptameApp.getContext().getResources().getColor(R.color.orange));
                        likeImage.setImageResource(R.drawable.ic_like);
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