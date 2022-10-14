package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.ChatListLoaderListener;
import com.appdoptame.appdoptame.data.listener.MessageListLoaderListener;
import com.appdoptame.appdoptame.data.parser.ParseChat;
import com.appdoptame.appdoptame.data.parser.ParseMessage;
import com.appdoptame.appdoptame.data.service.IChatGetter;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatGetterFS implements IChatGetter {
    private static final CollectionReference collectionChat = FirestoreDB.getCollectionChat();

    @Override
    public void getUserChats(String userID, ChatListLoaderListener listener) {
        List<Chat> chats = new ArrayList<>();

        collectionChat
                .whereEqualTo("OWNER.ID", userID)
                .get()
                .addOnCompleteListener(taskOwner -> {
                    if (taskOwner.isSuccessful()){

                        collectionChat
                                .whereEqualTo("ADOPTER.ID", userID)
                                .get()
                                .addOnCompleteListener(taskAdopter -> {
                                    if (taskAdopter.isSuccessful()){
                                        for (DocumentSnapshot document : taskAdopter.getResult()) {
                                            if(document.getData() != null){
                                                chats.add(ParseChat.parse(document.getData()));
                                            }
                                        }

                                        for (DocumentSnapshot document : taskOwner.getResult()) {
                                            if(document.getData() != null){
                                                chats.add(ParseChat.parse(document.getData()));
                                            }
                                        }

                                        listener.onSuccess(chats);
                                    } else {
                                        listener.onFailure();
                                    }
                                });
                    } else {
                        listener.onFailure();
                    }
                });
    }

    @Override
    public void getChatMessages(String chatID, MessageListLoaderListener listener) {
        collectionChat
                .whereEqualTo("ID", chatID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        Map<String, Object> doc   = document.getData();

                        List<Map<String, Object>> messagesDoc = (List<Map<String, Object>>) doc.get("MESSAGES");
                        List<Message> messages = ParseMessage.parseDocList(messagesDoc);

                        listener.onSuccess(messages);
                    } else {
                        listener.onFailure();
                    }
                });
    }
}
