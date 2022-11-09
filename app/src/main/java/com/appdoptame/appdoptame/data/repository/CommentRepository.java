package com.appdoptame.appdoptame.data.repository;

import com.appdoptame.appdoptame.data.listener.CommentListLoaderListener;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Comment;

public interface CommentRepository {
    void createComment(Comment comment, CompleteListener listener);
    void getPostComments(String postID, CommentListLoaderListener listener);
}
