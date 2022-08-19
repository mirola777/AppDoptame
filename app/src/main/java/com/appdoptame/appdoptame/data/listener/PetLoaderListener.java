package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.Pet;

public interface PetLoaderListener {
    void onSuccess(Pet pet);
    void onFailure();
}
