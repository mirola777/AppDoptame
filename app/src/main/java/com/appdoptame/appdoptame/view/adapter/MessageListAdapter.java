package com.appdoptame.appdoptame.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.MessageInserterListener;
import com.appdoptame.appdoptame.data.observer.MessageObserver;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.DateTextGetter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> implements MessageInserterListener {
    private final static int LEFT     = 0;
    private final static int RIGHT    = 1;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case LEFT:     return new ViewHolder(  inflater.inflate(R.layout.adapter_message_list_left,   parent, false));
            case RIGHT:    return new ViewHolder(   inflater.inflate(R.layout.adapter_message_list_right,  parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getWriterID().equals(userSession.getID())){
            return RIGHT;
        } else {
            return LEFT;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageText.setText(message.getMessage());
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
        if(lastMessage.getDate().getTime() != message.getDate().getTime() && message.getChatID().equals(chat.getID())){
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                messages.add(message);
                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return 500f / displayMetrics.densityDpi;
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

    @Override
    public void onNewAdoptMessage(Message message) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView dateText;

        ViewHolder(View itemView) {
            super(itemView);
            dateText    = itemView.findViewById(R.id.adapter_message_list_date);
            messageText = itemView.findViewById(R.id.adapter_message_list_message);
        }
    }
}