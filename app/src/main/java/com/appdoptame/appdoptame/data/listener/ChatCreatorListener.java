package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.Chat;

public interface ChatCreatorListener {
    void onChatCreated(Chat chat);
    void onFailure();
}
