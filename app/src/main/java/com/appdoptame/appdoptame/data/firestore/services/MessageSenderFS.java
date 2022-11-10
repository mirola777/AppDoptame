package com.appdoptame.appdoptame.data.firestore.services;

import android.net.Uri;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.parser.ParseMessage;
import com.appdoptame.appdoptame.data.service.IMessageSender;
import com.appdoptame.appdoptame.model.Message;
import com.appdoptame.appdoptame.model.message.MessageFile;
import com.appdoptame.appdoptame.model.message.MessageImage;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MessageSenderFS implements IMessageSender {
    private static final CollectionReference collectionChat = FirestoreDB.getCollectionChat();
    private static final StorageReference    storageChat    = FirestoreDB.getStorageChat();

    @Override
    public void sendMessage(Message message, CompleteListener listener) {
        message.setID(collectionChat.document().getId());
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
    public void sendMessage(Message message, List<byte[]> images, CompleteListener listener) {
        uploadMessageImages(message, images, 0, listener);
    }

    @Override
    public void sendMessage(Message message, Uri file, CompleteListener listener) {
        uploadMessageFile(message, file, listener);
    }

    private void uploadMessageFile(Message message, Uri file, CompleteListener listener) {
        StorageReference referenceFile = storageChat.child(collectionChat.document().getId() + "-" + file.getPath());
        UploadTask uploadTask = referenceFile.putFile(file);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) throw Objects.requireNonNull(task.getException());
            return referenceFile.getDownloadUrl();

        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                String fileUrl = task.getResult().toString();

                MessageFile messageImage = new MessageFile(
                        message.getID(),
                        message.getChatID(),
                        message.getWriterID(),
                        message.getDate(),
                        message.getMessage(),
                        new File(file.getPath()).getName(),
                        fileUrl
                );

                sendMessage(messageImage, listener);

            } else {
                listener.onFailure();
            }
        });
    }

    private void uploadMessageImages(Message message, List<byte[]> images, int counter, CompleteListener listener) {
        if (counter < images.size()) {
            StorageReference referenceImage = storageChat.child(collectionChat.document().getId() + counter);
            UploadTask uploadTask = referenceImage.putBytes(images.get(counter));
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) throw Objects.requireNonNull(task.getException());
                return referenceImage.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    String imageUrl = task.getResult().toString();
                    MessageImage messageImage = new MessageImage(
                            message.getID(),
                            message.getChatID(),
                            message.getWriterID(),
                            message.getDate(),
                            message.getMessage(),
                            imageUrl
                    );

                    sendMessage(messageImage, new CompleteListener() {
                        @Override
                        public void onSuccess() {
                            uploadMessageImages(message, images, counter + 1, listener);
                        }

                        @Override
                        public void onFailure() {
                            listener.onFailure();
                        }
                    });

                } else {
                    listener.onFailure();
                }
            });

        } else {
            // YA TERMINO LA SUBIDA DE IMAGENES
            sendMessage(message, listener);
        }
    }
}
