package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.UserLoaderListener;

public interface IUserSession {
    void getUserSession(UserLoaderListener listener);
    void saveUserSession(CompleteListener listener);
    boolean isUserActive();
}
