package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.observer.CommentObserver;
import com.appdoptame.appdoptame.data.parser.ParseComment;
import com.appdoptame.appdoptame.data.service.ICommentCreator;
import com.appdoptame.appdoptame.model.Comment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;

import java.util.Map;

public class CommentCreatorFS implements ICommentCreator {
    private final static  CollectionReference collectionPost = FirestoreDB.getCollectionPost();

    @Override
    public void createComment(Comment comment, CompleteListener listener) {
        Map<String, Object> doc = ParseComment.parse(comment);

        collectionPost.document(comment.getPostID())
                .update("COMMENTS", FieldValue.arrayUnion(doc))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        CommentObserver.notifyNewComment(comment);
                        listener.onSuccess();
                    } else {
                        listener.onFailure();
                    }
                });
    }
}
