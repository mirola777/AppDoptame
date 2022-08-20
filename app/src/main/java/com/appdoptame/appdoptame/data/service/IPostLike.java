package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.LikeListener;

public interface IPostLike {
    void like(String postID, LikeListener listener);
}
