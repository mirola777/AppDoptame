package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.parser.ParseChat;
import com.appdoptame.appdoptame.data.parser.ParsePet;
import com.appdoptame.appdoptame.data.parser.ParsePost;
import com.appdoptame.appdoptame.data.service.IPetEditor;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.Post;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PetEditorFS implements IPetEditor {
    private static final CollectionReference collectionPet  = FirestoreDB.getCollectionPet();
    private static final CollectionReference collectionPost = FirestoreDB.getCollectionPost();
    private static final CollectionReference collectionChat = FirestoreDB.getCollectionChat();
    private static final StorageReference    storagePet     = FirestoreDB.getStoragePet();

    @Override
    public void changeState(Pet pet, CompleteListener listener) {
        pet.setAdopted(!pet.isAdopted());
        listener.onSuccess();

        collectionPet
                .document(pet.getID())
                .update("ADOPTED", pet.isAdopted())
                .addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                // Cambiar el estado en Post tambien
                collectionPost
                        .whereEqualTo("PET.ID", pet.getID())
                        .get()
                        .addOnCompleteListener(postTask -> {
                    if(postTask.isSuccessful()){
                        for (DocumentSnapshot document : postTask.getResult()) {
                            if(document.getData() != null){
                                Post petPost = ParsePost.parse(document.getData());
                                collectionPost
                                        .document(petPost.getID())
                                        .update("PET.ADOPTED", pet.isAdopted())
                                        .addOnCompleteListener(petTask -> {
                                    if(petTask.isSuccessful()){

                                        collectionChat
                                                .whereEqualTo("PET.ID", pet.getID())
                                                .get()
                                                .addOnCompleteListener(chatTask -> {
                                                    if(chatTask.isSuccessful()){
                                                        for (DocumentSnapshot documentChat : chatTask.getResult()) {
                                                            if(documentChat.getData() != null){
                                                                Chat chat = ParseChat.parse(documentChat.getData());
                                                                collectionChat
                                                                        .document(chat.getID())
                                                                        .update("PET.ADOPTED", pet.isAdopted())
                                                                        .addOnCompleteListener(petChatTask -> {
                                                                            if(petChatTask.isSuccessful()){
                                                                                listener.onSuccess();
                                                                            } else {
                                                                                listener.onFailure();
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    } else {
                                                        listener.onFailure();
                                                    }
                                                });
                                    } else {
                                        listener.onFailure();
                                    }
                                });
                            }
                        }
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
    public void updatePet(Pet pet, List<byte[]> petImages, CompleteListener listener) {
        deletePetExtraImages(pet, petImages, listener, 0);
    }

    private void deletePetExtraImages(Pet pet, List<byte[]> petImages, CompleteListener listener, int counter){
        if(pet.getImages().size() > petImages.size() + counter){
            StorageReference referenceImage = storagePet.child(pet.getID() + (petImages.size() + counter));
            referenceImage.delete().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    deletePetExtraImages(pet, petImages, listener, counter + 1);
                } else {
                    listener.onFailure();
                }
            });
        } else {
            pet.getImages().clear();
            updatePetImages(pet, petImages, listener, 0);
        }
    }

    private void updatePetImages(Pet pet, List<byte[]> petImages, CompleteListener listener, int counter){
        if(counter < petImages.size()){
            StorageReference referenceImage = storagePet.child(pet.getID() + counter);
            UploadTask uploadTask = referenceImage.putBytes(petImages.get(counter));
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) throw Objects.requireNonNull(task.getException());
                return referenceImage.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String imageUrl = task.getResult().toString();
                    pet.getImages().add(imageUrl);
                    updatePetImages(pet, petImages, listener,counter + 1);
                } else {
                    listener.onFailure();
                }
            });

        } else {
            // YA TERMINO LA SUBIDA DE IMAGENES, SE SUBEN LOS DATOS DE LA MASCOTA ACA
            updatePetData(pet, listener);
        }
    }

    private void updatePetData(Pet pet, CompleteListener listener){
        Map<String, Object> doc = ParsePet.parse(pet);
        collectionPet.document(pet.getID()).update(doc).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                // Cambiar el estado en Post tambien
                collectionPost
                        .whereEqualTo("PET.ID", pet.getID())
                        .get()
                        .addOnCompleteListener(postTask -> {
                            if(postTask.isSuccessful()){
                                for (DocumentSnapshot document : postTask.getResult()) {
                                    if(document.getData() != null){
                                        Post petPost = ParsePost.parse(document.getData());
                                        collectionPost
                                                .document(petPost.getID())
                                                .update("PET.IMAGES", pet.getImages())
                                                .addOnCompleteListener(petTask -> {
                                                    if(petTask.isSuccessful()){
                                                        listener.onSuccess();
                                                    } else {
                                                        listener.onFailure();
                                                    }
                                                });
                                    }
                                }
                            } else {
                                listener.onFailure();
                            }
                        });
            } else {
                listener.onFailure();
            }
        });
    }
}
