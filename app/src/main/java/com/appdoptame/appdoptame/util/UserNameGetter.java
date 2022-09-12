package com.appdoptame.appdoptame.util;

import com.appdoptame.appdoptame.model.User;

public class UserNameGetter {
    public static String get(User user){
        if(user.getLastName() == null){
            return user.getName();
        }

        return user.getName() + " " + user.getLastName();
    }
}
