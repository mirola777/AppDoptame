package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.service.IPetDeleter;
import com.appdoptame.appdoptame.model.Pet;
import com.google.firebase.firestore.CollectionReference;

public class PetDeleterFS implements IPetDeleter {
    private static final CollectionReference collectionPet = FirestoreDB.getCollectionPet();

    @Override
    public void deletePet(Pet pet, CompleteListener listener) {
        collectionPet.document(pet.getID()).delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                listener.onSuccess();
            } else {
                listener.onFailure();
            }
        });
    }
}
