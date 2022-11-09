package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.Comment;
import com.appdoptame.appdoptame.model.Message;

public interface CommentInserterListener {
    void onNewComment(Comment comment);
}
