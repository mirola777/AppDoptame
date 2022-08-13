package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;

public interface IPostGetter {
    void getFeedPosts(PostListLoaderListener listener);
    void getPost(String ID, PostLoaderListener listener);
}
