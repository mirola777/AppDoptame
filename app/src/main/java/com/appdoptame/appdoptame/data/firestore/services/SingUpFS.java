package com.appdoptame.appdoptame.data.firestore.services;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.widget.Toast;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.service.ISingUp;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;

public class SingUpFS implements ISingUp {
    @Override
    public void singUp(String email, String password, String confirmPassword ,User user, CompleteListener listener) {
        if(email.length() > 0 && password.length() > 0 && confirmPassword.equals(password)){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onFailure();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Ingrese todos los valores", Toast.LENGTH_SHORT).show();
        }
    }
}
