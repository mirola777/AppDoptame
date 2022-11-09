package com.appdoptame.appdoptame.data.observer;

import com.appdoptame.appdoptame.data.listener.CommentInserterListener;
import com.appdoptame.appdoptame.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentObserver {
    private static List<CommentInserterListener> commentInserterListeners;

    private CommentObserver(){
        commentInserterListeners = new ArrayList<>();
    }

    public static void attachCommentInserterListener(CommentInserterListener listener){
        if(commentInserterListeners == null) new CommentObserver();

        if(!commentInserterListeners.contains(listener))
            commentInserterListeners.add(listener);
    }

    public static void detachCommentInserterListener(CommentInserterListener listener){
        if(commentInserterListeners == null) new CommentObserver();

        commentInserterListeners.remove(listener);
    }

    public static void notifyNewComment(Comment comment){
        if(commentInserterListeners == null) new CommentObserver();

        for(CommentInserterListener listener: commentInserterListeners){
            listener.onNewComment(comment);
        }
    }
}
