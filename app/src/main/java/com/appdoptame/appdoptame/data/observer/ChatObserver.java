package com.appdoptame.appdoptame.data.observer;

import com.appdoptame.appdoptame.data.listener.ChatCreatorListener;
import com.appdoptame.appdoptame.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatObserver {
    private static List<ChatCreatorListener> chatCreatorListeners;

    private ChatObserver(){
        chatCreatorListeners   = new ArrayList<>();
    }

    public static void attachChatCreatorListener(ChatCreatorListener listener){
        if(chatCreatorListeners == null) new ChatObserver();

        if(!chatCreatorListeners.contains(listener))
            chatCreatorListeners.add(listener);
    }

    public static void detachChatCreatorListener(ChatCreatorListener listener){
        if(chatCreatorListeners == null) new ChatObserver();

        chatCreatorListeners.remove(listener);
    }

    public static void notifyChatCreated(Chat chat){
        if(chatCreatorListeners == null) new ChatObserver();

        for(ChatCreatorListener listener: chatCreatorListeners){
            listener.onChatCreated(chat);
        }
    }
}
