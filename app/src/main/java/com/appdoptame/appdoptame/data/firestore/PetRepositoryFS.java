package com.appdoptame.appdoptame.data.firestore;

import com.appdoptame.appdoptame.data.firestore.services.PetGetterFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.data.repository.PetRepository;
import com.appdoptame.appdoptame.data.service.IPetGetter;
import com.appdoptame.appdoptame.model.Pet;

public class PetRepositoryFS implements PetRepository {
    private final IPetGetter iPetGetter;

    private PetRepositoryFS() {
        this.iPetGetter = new PetGetterFS();
    }

    @Override
    public void getPet(String ID, PetLoaderListener listener) {

    }

    @Override
    public void createPet(Pet pet, CompleteListener listener) {

    }

    @Override
    public void updatePet(Pet pet, PetLoaderListener listener) {

    }

    @Override
    public void deletePet(String ID, CompleteListener listener) {

    }
}
