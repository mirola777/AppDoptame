package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LoginListener;

public interface ILogin {
    void login(String email, String password, LoginListener listener);
    void singOut(CompleteListener listener);
}
