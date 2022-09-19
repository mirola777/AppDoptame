package com.appdoptame.appdoptame.data.observer;

import com.appdoptame.appdoptame.data.listener.UserEditorListener;
import com.appdoptame.appdoptame.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserObserver {
    private static List<UserEditorListener> userEditorListeners;

    private UserObserver(){
        userEditorListeners    = new ArrayList<>();
    }

    public static void attachUserEditorListener(UserEditorListener listener){
        if(userEditorListeners == null) new UserObserver();

        if(!userEditorListeners.contains(listener))
            userEditorListeners.add(listener);
    }

    public static void detachUserEditorListener(UserEditorListener listener){
        if(userEditorListeners == null) new UserObserver();

        userEditorListeners.remove(listener);
    }

    public static void notifyUserEdited(User user){
        if(userEditorListeners == null) new UserObserver();

        for(UserEditorListener listener: userEditorListeners){
            listener.onUserEdited(user);
        }
    }
}
