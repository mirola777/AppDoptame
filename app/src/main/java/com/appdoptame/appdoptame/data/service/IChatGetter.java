package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.ChatListLoaderListener;
import com.appdoptame.appdoptame.data.listener.MessageListLoaderListener;

public interface IChatGetter {
    void getUserChats(String userID, ChatListLoaderListener listener);
    void getChatMessages(String chatID, MessageListLoaderListener listener);
}
