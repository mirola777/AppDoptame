package com.appdoptame.appdoptame.data.firestore.services;

import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LoginListener;
import com.appdoptame.appdoptame.data.service.ILoginFacebook;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import java.util.Collections;

public class LoginFacebookFS implements ILoginFacebook {
    @Override
    public void loginFacebook(Fragment fragment, CallbackManager callbackManager, LoginListener listener) {
        System.out.println(callbackManager);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String token              = loginResult.getAccessToken().getToken();
                AuthCredential credential = FacebookAuthProvider.getCredential(token);
                FirebaseAuth auth         = FirebaseAuth.getInstance();
                auth.signInWithCredential(credential).addOnCompleteListener(authResult -> {
                    if(authResult.isSuccessful()){
                        UserRepositoryFS.getInstance().verifyProfileCreated(listener);
                    } else {
                        listener.onFailure();
                    }
                });
            }

            @Override
            public void onCancel() {
                listener.onFailure();
            }

            @Override
            public void onError(FacebookException error) {
                listener.onFailure();
            }
        });

        LoginManager.getInstance().logInWithReadPermissions(fragment, Collections.singletonList("public_profile"));
    }
}
