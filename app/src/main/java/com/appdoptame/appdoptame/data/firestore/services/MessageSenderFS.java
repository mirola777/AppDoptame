package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.service.IMessageSender;
import com.appdoptame.appdoptame.model.Message;
import com.google.firebase.firestore.CollectionReference;

public class MessageSenderFS implements IMessageSender {
    private static final CollectionReference collectionChat = FirestoreDB.getCollectionChat();

    @Override
    public void sendMessage(Message message, CompleteListener listener) {

    }
}
