package com.appdoptame.appdoptame.data.firestore;

import com.appdoptame.appdoptame.data.firestore.services.PostCreatorFS;
import com.appdoptame.appdoptame.data.firestore.services.PostDeleterFS;
import com.appdoptame.appdoptame.data.firestore.services.PostEditorFS;
import com.appdoptame.appdoptame.data.firestore.services.PostGetterFS;
import com.appdoptame.appdoptame.data.firestore.services.PostLikeFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LikeListener;
import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.data.repository.PostRepository;
import com.appdoptame.appdoptame.data.service.IPostCreator;
import com.appdoptame.appdoptame.data.service.IPostDeleter;
import com.appdoptame.appdoptame.data.service.IPostEditor;
import com.appdoptame.appdoptame.data.service.IPostGetter;
import com.appdoptame.appdoptame.data.service.IPostLike;
import com.appdoptame.appdoptame.model.Post;

import java.util.List;

public class PostRepositoryFS implements PostRepository {
    private static PostRepositoryFS instance;
    private final IPostGetter  iPostGetter;
    private final IPostLike    iPostLike;
    private final IPostCreator iPostCreator;
    private final IPostDeleter iPostDeleter;
    private final IPostEditor  iPostEditor;

    private PostRepositoryFS(){
        this.iPostGetter  = new PostGetterFS();
        this.iPostLike    = new PostLikeFS();
        this.iPostCreator = new PostCreatorFS();
        this.iPostDeleter = new PostDeleterFS();
        this.iPostEditor  = new PostEditorFS();
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
    public void getUserPosts(String userID, PostListLoaderListener listener) {
        iPostGetter.getUserPosts(userID, listener);
    }

    @Override
    public void like(Post post, LikeListener listener) {
        iPostLike.like(post, listener);
    }

    @Override
    public void createPost(Post post, CompleteListener listener) {
        iPostCreator.createPost(post, listener);
    }

    @Override
    public void verifyPostCreated(PostLoaderListener listener) {
        iPostCreator.verifyPostCreated(listener);
    }

    @Override
    public void deletePost(Post post, CompleteListener listener) {
        iPostDeleter.deletePost(post, listener);
    }

    @Override
    public void updatePost(Post post, List<byte[]> postImages, CompleteListener listener) {
        iPostEditor.updatePost(post, postImages, listener);
    }
}
