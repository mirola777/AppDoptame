package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.ChatCreatorListener;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;

public interface IChatCreator {
    void createChat(Chat chat, Message firstMessage, ChatCreatorListener listener);
}
