package com.appdoptame.appdoptame.data.observer;

import com.appdoptame.appdoptame.data.listener.PostDeleterListener;
import com.appdoptame.appdoptame.data.listener.PostEditorListener;
import com.appdoptame.appdoptame.data.listener.PostInserterListener;
import com.appdoptame.appdoptame.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostObserver {
    private static List<PostEditorListener> postEditorListeners;
    private static List<PostDeleterListener> postDeleterListeners;
    private static List<PostInserterListener> postInserterListeners;

    private PostObserver(){
        postDeleterListeners   = new ArrayList<>();
        postEditorListeners    = new ArrayList<>();
        postInserterListeners  = new ArrayList<>();
    }

    public static void attachPostDeleterListener(PostDeleterListener listener){
        if(postDeleterListeners == null) new PostObserver();

        if(!postDeleterListeners.contains(listener))
            postDeleterListeners.add(listener);
    }

    public static void detachPostDeleterListener(PostDeleterListener listener){
        if(postDeleterListeners == null) new PostObserver();

        postDeleterListeners.remove(listener);
    }

    public static void notifyPostDeleted(String postID){
        if(postDeleterListeners == null) new PostObserver();

        for(PostDeleterListener listener: postDeleterListeners){
            listener.onDeleted(postID);
        }
    }

    public static void attachPostEditorListener(PostEditorListener listener){
        if(postEditorListeners == null) new PostObserver();

        if(!postEditorListeners.contains(listener))
            postEditorListeners.add(listener);
    }

    public static void detachPostEditorListener(PostEditorListener listener){
        if(postEditorListeners == null) new PostObserver();

        postEditorListeners.remove(listener);
    }

    public static void notifyPostEdited(Post post){
        if(postEditorListeners == null) new PostObserver();

        for(PostEditorListener listener: postEditorListeners){
            listener.onEdited(post);
        }
    }

    public static void attachPostInserterListener(PostInserterListener listener){
        if(postInserterListeners == null) new PostObserver();

        if(!postInserterListeners.contains(listener))
            postInserterListeners.add(listener);
    }

    public static void detachPostInserterListener(PostInserterListener listener){
        if(postInserterListeners == null) new PostObserver();

        postInserterListeners.remove(listener);
    }

    public static void notifyPostInserted(Post post){
        if(postInserterListeners == null) new PostObserver();

        for(PostInserterListener listener: postInserterListeners){
            listener.onInserted(post);
        }
    }
}