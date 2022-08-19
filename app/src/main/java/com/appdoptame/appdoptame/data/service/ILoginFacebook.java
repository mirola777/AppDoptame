package com.appdoptame.appdoptame.data.service;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.facebook.CallbackManager;

public interface ILoginFacebook {
    void loginFacebook(Fragment fragment, CallbackManager callbackManager, CompleteListener listener);
  //  void loginGoogleResult(Intent data, CompleteListener listener);
}
