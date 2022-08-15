package com.appdoptame.appdoptame.data.repository;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.User;

public interface UserRepository {
    void login(String email, String password, CompleteListener listener);
    void loginGoogle(CompleteListener listener);
    void singOut(CompleteListener listener);
    void singUp(String email, String password, String confirmPassword, User user, CompleteListener listener);
}
