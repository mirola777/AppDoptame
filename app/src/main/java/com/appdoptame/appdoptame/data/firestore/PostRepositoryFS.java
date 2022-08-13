package com.appdoptame.appdoptame.data.firestore;

import com.appdoptame.appdoptame.data.firestore.services.PostGetterFS;
import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.data.repository.PostRepository;
import com.appdoptame.appdoptame.data.service.IPostGetter;

public class PostRepositoryFS implements PostRepository {
    private static PostRepositoryFS instance;
    private final IPostGetter iPostGetter;

    private PostRepositoryFS(){
        this.iPostGetter = new PostGetterFS();
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
}
