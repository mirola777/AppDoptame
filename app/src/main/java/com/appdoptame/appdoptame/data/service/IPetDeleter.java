package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Pet;

public interface IPetDeleter {
    void deletePet(Pet pet, CompleteListener listener);
}
