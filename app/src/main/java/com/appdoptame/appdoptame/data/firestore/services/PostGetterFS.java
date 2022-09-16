package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.data.parser.ParsePost;
import com.appdoptame.appdoptame.data.service.IPostGetter;
import com.appdoptame.appdoptame.model.Post;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class PostGetterFS implements IPostGetter {
    private static CollectionReference collectionPost = FirestoreDB.getCollectionPost();

    @Override
    public void getFeedPosts(PostListLoaderListener listener) {
        ArrayList<Post> posts = new ArrayList<>();
        collectionPost = FirestoreDB.getCollectionPost();
        collectionPost.orderBy("DATE", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    if(document.getData() != null){
                        posts.add(ParsePost.parse(document.getData()));
                    }
                }
                listener.onSuccess(posts);
            } else {
                listener.onFailure();
            }
        });
    }

    @Override
    public void getPost(String ID, PostLoaderListener listener) {

    }

    @Override
    public void getUserPosts(String userID, PostListLoaderListener listener) {
        List<Post> posts = new ArrayList<>();

        collectionPost.whereEqualTo("PERSON.ID", userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    if(document.getData() != null){
                        posts.add(ParsePost.parse(document.getData()));
                    }
                }
                listener.onSuccess(posts);
            } else {
                listener.onFailure();
            }
        });
    }
}
