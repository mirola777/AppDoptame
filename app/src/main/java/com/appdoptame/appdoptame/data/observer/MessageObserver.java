package com.appdoptame.appdoptame.data.observer;

import com.appdoptame.appdoptame.data.listener.MessageInserterListener;
import com.appdoptame.appdoptame.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageObserver {
    private static List<MessageInserterListener> messageInserterListeners;

    private MessageObserver(){
        messageInserterListeners = new ArrayList<>();
    }

    public static void attachMessageInserterListener(MessageInserterListener listener){
        if(messageInserterListeners == null) new MessageObserver();

        if(!messageInserterListeners.contains(listener))
            messageInserterListeners.add(listener);
    }

    public static void detachMessageInserterListener(MessageInserterListener listener){
        if(messageInserterListeners == null) new MessageObserver();

        messageInserterListeners.remove(listener);
    }

    public static void notifyNewMessage(Message message){
        if(messageInserterListeners == null) new MessageObserver();

        for(MessageInserterListener listener: messageInserterListeners){
            listener.onNewMessage(message);
        }
    }
}
