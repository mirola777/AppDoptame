package com.appdoptame.appdoptame.data.listener;

import com.appdoptame.appdoptame.model.Comment;

import java.util.List;

public interface CommentListLoaderListener {
    void onSuccess(List<Comment> comments);
    void onFailure();
}
