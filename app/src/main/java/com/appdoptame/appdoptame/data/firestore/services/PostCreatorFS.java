package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.data.parser.ParsePost;
import com.appdoptame.appdoptame.data.service.IPostCreator;
import com.appdoptame.appdoptame.model.Post;
import com.google.firebase.firestore.CollectionReference;

import java.util.Map;

public class PostCreatorFS implements IPostCreator {
    private static final CollectionReference collectionPost = FirestoreDB.getCollectionPost();

    @Override
    public void createPost(Post post, CompleteListener listener) {
        if(post.getPet() != null && post.getUser() != null) {
            String postID = collectionPost.document().getId();
            post.setID(postID);
            Map<String, Object> postDoc = ParsePost.parse(post);
            collectionPost.document(post.getID()).set(postDoc).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    listener.onSuccess();
                } else {
                    listener.onFailure();
                }
            });
        } else {
            listener.onFailure();
        }
    }

    @Override
    public void verifyPostCreated(PostLoaderListener listener) {

    }
}
