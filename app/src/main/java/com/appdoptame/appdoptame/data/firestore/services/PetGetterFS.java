package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.PetListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.data.parser.ParsePet;
import com.appdoptame.appdoptame.data.service.IPetGetter;
import com.appdoptame.appdoptame.model.Pet;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetGetterFS implements IPetGetter {
    private static final  CollectionReference collectionPet = FirestoreDB.getCollectionPet();

    @Override
    public void getFeedPets(PetListLoaderListener listener) {
        ArrayList<Pet> pets = new ArrayList<>();



    }

    @Override
    public void getPet(String ID, PetLoaderListener listener) {

    }

    @Override
    public void getUserPets(String userID, PetListLoaderListener listener) {
        List<Pet> pets = new ArrayList<>();

        collectionPet.whereEqualTo("OWNER.ID", userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    if(document.getData() != null){
                        pets.add(ParsePet.parse(document.getData()));
                    }
                }
                listener.onSuccess(pets);
            } else {
                listener.onFailure();
            }
        });
    }
}
