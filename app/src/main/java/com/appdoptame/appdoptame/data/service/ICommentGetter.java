package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CommentListLoaderListener;

public interface ICommentGetter {
    void getPostComments(String postID, CommentListLoaderListener listener);
}
