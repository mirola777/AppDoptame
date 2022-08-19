package com.appdoptame.appdoptame.data.repository;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.User;
import com.facebook.CallbackManager;

public interface UserRepository {
    void login(String email, String password, CompleteListener listener);
    void loginGoogle(Fragment fragment);
    void loginGoogleResult(Intent data, CompleteListener listener);
    void loginFacebook(Fragment fragment, CallbackManager callbackManager, CompleteListener listener);
    void singOut(CompleteListener listener);
    void singUp(String email, String password, String confirmPassword, User user, CompleteListener listener);
}
