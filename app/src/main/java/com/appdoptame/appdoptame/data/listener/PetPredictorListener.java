package com.appdoptame.appdoptame.data.listener;

public interface PetPredictorListener {
    void onSuccess(String prediction);
    void onFailure();
}
