package com.appdoptame.appdoptame.data.firestore.services;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.LoginListener;
import com.appdoptame.appdoptame.data.service.ILoginGoogle;
import com.appdoptame.appdoptame.view.fragment.FragmentLogin;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginGoogleFS implements ILoginGoogle {

    @Override
    public void loginGoogle(Fragment fragment) {
        GoogleSignInOptions GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(AppDoptameApp.getContext().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(AppDoptameApp.getContext(), GSO);
        Intent googleSingInIntent = client.getSignInIntent();
        fragment.startActivityForResult(googleSingInIntent, FragmentLogin.GOOGLE);
    }

    @Override
    public void loginGoogleResult(Intent data, LoginListener listener) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account    = task.getResult(ApiException.class);
            AuthCredential credential      = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            FirebaseAuth auth              = FirebaseAuth.getInstance();
            auth.signInWithCredential(credential).addOnCompleteListener(authResult -> {
                if(authResult.isSuccessful()){
                    UserRepositoryFS.getInstance().verifyProfileCreated(listener);
                } else {
                    listener.onFailure();
                }
            });
        } catch (ApiException e) {
            listener.onFailure();
        }
    }
}
