package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.Chat;

public interface ChatLoaderListener {
    void onSuccess(Chat chat);
    void onFailure();
}
