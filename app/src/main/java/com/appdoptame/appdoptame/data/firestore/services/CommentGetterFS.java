package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CommentListLoaderListener;
import com.appdoptame.appdoptame.data.parser.ParseComment;
import com.appdoptame.appdoptame.data.service.ICommentGetter;
import com.appdoptame.appdoptame.model.Comment;
import com.google.firebase.firestore.CollectionReference;

import java.util.List;
import java.util.Map;

public class CommentGetterFS implements ICommentGetter {
    private final static CollectionReference collectionPost = FirestoreDB.getCollectionPost();

    @Override
    public void getPostComments(String postID, CommentListLoaderListener listener) {
        collectionPost.document(postID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Map<String,Object> postDoc            = task.getResult().getData();
                List<Map<String, Object>> commentsDoc = (List<Map<String, Object>>) postDoc.get("COMMENTS");
                List<Comment> comments                = ParseComment.parseDocList(commentsDoc);

                listener.onSuccess(comments);
            } else {
                listener.onFailure();
            }
        });
    }
}
