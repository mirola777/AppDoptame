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

public class PostGetterFS implements IPostGetter {
    @Override
    public void getFeedPosts(PostListLoaderListener listener) {
        ArrayList<Post> posts = new ArrayList<>();
        CollectionReference collectionPost = FirestoreDB.getCollectionPost();
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
}
