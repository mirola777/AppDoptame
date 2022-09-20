package com.appdoptame.appdoptame.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.UserNameGetter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private List<Chat> chats;
    private final LayoutInflater inflater;
    private final Context context;
    private User userSession;

    public ChatListAdapter(Context context) {
        this.context     = context;
        this.inflater    = LayoutInflater.from(context);
        this.chats       = new ArrayList<>();
        this.userSession = UserRepositoryFS.getInstance().getUserSession();
    }

    public void setData(List<Chat> chats){
        this.chats = chats;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(this::notifyDataSetChanged);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_chat_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat    = chats.get(position);
        User owner   = chat.getOwner();
        User adopter = chat.getAdopter();

        if(userSession.getID().equals(owner.getID())){

            holder.nameText.setText(UserNameGetter.get(adopter));
            Glide.with(AppDoptameApp.getContext())
                    .load(adopter.getImage())
                    .placeholder(R.drawable.user_icon_orange)
                    .error(R.drawable.user_icon_orange)
                    .into(holder.image);

        } else if(userSession.getID().equals(adopter.getID())){

            holder.nameText.setText(UserNameGetter.get(owner));
            Glide.with(AppDoptameApp.getContext())
                    .load(owner.getImage())
                    .placeholder(R.drawable.user_icon_orange)
                    .error(R.drawable.user_icon_orange)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView  messageText;
        TextView  nameText;

        ViewHolder(View itemView) {
            super(itemView);
            image       = itemView.findViewById(R.id.adapter_chat_list_image);
            nameText    = itemView.findViewById(R.id.adapter_chat_list_name);
            messageText = itemView.findViewById(R.id.adapter_chat_list_message);
        }
    }
}