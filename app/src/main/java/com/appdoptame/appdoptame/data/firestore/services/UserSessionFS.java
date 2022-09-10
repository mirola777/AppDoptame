package com.appdoptame.appdoptame.data.firestore.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.data.service.IUserSession;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class UserSessionFS implements IUserSession {
    private static final String USER_SESSION      = "USER_SESSION";
    private static final String USER_DATA         = "USER_DATA";

    @Override
    public User getUserSession() {
        if(isUserActive()){
            SharedPreferences preferences = AppDoptameApp.getContext().getSharedPreferences(
                    USER_SESSION,
                    Context.MODE_PRIVATE
            );
            Gson   gson     = new Gson();
            String userJson = preferences.getString(USER_DATA, null);

            if(userJson != null){
                return gson.fromJson(userJson, User.class);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void saveUserSession(User user) {
        if(isUserActive() && user != null){
            SharedPreferences preferences = AppDoptameApp.getContext().getSharedPreferences(
                    USER_SESSION,
                    Context.MODE_PRIVATE
            );

            Gson gson = new Gson();

            String userJson = gson.toJson(user);
            preferences.edit().putString(USER_DATA, userJson).apply();
        }
    }

    @Override
    public boolean isUserActive() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }
}
