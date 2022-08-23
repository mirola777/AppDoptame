package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.LikeListener;
import com.appdoptame.appdoptame.model.Post;

public interface IPostLike {
    void like(Post post, LikeListener listener);
}
