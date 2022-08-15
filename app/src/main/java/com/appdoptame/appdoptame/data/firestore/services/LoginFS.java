package com.appdoptame.appdoptame.data.firestore.services;

import androidx.annotation.NonNull;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LoginListener;
import com.appdoptame.appdoptame.data.service.ILogin;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginFS implements ILogin {
    @Override
    public void login(String email, String password, LoginListener listener) {
        if(email.length() > 0 && password.length() > 0){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SingInTask -> {
                if (SingInTask.isSuccessful()) {
                    UserRepositoryFS.getInstance().verifyProfileCreated(listener);
                } else {
                    listener.onFailure();
                }
            });
        }
    }

    @Override
    public void singOut(CompleteListener listener) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        // En caso de que tenga sesión iniciada con google o facebook, se debe de cerrar
        // la sesión igualmente en esos sitios.
        for(UserInfo data: user.getProviderData()){
            switch (data.getProviderId()){
                case "google.com":
                    GoogleSignInOptions GSO
                            = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(AppDoptameApp.getContext().getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();

                    GoogleSignInClient client = GoogleSignIn.getClient(AppDoptameApp.getContext(), GSO);
                    client.signOut();
                case "facebook.com":
                    break;
            }
        }
        auth.signOut();
        listener.onSuccess();
    }
}
