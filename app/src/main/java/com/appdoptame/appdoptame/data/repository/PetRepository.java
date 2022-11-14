package com.appdoptame.appdoptame.data.repository;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.data.listener.PetPredictorListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.model.Pet;

import java.util.List;

public interface PetRepository {
    void getPet(String ID, PetLoaderListener listener);
    void getUserPets(String userID, PetListLoaderListener listener);
    void createPet(Pet pet, List<byte[]> petImages, CompleteListener listener);
    void updatePet(Pet pet, List<byte[]> petImages, CompleteListener listener);
    void changeState(Pet pet, CompleteListener listener);
    void deletePet(Pet pet, CompleteListener listener);
    void predictRace(byte[] image, PetPredictorListener listener);
    void recommend(PostLoaderListener listener);
}
