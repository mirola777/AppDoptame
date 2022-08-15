package com.appdoptame.appdoptame.data.service;

import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.data.listener.LoginListener;
import com.facebook.CallbackManager;

public interface ILoginFacebook {
    void loginFacebook(Fragment fragment, CallbackManager callbackManager, LoginListener listener);
  //  void loginGoogleResult(Intent data, CompleteListener listener);
}
