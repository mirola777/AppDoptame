package com.appdoptame.appdoptame.data.repository;

import android.net.Uri;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.model.Pet;

import java.util.List;

public interface PetRepository {
    void getPet(String ID, PetLoaderListener listener);
    void getUserPets(String userID, PetListLoaderListener listener);
    void createPet(Pet pet, List<Uri> petImages, CompleteListener listener);
    void updatePet(Pet pet, CompleteListener listener);
    void changeState(Pet pet, CompleteListener listener);
    void deletePet(String ID, CompleteListener listener);
}
