package com.appdoptame.appdoptame.data.firestore.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.data.listener.UserLoaderListener;
import com.appdoptame.appdoptame.data.service.IUserSession;
import com.appdoptame.appdoptame.model.Organization;
import com.appdoptame.appdoptame.model.Person;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class UserSessionFS implements IUserSession {
    private static final String USER_SESSION      = "USER_SESSION";
    private static final String PERSON_DATA       = "PERSON_DATA";
    private static final String ORGANIZATION_DATA = "ORGANIZATION_DATA";

    @Override
    public User getUserSession() {
        if(isUserActive()){
            SharedPreferences preferences = AppDoptameApp.getContext().getSharedPreferences(
                    USER_SESSION,
                    Context.MODE_PRIVATE
            );
            Gson   gson     = new Gson();
            String organizationJson = preferences.getString(ORGANIZATION_DATA, null);
            String personJson       = preferences.getString(PERSON_DATA, null);

            if(personJson != null){
                return gson.fromJson(personJson, Person.class);
            } else if(organizationJson != null){
                return gson.fromJson(organizationJson, Organization.class);
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

            String userJson;
            if(user instanceof Person) {
                userJson = gson.toJson((Person) user);
                if(userJson != null){
                    preferences.edit().putString(PERSON_DATA, userJson).apply();
                }

            } else if (user instanceof Organization) {
                userJson = gson.toJson((Organization) user);
                if(userJson != null){
                    preferences.edit().putString(ORGANIZATION_DATA, userJson).apply();

                }

            }
        }
    }

    @Override
    public boolean isUserActive() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }
}
