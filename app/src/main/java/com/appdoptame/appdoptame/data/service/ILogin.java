package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;

public interface ILogin {
    void login(String email, String password, CompleteListener listener);
    void singOut(CompleteListener listener);
}
