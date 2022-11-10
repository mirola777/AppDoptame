package com.appdoptame.appdoptame.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.ChatCreatorListener;
import com.appdoptame.appdoptame.data.listener.ChatEditorListener;
import com.appdoptame.appdoptame.data.listener.MessageInserterListener;
import com.appdoptame.appdoptame.data.observer.ChatObserver;
import com.appdoptame.appdoptame.data.observer.MessageObserver;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.UserNameGetter;
import com.appdoptame.appdoptame.view.fragmentcontroller.SetFragmentChat;
import com.bumptech.glide.Glide;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> implements ChatCreatorListener, ChatEditorListener, MessageInserterListener {

    private List<Chat> chats;
    private final LayoutInflater inflater;
    private final Context context;
    private User userSession;

    public ChatListAdapter(Context context) {
        this.context     = context;
        this.inflater    = LayoutInflater.from(context);
        this.chats       = new ArrayList<>();
        this.userSession = UserRepositoryFS.getInstance().getUserSession();

        ChatObserver.attachChatCreatorListener(this);
        ChatObserver.attachChatEditorListener(this);
        MessageObserver.attachMessageInserterListener(this);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat           = chats.get(position);
        User owner          = chat.getOwner();
        Pet  pet            = chat.getPet();
        User adopter        = chat.getAdopter();
        Message lastMessage = chat.getLastMessage();

        Glide.with(AppDoptameApp.getContext())
                .load(pet.getImages().get(0))
                .placeholder(R.drawable.user_icon_orange)
                .error(R.drawable.user_icon_orange)
                .into(holder.image);

        if(userSession.getID().equals(owner.getID())){
            holder.nameText.setText("[" + pet.getName() + "] " + UserNameGetter.get(adopter));

        } else if(userSession.getID().equals(adopter.getID())){
            holder.nameText.setText("[" + pet.getName() + "] " + UserNameGetter.get(owner));
        }

        if(lastMessage.getWriterID().equals(userSession.getID())){
            holder.messageText.setText(AppDoptameApp.getStringById(R.string.you) + ": " + lastMessage.getMessage());
        } else if(lastMessage.getWriterID().equals(owner.getID())) {
            holder.messageText.setText(owner.getName() + ": " + lastMessage.getMessage());
        } else if(lastMessage.getWriterID().equals(adopter.getID())){
            holder.messageText.setText(adopter.getName() + ": " + lastMessage.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public void onChatCreated(Chat chat) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            chats.add(0, chat);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> notifyItemInserted(0));
        });
    }

    @Override
    public void onFailure() {

    }

    public void onDetach(){
        ChatObserver.detachChatCreatorListener(this);
        ChatObserver.detachChatEditorListener(this);
        MessageObserver.detachMessageInserterListener(this);
    }

    @Override
    public void onNewMessage(Message message) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            int position = Iterables.indexOf(chats, input -> input.getID().equals(message.getChatID()));
            if(position == -1) return;

            chats.get(position).setLastMessage(message);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> notifyItemChanged(position));
        });
    }

    @Override
    public void onEdited(Chat chat) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            int position = Iterables.indexOf(chats, input -> input.getID().equals(chat.getID()));
            if(position == -1) return;
            chats.set(position, chat);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> notifyItemChanged(position));
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView        image;
        TextView         messageText;
        TextView         nameText;
        ConstraintLayout button;

        ViewHolder(View itemView) {
            super(itemView);
            image       = itemView.findViewById(R.id.adapter_chat_list_image);
            nameText    = itemView.findViewById(R.id.adapter_chat_list_name);
            messageText = itemView.findViewById(R.id.adapter_chat_list_message);
            button      = itemView.findViewById(R.id.adapter_chat_list_button);

            button.setOnClickListener(v -> {
                SetFragmentChat.set(chats.get(getAdapterPosition()));
            });
        }
    }
}