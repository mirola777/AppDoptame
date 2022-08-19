package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.PetListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.data.service.IPetGetter;
import com.appdoptame.appdoptame.model.Pet;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

public class PetGetterFS implements IPetGetter {
    @Override
    public void getFeedPets(PetListLoaderListener listener) {
        ArrayList<Pet> pets = new ArrayList<>();
        CollectionReference collectionPet = FirestoreDB.getCollectionPet();


    }

    @Override
    public void getPet(String ID, PetLoaderListener listener) {

    }
}
