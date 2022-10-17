package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Chat;
import com.appdoptame.appdoptame.model.Message;

public interface IMessageSender {
    void sendMessage(Message message, CompleteListener listener);
    void sendAdoptMessage(Message message, CompleteListener listener);
}
