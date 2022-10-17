package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.Message;

public interface MessageInserterListener {
    void onNewMessage(Message message);
    void onNewAdoptMessage(Message message);
}
