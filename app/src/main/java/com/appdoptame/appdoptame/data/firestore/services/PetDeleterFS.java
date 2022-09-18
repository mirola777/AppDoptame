package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.service.IPetDeleter;
import com.appdoptame.appdoptame.model.Pet;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;

public class PetDeleterFS implements IPetDeleter {
    private static final CollectionReference collectionPet = FirestoreDB.getCollectionPet();
    private static final StorageReference    storagePet    = FirestoreDB.getStoragePet();

    @Override
    public void deletePet(Pet pet, CompleteListener listener) {
        deletePetImages(pet, listener, 0);
    }

    private void deletePetImages(Pet pet, CompleteListener listener, int counter){
        if(counter < pet.getImages().size()){
            StorageReference referenceImage = storagePet.child(pet.getID() + counter);
            referenceImage.delete().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    deletePetImages(pet, listener, counter + 1);
                } else {
                    listener.onFailure();
                }
            });
        } else {
            // YA TERMINO LA BORRADA DE IMAGENES, SE BORRAN LOS DATOS DE LA MASCOTA ACA
            deletePetData(pet, listener);
        }
    }

    private void deletePetData(Pet pet, CompleteListener listener){
        collectionPet.document(pet.getID()).delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                listener.onSuccess();
            } else {
                listener.onFailure();
            }
        });
    }
}
