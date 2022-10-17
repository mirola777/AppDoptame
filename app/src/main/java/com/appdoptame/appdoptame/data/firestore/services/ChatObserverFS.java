package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.observer.ChatObserver;
import com.appdoptame.appdoptame.data.observer.MessageObserver;
import com.appdoptame.appdoptame.data.parser.ParseChat;
import com.appdoptame.appdoptame.data.parser.ParseMessage;
import com.appdoptame.appdoptame.data.service.IChatObserver;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.User;
import com.appdoptame.appdoptame.util.MessageConstants;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.MetadataChanges;

import java.util.List;
import java.util.Map;

public class ChatObserverFS implements IChatObserver {
    private final static CollectionReference collectionChat = FirestoreDB.getCollectionChat();

    @Override
    public void createObserver() {
        User user = UserRepositoryFS.getInstance().getUserSession();

        collectionChat
                .whereEqualTo("OWNER.ID", user.getID())
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        return;
                    }
                    chatModified(value.getDocumentChanges());
                });

        collectionChat
                .whereEqualTo("ADOPTER.ID", user.getID())
                .addSnapshotListener(MetadataChanges.EXCLUDE, (value, e) -> {
                    if (e != null) {
                        return;
                    }
                    chatModified(value.getDocumentChanges());
                });
    }

    private void chatModified(List<DocumentChange> documentChanges){
        for (DocumentChange dc : documentChanges) {
            switch (dc.getType()) {
                case ADDED:
                    Map<String, Object> chatDoc = dc.getDocument().getData();
                    Chat chat = ParseChat.parse(chatDoc);
                    ChatObserver.notifyChatCreated(chat);
                    break;

                case REMOVED:
                    break;
                case MODIFIED:
                    chatDoc                                   = dc.getDocument().getData();
                    chat                                      = ParseChat.parse(chatDoc);
                    List<Map<String, Object>> messagesListDoc = (List<Map<String, Object>>) chatDoc.get("MESSAGES");
                    Map<String, Object>       lastMessageDoc  = messagesListDoc.get(messagesListDoc.size() - 1);
                    Message                   lastMessage     = ParseMessage.parse(lastMessageDoc);

                    ChatObserver.notifyChatEdited(chat);

                    if(lastMessageDoc.get("TYPE") != null){
                        switch ((String) lastMessageDoc.get("TYPE")){
                            case MessageConstants.NORMAL:
                                MessageObserver.notifyNewMessage(lastMessage);
                                break;
                            case MessageConstants.ADOPT:
                                MessageObserver.notifyNewAdoptMessage(lastMessage);
                                break;
                        }
                    } else {
                        MessageObserver.notifyNewMessage(lastMessage);
                    }

                    break;
            }
        }
    }
}
