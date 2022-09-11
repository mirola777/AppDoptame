package com.appdoptame.appdoptame.data.repository;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LikeListener;
import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.model.Post;

public interface PostRepository {
    void getFeedPosts(PostListLoaderListener listener);
    void getPost(String ID, PostLoaderListener listener);
    void like(Post post, LikeListener listener);
    void createPost(Post post, CompleteListener listener);
    void verifyPostCreated(PostLoaderListener listener);
}
