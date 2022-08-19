package com.appdoptame.appdoptame.data.service;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.data.listener.LoginListener;

public interface ILoginGoogle {
    void loginGoogle(Fragment fragment);
    void loginGoogleResult(Intent data, LoginListener listener);
}
