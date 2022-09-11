package com.appdoptame.appdoptame.data.service;

import android.net.Uri;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LoginListener;
import com.appdoptame.appdoptame.model.User;

public interface IUserCreator {
    void createUser(User user, Uri userImage, CompleteListener listener);
    void verifyProfileCreated(LoginListener listener);
}
