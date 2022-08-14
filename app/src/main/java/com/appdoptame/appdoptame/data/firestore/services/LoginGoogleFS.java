package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.service.ILoginGoogle;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.appdoptame.appdoptame.R;

public class LoginGoogleFS implements ILoginGoogle {
    @Override
    public void loginGoogle(CompleteListener listener) {
        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(AppDoptameApp.getContext().getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();
    }
}
