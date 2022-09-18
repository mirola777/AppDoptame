package com.appdoptame.appdoptame.data.repository;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LikeListener;
import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.model.Post;

import java.util.List;

public interface PostRepository {
    void getFeedPosts(PostListLoaderListener listener);
    void getPost(String ID, PostLoaderListener listener);
    void getUserPosts(String userID, PostListLoaderListener listener);
    void like(Post post, LikeListener listener);
    void createPost(Post post, CompleteListener listener);
    void verifyPostCreated(PostLoaderListener listener);
    void deletePost(Post post, CompleteListener listener);
    void updatePost(Post post, List<byte[]> postImages, CompleteListener listener);
}
