package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.ChatCreatorListener;
import com.appdoptame.appdoptame.data.parser.ParseChat;
import com.appdoptame.appdoptame.data.service.IChatCreator;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class ChatCreatorFS implements IChatCreator {
    private static final CollectionReference collectionChat = FirestoreDB.getCollectionChat();

    @Override
    public void createChat(Chat chat, Message firstMessage, ChatCreatorListener listener) {

        String chatID = collectionChat.document().getId();
        chat.setID(chatID);
        firstMessage.setChatID(chatID);
        chat.setLastMessage(firstMessage);

        Map<String, Object> doc = ParseChat.parse(chat);

        collectionChat
                .whereEqualTo("ADOPTER.ID", chat.getAdopter().getID())
                .whereEqualTo("PET.ID",     chat.getPet().getID())
                .whereEqualTo("OWNER.ID",   chat.getOwner().getID())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if(task.getResult().size() > 0){
                            // YA EXISTE EL CHAT
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            Chat chatAlreadyExists = ParseChat.parse(document.getData());
                            listener.onChatCreated(chatAlreadyExists);

                        } else {
                            // NO EXISTE EL CHAT
                            collectionChat.document(chat.getID()).set(doc).addOnCompleteListener(taskCreate -> {
                                if (taskCreate.isSuccessful()){
                                    listener.onChatCreated(chat);
                                } else {
                                    listener.onFailure();
                                }
                            });
                        }
                    } else {
                        listener.onFailure();
                    }
                });


    }
}
