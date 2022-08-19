package com.appdoptame.appdoptame.data.repository;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.model.Pet;

public interface PetRepository {
    void getPet(String ID, PetLoaderListener listener);
    void createPet(Pet pet, CompleteListener listener);
    void updatePet(Pet pet, PetLoaderListener listener);
    void deletePet(String ID, CompleteListener listener);

}
