package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.model.Pet;

public interface IPetCreator {
    void createPet(Pet pet, CompleteListener listener);
    void verifyPetCreated(PetLoaderListener listener);
}
