package com.appdoptame.appdoptame.data.service;

import android.net.Uri;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Message;

import java.util.List;

public interface IMessageSender {
    void sendMessage(Message message, CompleteListener listener);
    void sendMessage(Message message, List<byte[]> images, CompleteListener listener);
    void sendMessage(Message message, Uri file, CompleteListener listener);
}
