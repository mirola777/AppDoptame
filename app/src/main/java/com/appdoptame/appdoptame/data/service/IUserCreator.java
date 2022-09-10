package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LoginListener;
import com.appdoptame.appdoptame.model.User;

public interface IUserCreator {
    void createUser(User user, CompleteListener listener);
    void verifyProfileCreated(LoginListener listener);
}
