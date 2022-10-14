package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.observer.ChatObserver;
import com.appdoptame.appdoptame.data.observer.MessageObserver;
import com.appdoptame.appdoptame.data.parser.ParseChat;
import com.appdoptame.appdoptame.data.parser.ParseMessage;
import com.appdoptame.appdoptame.data.service.IPostObserver;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;

import java.util.List;
import java.util.Map;

public class PostObserverFS implements IPostObserver {
    private final static CollectionReference collectionPost = FirestoreDB.getCollectionPost();

    @Override
    public void createObserver() {
        collectionPost
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        return;
                    }
                    postModified(value.getDocumentChanges());
                });
    }

    private void postModified(List<DocumentChange> documentChanges){
        /*
        for (DocumentChange dc : documentChanges) {
            switch (dc.getType()) {
                case ADDED:
                    Map<String, Object> chatDoc = dc.getDocument().getData();
                    Chat chat = ParseChat.parse(chatDoc);
                    ChatObserver.notifyChatCreated(chat);

                case REMOVED:
                    break;
                case MODIFIED:
                    chatDoc                                   = dc.getDocument().getData();
                    List<Map<String, Object>> messagesListDoc = (List<Map<String, Object>>) chatDoc.get("MESSAGES");
                    Map<String, Object>       lastMessageDoc  = messagesListDoc.get(messagesListDoc.size() - 1);
                    Message lastMessage     = ParseMessage.parse(lastMessageDoc);

                    MessageObserver.notifyNewMessage(lastMessage);

                    break;
            }
        }

         */
    }
}
