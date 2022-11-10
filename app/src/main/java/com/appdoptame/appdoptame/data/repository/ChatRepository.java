package com.appdoptame.appdoptame.data.repository;

import android.net.Uri;

import com.appdoptame.appdoptame.data.listener.ChatCreatorListener;
import com.appdoptame.appdoptame.data.listener.ChatListLoaderListener;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.MessageListLoaderListener;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;

import java.util.List;

public interface ChatRepository {
    void createChat(Chat chat, Message firstMessage, ChatCreatorListener listener);
    void sendMessage(Message message, CompleteListener listener);
    void sendMessage(Message message, List<byte[]> images, CompleteListener listener);
    void sendMessage(Message message, Uri file, CompleteListener listener);
    void getUserChats(String userID, ChatListLoaderListener listener);
    void getChatMessages(String chatID, MessageListLoaderListener listener);
}
