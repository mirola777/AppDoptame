package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.PetRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.parser.ParsePost;
import com.appdoptame.appdoptame.data.service.IPostEditor;
import com.appdoptame.appdoptame.model.Post;
import com.google.firebase.firestore.CollectionReference;

import java.util.List;
import java.util.Map;

public class PostEditorFS implements IPostEditor {
    private static final CollectionReference collectionPost = FirestoreDB.getCollectionPost();

    @Override
    public void updatePost(Post post, List<byte[]> petImages, CompleteListener listener) {
        Map<String, Object> doc = ParsePost.parse(post);

        collectionPost.document(post.getID()).update(doc).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                PetRepositoryFS.getInstance().updatePet(post.getPet(), petImages, listener);
            } else {
                listener.onFailure();
            }
        });
    }
}
