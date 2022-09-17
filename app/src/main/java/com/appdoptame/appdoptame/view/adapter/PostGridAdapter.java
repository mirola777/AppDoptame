package com.appdoptame.appdoptame.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.listener.PostDeleterListener;
import com.appdoptame.appdoptame.data.listener.PostInserterListener;
import com.appdoptame.appdoptame.data.observer.PostObserver;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.util.PetAgeGetter;
import com.appdoptame.appdoptame.view.dialog.DialogPostSettings;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PostGridAdapter extends RecyclerView.Adapter<PostGridAdapter.ViewHolder> implements PostDeleterListener, PostInserterListener {

    private final LayoutInflater inflater;
    private List<Post> posts;
    private final Context context;

    public PostGridAdapter(Context context, List<Post> posts){
        this.inflater    = LayoutInflater.from(context);
        this.context     = context;
        this.posts       = posts;

        PostObserver.attachPostDeleterListener(this);
        PostObserver.attachPostInserterListener(this);
    }

    public PostGridAdapter(Context context){
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
        View view = inflater.inflate(R.layout.adapter_post_grid, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n") @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.petBreed.setText(post.getPet().getBreed());
        holder.petAge.setText(PetAgeGetter.get(post.getPet()));
        holder.petName.setText(post.getPet().getName());
        holder.imageAdapter.setData(post.getPet().getImages());
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
        TextView         petName;
        TextView         petAge;
        TextView         petBreed;
        ViewPager2       imageView;
        ImageButton      optionsButton;
        PostImageAdapter imageAdapter;

        ViewHolder(View itemView) {
            super(itemView);

            imageView     = itemView.findViewById(R.id.adapter_post_grid_image_view);
            petName       = itemView.findViewById(R.id.adapter_post_grid_pet_name);
            petAge        = itemView.findViewById(R.id.adapter_post_grid_pet_age);
            petBreed      = itemView.findViewById(R.id.adapter_post_grid_pet_breed);
            optionsButton = itemView.findViewById(R.id.adapter_post_grid_options);

            optionsButton.setOnClickListener(v -> {
                Post post = posts.get(getAdapterPosition());
                DialogPostSettings dialog = new DialogPostSettings(post);
                FragmentController.showDialog(dialog);
            });

            imageAdapter = new PostImageAdapter(context);
            imageView.setAdapter(imageAdapter);
        }
    }
}