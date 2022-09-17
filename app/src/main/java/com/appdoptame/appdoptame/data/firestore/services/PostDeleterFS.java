package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.PetRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.service.IPostDeleter;
import com.appdoptame.appdoptame.model.Post;
import com.google.firebase.firestore.CollectionReference;

public class PostDeleterFS implements IPostDeleter {
    private static final CollectionReference collectionPost = FirestoreDB.getCollectionPost();

    @Override
    public void deletePost(Post post, CompleteListener listener) {
        collectionPost.document(post.getID()).delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                PetRepositoryFS.getInstance().deletePet(post.getPet(), listener);
            } else {
                listener.onFailure();
            }
        });
    }
}
