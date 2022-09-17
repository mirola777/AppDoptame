package com.appdoptame.appdoptame.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.util.Selectable;

import java.util.List;

public class SelectableAdapter extends RecyclerView.Adapter<SelectableAdapter.ViewHolder> {

    private final List<Selectable> selectables;
    private final LayoutInflater inflater;

    public SelectableAdapter(Context context, List<Selectable> selectables) {
        this.inflater    = LayoutInflater.from(context);
        this.selectables = selectables;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_selectable, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Selectable selectable = selectables.get(position);

        holder.image.setImageResource(selectable.getDrawableID());
        holder.text.setText(selectable.getStringID());
    }

    @Override
    public int getItemCount() {
        return selectables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView        image;
        TextView         text;
        ConstraintLayout button;

        ViewHolder(View itemView) {
            super(itemView);
            text   = itemView.findViewById(R.id.adapter_selectable_text);
            image  = itemView.findViewById(R.id.adapter_selectable_image);
            button = itemView.findViewById(R.id.adapter_selectable_button);

            button.setOnClickListener(v -> selectables.get(getAdapterPosition()).onClick());
        }
    }
}