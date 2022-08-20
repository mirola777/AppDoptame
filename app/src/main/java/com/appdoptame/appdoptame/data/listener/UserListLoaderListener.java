package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.User;

import java.util.List;

public interface UserListLoaderListener {
    void onSuccess(List<User> users);
    void onFailure();
}
