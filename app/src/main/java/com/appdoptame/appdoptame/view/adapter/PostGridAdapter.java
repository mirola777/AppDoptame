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
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.util.PetAgeGetter;

import java.util.ArrayList;
import java.util.List;

public class PostGridAdapter extends RecyclerView.Adapter<PostGridAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<Post> posts;
    private final Context context;

    public PostGridAdapter(Context context, List<Post> posts){
        this.inflater    = LayoutInflater.from(context);
        this.context     = context;
        this.posts       = posts;
    }

    public PostGridAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
        this.context  = context;
        this.posts    = new ArrayList<>();
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

            optionsButton.setOnClickListener(v -> {});

            imageAdapter = new PostImageAdapter(context);
            imageView.setAdapter(imageAdapter);
        }
    }
}