package com.appdoptame.appdoptame.data.firestore.services;

import androidx.annotation.NonNull;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.LikeListener;
import com.appdoptame.appdoptame.data.listener.UserLoaderListener;
import com.appdoptame.appdoptame.data.service.IPostLike;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;

public class PostLikeFS implements IPostLike {
    @Override
    public void like(Post post, LikeListener listener) {
        User user = UserRepositoryFS.getInstance().getUserSession();
        CollectionReference collectionPost = FirestoreDB.getCollectionPost();

        if(post.getLikes().contains(user.getID())){
            // Ya hizo like, se hace el dislike
            post.getLikes().remove(user.getID());
            listener.onDislike();
            collectionPost.document(post.getID())
                    .update("LIKES", FieldValue.arrayRemove(user.getID()))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            listener.onDislike();
                        } else {
                            post.getLikes().add(user.getID());
                            listener.onFailure();
                        }
                    });
        } else {
            // No ha hecho like, se hace el like
            post.getLikes().add(user.getID());
            listener.onLike();
            collectionPost.document(post.getID())
                    .update("LIKES", FieldValue.arrayUnion(user.getID()))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            listener.onLike();
                        } else {
                            post.getLikes().remove(user.getID());
                            listener.onFailure();
                        }
                    });
        }
    }
}
