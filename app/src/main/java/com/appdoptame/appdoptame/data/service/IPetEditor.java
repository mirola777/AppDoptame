package com.appdoptame.appdoptame.data.service;

import android.net.Uri;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Pet;

import java.util.List;

public interface IPetEditor {
    void changeState(Pet pet, CompleteListener listener);
    void updatePet(Pet pet, List<byte[]> petImages, CompleteListener listener);
}
