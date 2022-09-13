package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Pet;

public interface IPetEditor {
    void changeState(Pet pet, CompleteListener listener);
    void updatePet(Pet pet, CompleteListener listener);
}
