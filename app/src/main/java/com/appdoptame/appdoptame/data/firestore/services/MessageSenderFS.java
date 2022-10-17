package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.parser.ParseMessage;
import com.appdoptame.appdoptame.data.service.IMessageSender;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;

import java.util.Map;

public class MessageSenderFS implements IMessageSender {
    private static final CollectionReference collectionChat = FirestoreDB.getCollectionChat();

    @Override
    public void sendMessage(Message message, CompleteListener listener) {
        Map<String, Object> doc = ParseMessage.parse(message);
        collectionChat
                .document(message.getChatID())
                .update("MESSAGES", FieldValue.arrayUnion(doc))
                .addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                listener.onSuccess();
            } else {
                listener.onFailure();
            }
        });
    }

    @Override
    public void sendAdoptMessage(Message message, CompleteListener listener) {
        Map<String, Object> doc = ParseMessage.parseAdopt(message);
        collectionChat
                .document(message.getChatID())
                .update("MESSAGES", FieldValue.arrayUnion(doc))
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){
                        listener.onSuccess();
                    } else {
                        listener.onFailure();
                    }
                });
    }
}
