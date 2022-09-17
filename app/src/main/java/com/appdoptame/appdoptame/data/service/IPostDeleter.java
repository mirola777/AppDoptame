package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Post;

public interface IPostDeleter {
    void deletePost(Post post, CompleteListener listener);
}
