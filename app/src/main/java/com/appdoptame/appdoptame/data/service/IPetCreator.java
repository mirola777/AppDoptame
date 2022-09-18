package com.appdoptame.appdoptame.data.service;

import android.net.Uri;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.model.Pet;

import java.util.List;

public interface IPetCreator {
    void createPet(Pet pet, List<byte[]> petImages, CompleteListener listener);
    void verifyPetCreated(PetLoaderListener listener);
}
