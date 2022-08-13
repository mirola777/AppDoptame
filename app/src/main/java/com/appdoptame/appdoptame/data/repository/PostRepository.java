package com.appdoptame.appdoptame.data.repository;

import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;

public interface PostRepository {
    void getFeedPosts(PostListLoaderListener listener);
    void getPost(String ID, PostLoaderListener listener);
}
