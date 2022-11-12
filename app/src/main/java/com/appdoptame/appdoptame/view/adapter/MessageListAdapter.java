package com.appdoptame.appdoptame.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.MessageInserterListener;
import com.appdoptame.appdoptame.data.observer.MessageObserver;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.model.message.MessageAdopt;
import com.appdoptame.appdoptame.model.message.MessageFile;
import com.appdoptame.appdoptame.model.message.MessageImage;
import com.appdoptame.appdoptame.util.DateTextGetter;
import com.appdoptame.appdoptame.util.MessageConstants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> implements MessageInserterListener {
    private final static int LEFT_NORMAL    = 0;
    private final static int RIGHT_NORMAL   = 1;
    private final static int RIGHT_IMAGE    = 2;
    private final static int LEFT_IMAGE     = 3;
    private final static int RIGHT_FILE     = 4;
    private final static int LEFT_FILE      = 5;
    private final static int LARGE          = 6;

    private List<Message> messages;
    private Chat          chat;
    private User          userSession;

    private final LayoutInflater inflater;
    private final Context        context;
    private final RecyclerView   messagesRecyclerView;
    private final TimeUnit       timeUnit ;

    public MessageListAdapter(Context context, Chat chat, RecyclerView messagesRecyclerView) {
        this.context              = context;
        this.inflater             = LayoutInflater.from(context);
        this.messages             = new ArrayList<>();
        this.chat                 = chat;
        this.messagesRecyclerView = messagesRecyclerView;
        this.userSession          = UserRepositoryFS.getInstance().getUserSession();
        this.timeUnit             = TimeUnit.SECONDS;

        MessageObserver.attachMessageInserterListener(this);
    }

    public void onDetach(){
        MessageObserver.detachMessageInserterListener(this);
    }

    public void setData(List<Message> messages){
        this.messages = messages;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(this::notifyDataSetChanged);
    }

    public void addMessage(Message message){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            messages.add(message);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> notifyItemInserted(messages.size() - 1));
        });
    }

    public Message getLastMessage(){
        if(messages.size() > 0) return messages.get(messages.size() - 1);

        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case RIGHT_IMAGE:     return new ViewHolder(inflater.inflate(R.layout.adapter_message_image_list_right, parent, false));
            case RIGHT_NORMAL:    return new ViewHolder(inflater.inflate(R.layout.adapter_message_list_right, parent, false));
            case LEFT_IMAGE:      return new ViewHolder(inflater.inflate(R.layout.adapter_message_image_list_left, parent, false));
            case LEFT_NORMAL:     return new ViewHolder(inflater.inflate(R.layout.adapter_message_list_left, parent, false));
            case LEFT_FILE:       return new ViewHolder(inflater.inflate(R.layout.adapter_message_file_list_left, parent, false));
            case RIGHT_FILE:      return new ViewHolder(inflater.inflate(R.layout.adapter_message_file_right, parent, false));
            case LARGE:           return new ViewHolder(inflater.inflate(R.layout.adapter_message_large_list, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position) instanceof MessageAdopt){
            return LARGE;
        }

        if(messages.get(position).getWriterID().equals(userSession.getID())){
            if(messages.get(position) instanceof MessageImage) {
                return RIGHT_IMAGE;
            } else if(messages.get(position) instanceof MessageFile) {
                return RIGHT_FILE;
            } else {
                return RIGHT_NORMAL;
            }
        } else {
            if(messages.get(position) instanceof MessageImage) {
                return LEFT_IMAGE;
            } else if(messages.get(position) instanceof MessageFile) {
                return LEFT_FILE;
            } else {
                return LEFT_NORMAL;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        if(message instanceof MessageImage) {
            Glide.with(AppDoptameApp.getContext())
                    .load(((MessageImage)message).getImage())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(holder.image);
        } else if(message instanceof MessageFile) {
            holder.fileText.setText(((MessageFile)message).getFilename());

        } else if(message instanceof MessageAdopt) {

            switch (message.getMessage()) {
                case MessageConstants.ADOPT_YES:
                    holder.largeText.setText(R.string.adopt_yes);
                    break;
                case MessageConstants.ADOPT_NO:
                    holder.largeText.setText(R.string.adopt_no);
                    break;
                case MessageConstants.ADOPT_TIMEOUT:
                    holder.largeText.setText(R.string.adopt_timeout);
                    break;
                case MessageConstants.ADOPT_CANCEL:
                    holder.largeText.setText(R.string.adopt_cancel);
                    break;
                case MessageConstants.ADOPT_START:
                default:
                    holder.largeText.setText(R.string.adopt_start);
                    break;
            }

        } else {
            holder.messageText.setText(message.getMessage());
        }

        holder.dateText.setText(DateTextGetter.getDateTextForChat(message.getDate()));
        if(position < messages.size() - 1){
            Message messageAfter         = messages.get(position + 1);
            long dateDifference          = messageAfter.getDate().getTime() - message.getDate().getTime();
            long dateDifferenceInMinutes = Math.abs(timeUnit.convert(dateDifference, TimeUnit.MILLISECONDS));

            if(dateDifferenceInMinutes <= 60 && messageAfter.getWriterID().equals(message.getWriterID())){
                holder.dateText.setVisibility(View.GONE);
            } else {
                holder.dateText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onNewMessage(Message message) {
        Message lastMessage = messages.get(messages.size() - 1);

        if(!lastMessage.getID().equals(message.getID()) && message.getChatID().equals(chat.getID())){
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                messages.add(message);
                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return 350f / displayMetrics.densityDpi;
                    }
                };

                smoothScroller.setTargetPosition(messages.size() - 1);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    notifyItemInserted(messages.size() - 1);
                    try {
                        messagesRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
                    } catch (Exception ignored){

                    }
                });
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView fileText;
        ConstraintLayout fileButton;
        TextView dateText;
        TextView largeText;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            dateText    = itemView.findViewById(R.id.adapter_message_list_date);
            messageText = itemView.findViewById(R.id.adapter_message_list_message);
            image       = itemView.findViewById(R.id.adapter_message_list_image);
            fileText    = itemView.findViewById(R.id.adapter_message_list_file_name);
            fileButton  = itemView.findViewById(R.id.adapter_message_list_file_button);
            largeText   = itemView.findViewById(R.id.adapter_message_large);

            if(fileButton != null) fileButton.setOnClickListener(v -> {
                MessageFile messageFile = (MessageFile) messages.get(getAdapterPosition());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(messageFile.getFileURL()));
                context.startActivity(browserIntent);
            });
        }
    }
}