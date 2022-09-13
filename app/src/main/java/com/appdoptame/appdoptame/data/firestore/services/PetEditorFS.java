package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.parser.ParsePost;
import com.appdoptame.appdoptame.data.service.IPetEditor;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.Post;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class PetEditorFS implements IPetEditor {
    private static final CollectionReference collectionPet  = FirestoreDB.getCollectionPet();
    private static final CollectionReference collectionPost = FirestoreDB.getCollectionPost();

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

    @Override
    public void updatePet(Pet pet, CompleteListener listener) {

    }
}
