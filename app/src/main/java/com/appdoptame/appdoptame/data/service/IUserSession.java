package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.UserLoaderListener;
import com.appdoptame.appdoptame.model.User;

public interface IUserSession {
    User getUserSession();
    void saveUserSession(User user);
    void deleteUserSession();
    boolean isUserActive();
}
