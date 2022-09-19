package com.appdoptame.appdoptame.data.repository;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LoginListener;
import com.appdoptame.appdoptame.model.User;
import com.facebook.CallbackManager;

public interface UserRepository {
    void login(String email, String password, LoginListener listener);
    void loginGoogle(Fragment fragment);
    void loginGoogleResult(Intent data, LoginListener listener);
    void loginFacebook(Fragment fragment, CallbackManager callbackManager, LoginListener listener);
    void singOut(CompleteListener listener);
    void singUp(String email, String password, String confirmPassword, User user, CompleteListener listener);
    void createUser(User user, byte[] userImage, CompleteListener listener);
    void editUser(User user, byte[] userImage, CompleteListener listener);
    void verifyProfileCreated(LoginListener listener);
    boolean isUserActive();
    User getUserSession();
    void saveUserSession(User user);
    void deleteUserSession();
}
