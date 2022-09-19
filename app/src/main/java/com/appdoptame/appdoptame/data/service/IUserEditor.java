package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.User;

public interface IUserEditor {
    void editUser(User user, byte[] userImage, CompleteListener listener);
}
