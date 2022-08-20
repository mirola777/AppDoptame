package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.User;

public interface UserLoaderListener {
    void onSuccess(User user);
    void onFailure();
}
