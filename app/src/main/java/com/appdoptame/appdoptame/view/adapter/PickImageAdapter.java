package com.appdoptame.appdoptame.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.listener.PickImageAdapterListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PickImageAdapter extends RecyclerView.Adapter<PickImageAdapter.ViewHolder> {

    private final List<byte[]>   images;
    private final LayoutInflater inflater;
    private final Context        context;
    private PickImageAdapterListener listener;
    private boolean addButton;

    public PickImageAdapter(Context context, PickImageAdapterListener listener, boolean addButton) {
        this(context, listener, new ArrayList<>(), addButton);
    }

    public PickImageAdapter(Context context, PickImageAdapterListener listener, List<byte[]> images, boolean addButton){
        this.context   = context;
        this.inflater  = LayoutInflater.from(context);
        this.images    = images;
        this.listener  = listener;
        this.addButton = addButton;

        if(addButton) {
            images.add(0, null);
        }
    }

    public List<byte[]> getImages(){
        if (!addButton) return images;
        return images.subList(1, images.size());
    }

    public void deleteImages(){
        images.clear();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(this::notifyDataSetChanged);
        listener.onImagesDeleted();
    }

    public void addImage(byte[] image){
        this.images.add(image);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> notifyItemInserted(images.size()-1));
        listener.onImageAdded(images.size() - 1);
    }

    private void deleteImage(int position){
        images.remove(position);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> notifyItemRemoved(position));
        listener.onImageDeleted(images.size() - 1);

    }

    public void addImages(List<byte[]> images){
        int initialPosition = this.images.size();
        this.images.addAll(images);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> notifyItemRangeInserted(initialPosition, images.size()));
        listener.onImagesAdded(this.images.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_pick_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        byte[] imageUri = images.get(position);

        if(position == 0 && imageUri == null){
            holder.add.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.GONE);
        } else {
            holder.add.setVisibility(View.GONE);
            holder.delete.setVisibility(View.VISIBLE);

            Glide.with(AppDoptameApp.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView   image;
        ImageButton add;
        ImageButton delete;

        ViewHolder(View itemView) {
            super(itemView);
            image  = itemView.findViewById(R.id.adapter_pick_image_image);
            add    = itemView.findViewById(R.id.adapter_pick_image_add);
            delete = itemView.findViewById(R.id.adapter_pick_image_delete);

            delete.setOnClickListener(v -> deleteImage(getAdapterPosition()));

            add.setOnClickListener(v -> listener.onAdd());
        }
    }
}