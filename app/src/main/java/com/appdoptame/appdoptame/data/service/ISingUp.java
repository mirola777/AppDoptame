package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.User;

public interface ISingUp {
    void singUp(String email, String password, String confirmPassword, User user, CompleteListener listener);
}
