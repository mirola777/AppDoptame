package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.Post;

import java.util.List;

public interface PostListLoaderListener {
    void onSuccess(List<Post> posts);
    void onFailure();
}
