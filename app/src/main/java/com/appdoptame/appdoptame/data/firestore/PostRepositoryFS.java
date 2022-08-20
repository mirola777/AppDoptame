package com.appdoptame.appdoptame.data.firestore;

import com.appdoptame.appdoptame.data.firestore.services.PostGetterFS;
import com.appdoptame.appdoptame.data.firestore.services.PostLikeFS;
import com.appdoptame.appdoptame.data.listener.LikeListener;
import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.data.repository.PostRepository;
import com.appdoptame.appdoptame.data.service.IPostGetter;
import com.appdoptame.appdoptame.data.service.IPostLike;

public class PostRepositoryFS implements PostRepository {
    private static PostRepositoryFS instance;
    private final IPostGetter iPostGetter;
    private final IPostLike iPostLike;

    private PostRepositoryFS(){
        this.iPostGetter = new PostGetterFS();
        this.iPostLike   = new PostLikeFS();
    }

    public static PostRepositoryFS getInstance(){
        if(instance == null){
            instance = new PostRepositoryFS();
        }

        return instance;
    }

    @Override
    public void getFeedPosts(PostListLoaderListener listener) {
        iPostGetter.getFeedPosts(listener);
    }

    @Override
    public void getPost(String ID, PostLoaderListener listener) {
        iPostGetter.getPost(ID, listener);
    }

    @Override
    public void like(String ID, LikeListener listener) {
        iPostLike.like(ID, listener);
    }
}
