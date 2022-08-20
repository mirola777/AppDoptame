package com.appdoptame.appdoptame.data.firestore.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.UserLoaderListener;
import com.appdoptame.appdoptame.data.parser.ParsePerson;
import com.appdoptame.appdoptame.data.service.IUserSession;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import java.util.Map;

public class UserSessionFS implements IUserSession {
    private static final String USER_SESSION = "USER_SESSION";
    private static final String USER_DATA    = "USER_DATA";

    @Override
    public void getUserSession(UserLoaderListener listener) {
        if(isUserActive()){
            SharedPreferences preferences = AppDoptameApp.getContext().getSharedPreferences(
                    USER_SESSION,
                    Context.MODE_PRIVATE
            );
            Gson   gson     = new Gson();
            String userJson = preferences.getString(USER_DATA, null);
            if(userJson == null){
                listener.onFailure();
            } else {
                User user = gson.fromJson(userJson, User.class);
                listener.onSuccess(user);
            }
        } else {
            listener.onFailure();
        }
    }

    @Override
    public void saveUserSession(CompleteListener listener) {
        if(isUserActive()){
            SharedPreferences preferences = AppDoptameApp.getContext().getSharedPreferences(
                    USER_SESSION,
                    Context.MODE_PRIVATE
            );

            FirebaseAuth auth     = FirebaseAuth.getInstance();
            FirebaseUser user     = auth.getCurrentUser();

            CollectionReference collectionUser = FirestoreDB.getCollectionUser();
            assert user != null;
            collectionUser.document(user.getUid()).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot doc        = task.getResult();
                    Map<String, Object> docData = doc.getData();
                    User person                 = ParsePerson.parse(docData);
                    Gson gson                   = new Gson();
                    String userJson             = gson.toJson(person);
                    preferences.edit().putString(USER_DATA, userJson).apply();
                    listener.onSuccess();
                } else {
                    listener.onFailure();
                }
            });
        } else {
            listener.onFailure();
        }
    }

    @Override
    public boolean isUserActive() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }
}
