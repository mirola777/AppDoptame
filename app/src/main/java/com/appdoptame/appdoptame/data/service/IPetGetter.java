package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.PetLoaderListener;

public interface IPetGetter {
    void getPet(String ID, PetLoaderListener listener);

}
