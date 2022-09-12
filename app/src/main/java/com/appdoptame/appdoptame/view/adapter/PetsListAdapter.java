package com.appdoptame.appdoptame.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.model.Pet;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PetsListAdapter extends RecyclerView.Adapter<PetsListAdapter.ViewHolder> {

    private List<Pet>            pets;
    private final LayoutInflater inflater;
    private final Context        context;

    public PetsListAdapter(Context context) {
        this.context  = context;
        this.inflater = LayoutInflater.from(context);
        this.pets     = new ArrayList<>();
    }

    public void setData(List<Pet> pets){
        this.pets = pets;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(this::notifyDataSetChanged);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_pets_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pet pet = pets.get(position);

        if(pet.getImages().size() > 0){
            Glide.with(AppDoptameApp.getContext())
                    .load(pet.getImages().get(0))
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.adapter_pets_list_image);
        }
    }
}