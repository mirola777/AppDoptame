package com.appdoptame.appdoptame.data.firestore;

import android.net.Uri;

import com.appdoptame.appdoptame.data.firestore.services.ChatCreatorFS;
import com.appdoptame.appdoptame.data.firestore.services.ChatGetterFS;
import com.appdoptame.appdoptame.data.firestore.services.ChatObserverFS;
import com.appdoptame.appdoptame.data.firestore.services.MessageSenderFS;
import com.appdoptame.appdoptame.data.listener.ChatCreatorListener;
import com.appdoptame.appdoptame.data.listener.ChatListLoaderListener;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.MessageListLoaderListener;
import com.appdoptame.appdoptame.data.repository.ChatRepository;
import com.appdoptame.appdoptame.data.service.IChatCreator;
import com.appdoptame.appdoptame.data.service.IChatGetter;
import com.appdoptame.appdoptame.data.service.IChatObserver;
import com.appdoptame.appdoptame.data.service.IMessageSender;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;

import java.io.File;
import java.util.List;

public class ChatRepositoryFS implements ChatRepository {
    private static ChatRepositoryFS instance;
    private final IMessageSender iMessageSender;
    private final IChatCreator   iChatCreator;
    private final IChatGetter    iChatGetter;
    private final IChatObserver  iChatObserver;

    private ChatRepositoryFS() {
        this.iMessageSender = new MessageSenderFS();
        this.iChatCreator   = new ChatCreatorFS();
        this.iChatGetter    = new ChatGetterFS();
        this.iChatObserver  = new ChatObserverFS();

        iChatObserver.createObserver();
    }

    public static ChatRepositoryFS getInstance(){
        if(instance == null){
            instance = new ChatRepositoryFS();
        }

        return instance;
    }

    @Override
    public void createChat(Chat chat, Message firstMessage, ChatCreatorListener listener) {
        iChatCreator.createChat(chat, firstMessage, listener);
    }

    @Override
    public void sendMessage(Message message, CompleteListener listener) {
        iMessageSender.sendMessage(message, listener);
    }

    @Override
    public void sendMessage(Message message, List<byte[]> images, CompleteListener listener) {
        iMessageSender.sendMessage(message, images, listener);
    }

    @Override
    public void sendMessage(Message message, Uri file, CompleteListener listener) {
        iMessageSender.sendMessage(message, file, listener);
    }

    @Override
    public void getUserChats(String userID, ChatListLoaderListener listener) {
        iChatGetter.getUserChats(userID, listener);
    }

    @Override
    public void getChatMessages(String chatID, MessageListLoaderListener listener) {
        iChatGetter.getChatMessages(chatID, listener);
    }
}
