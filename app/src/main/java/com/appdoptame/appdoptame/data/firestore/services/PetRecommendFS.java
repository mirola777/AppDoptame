package com.appdoptame.appdoptame.data.firestore.services;

import android.os.Handler;
import android.os.Looper;

import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.data.service.IPetRecommend;

public class PetRecommendFS implements IPetRecommend {
    @Override
    public void recommend(PostLoaderListener listener) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(listener::onFailure, 5000);
    }
}
