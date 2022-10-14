package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.Chat;

import java.util.List;

public interface ChatListLoaderListener {
    void onSuccess(List<Chat> chats);
    void onFailure();
}
