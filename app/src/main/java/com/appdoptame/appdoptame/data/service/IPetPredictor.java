package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.PetPredictorListener;

public interface IPetPredictor {
    void predictRace(byte[] image, PetPredictorListener listener);
}
