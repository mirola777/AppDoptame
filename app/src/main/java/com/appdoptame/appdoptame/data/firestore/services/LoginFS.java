package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.service.ILogin;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFS implements ILogin {
    @Override
    public void login(String email, String password, CompleteListener listener) {
        if(email.length() > 0 && password.length() > 0){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onFailure();
                }
            });
        }
    }

    @Override
    public void singOut(CompleteListener listener) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        listener.onSuccess();
    }
}
