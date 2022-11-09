package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.model.Comment;

public interface ICommentCreator {
    void createComment(Comment comment, CompleteListener listener);
}
