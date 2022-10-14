package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.Message;

import java.util.List;

public interface MessageListLoaderListener {
    void onSuccess(List<Message> messages);
    void onFailure();
}
