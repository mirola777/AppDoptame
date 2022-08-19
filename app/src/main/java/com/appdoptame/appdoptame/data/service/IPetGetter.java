package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.PetListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;

public interface IPetGetter {
    void getFeedPets(PetListLoaderListener listener);
    void getPet(String ID, PetLoaderListener listener);

}
