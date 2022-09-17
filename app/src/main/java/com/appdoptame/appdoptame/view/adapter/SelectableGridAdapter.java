package com.appdoptame.appdoptame.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.util.Selectable;

import java.util.List;

public class SelectableGridAdapter extends RecyclerView.Adapter<SelectableGridAdapter.ViewHolder> {

    private final List<Selectable> selectables;
    private final LayoutInflater inflater;

    public SelectableGridAdapter(Context context, List<Selectable> selectables) {
        this.inflater    = LayoutInflater.from(context);
        this.selectables = selectables;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_selectable_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Selectable selectable = selectables.get(position);

        holder.image.setImageResource(selectable.getDrawableID());
    }

    @Override
    public int getItemCount() {
        return selectables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ConstraintLayout button;

        ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.adapter_selectable_button);
            image  = itemView.findViewById(R.id.adapter_selectable_image);

            button.setOnClickListener(v -> selectables.get(getAdapterPosition()).onClick());
        }
    }
}