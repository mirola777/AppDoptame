package com.appdoptame.appdoptame.data.firestore;

import com.appdoptame.appdoptame.data.firestore.services.PetCreatorFS;
import com.appdoptame.appdoptame.data.firestore.services.PetGetterFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.data.repository.PetRepository;
import com.appdoptame.appdoptame.data.service.IPetCreator;
import com.appdoptame.appdoptame.data.service.IPetGetter;
import com.appdoptame.appdoptame.model.Pet;

public class PetRepositoryFS implements PetRepository {
    private static PetRepositoryFS instance;
    private final IPetGetter iPetGetter;
    private final IPetCreator iPetCreator;

    private PetRepositoryFS() {
        this.iPetGetter = new PetGetterFS();
        this.iPetCreator = new PetCreatorFS();
    }

    public static PetRepositoryFS getInstance(){
        if(instance == null){
            instance = new PetRepositoryFS();
        }

        return instance;
    }

    @Override
    public void getPet(String ID, PetLoaderListener listener) {
        iPetGetter.getPet(ID, listener);
    }

    @Override
    public void createPet(Pet pet, CompleteListener listener) {
        iPetCreator.createPet(pet, listener);
    }

    @Override
    public void updatePet(Pet pet, PetLoaderListener listener) {

    }

    @Override
    public void deletePet(String ID, CompleteListener listener) {

    }
}
