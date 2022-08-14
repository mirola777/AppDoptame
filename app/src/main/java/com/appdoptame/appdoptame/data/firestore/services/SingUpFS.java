package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.service.ISingUp;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;

public class SingUpFS implements ISingUp {
    @Override
    public void singUp(String email, String password, User user, CompleteListener listener) {
        if(email.length() > 0 && password.length() > 0 && user != null){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Meter user en la base de datos
                    CollectionReference collectionUser = FirestoreDB.getCollectionUser();


                    listener.onSuccess();
                } else {
                    listener.onFailure();
                }
            });
        }
    }
}
