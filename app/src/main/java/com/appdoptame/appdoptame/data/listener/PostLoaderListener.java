package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.Post;

import java.util.List;

public interface PostLoaderListener {
    void onSuccess(Post post);
    void onFailure();
}
