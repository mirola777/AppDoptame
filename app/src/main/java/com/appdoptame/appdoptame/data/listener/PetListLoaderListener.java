package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.Pet;

import java.util.List;

public interface PetListLoaderListener {
    void onSuccess(List<Pet> pets);
    void onFailure();
}
