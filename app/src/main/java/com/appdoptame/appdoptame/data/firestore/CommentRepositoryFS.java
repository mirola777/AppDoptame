package com.appdoptame.appdoptame.data.firestore;

import com.appdoptame.appdoptame.data.firestore.services.CommentCreatorFS;
import com.appdoptame.appdoptame.data.firestore.services.CommentGetterFS;
import com.appdoptame.appdoptame.data.listener.CommentListLoaderListener;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.repository.CommentRepository;
import com.appdoptame.appdoptame.data.service.ICommentCreator;
import com.appdoptame.appdoptame.data.service.ICommentGetter;
import com.appdoptame.appdoptame.model.Comment;

public class CommentRepositoryFS implements CommentRepository {
    private static CommentRepositoryFS instance;
    private final ICommentCreator iCommentCreator;
    private final ICommentGetter  iCommentGetter;

    private CommentRepositoryFS() {
        this.iCommentGetter  = new CommentGetterFS();
        this.iCommentCreator = new CommentCreatorFS();
    }

    public static CommentRepositoryFS getInstance(){
        if(instance == null){
            instance = new CommentRepositoryFS();
        }

        return instance;
    }

    @Override
    public void createComment(Comment comment, CompleteListener listener) {
        iCommentCreator.createComment(comment, listener);
    }

    @Override
    public void getPostComments(String postID, CommentListLoaderListener listener) {
        iCommentGetter.getPostComments(postID, listener);
    }
}
