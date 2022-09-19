package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.PostRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PostListLoaderListener;
import com.appdoptame.appdoptame.data.observer.PostObserver;
import com.appdoptame.appdoptame.data.observer.UserObserver;
import com.appdoptame.appdoptame.data.parser.ParseUser;
import com.appdoptame.appdoptame.data.service.IUserEditor;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserEditorFS implements IUserEditor {
    private static final CollectionReference collectionUser = FirestoreDB.getCollectionUser();
    private static final CollectionReference collectionPost = FirestoreDB.getCollectionPost();
    private static final StorageReference    storageUser    = FirestoreDB.getStorageUser();

    @Override
    public void editUser(User user, byte[] userImage, CompleteListener listener) {
        if(userImage != null){
            updateUserImage(user, userImage, listener);
        } else {
            deleteUserImage(user, listener);
        }
    }

    private void updateUserImage(User user, byte[] userImage, CompleteListener listener){
        StorageReference referenceImage = storageUser.child(user.getID() + ".jpg");
        UploadTask uploadTask = referenceImage.putBytes(userImage);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) throw Objects.requireNonNull(task.getException());
            return referenceImage.getDownloadUrl();

        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String imageUrl = task.getResult().toString();
                user.setImage(imageUrl);
                updateUserData(user, listener);
            } else {
                listener.onFailure();
            }
        });
    }

    private void deleteUserImage(User user, CompleteListener listener){
        StorageReference referenceImage = storageUser.child(user.getID() + ".jpg");
        referenceImage.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.setImage(null);
                updateUserData(user, listener);
            } else {
                listener.onFailure();
            }
        });
    }

    private void updateUserData(User user, CompleteListener listener){
        Map<String, Object> doc = ParseUser.parse(user);
        collectionUser
                .document(user.getID())
                .update(doc)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        User userSession = UserRepositoryFS.getInstance().getUserSession();
                        if(user.getID().equals(userSession.getID())){
                            UserRepositoryFS.getInstance().saveUserSession(user);
                        }

                        UserObserver.notifyUserEdited(user);
                        PostRepositoryFS.getInstance().getUserPosts(user.getID(), new PostListLoaderListener() {
                            @Override
                            public void onSuccess(List<Post> posts) {
                                updatePosts(user, doc, posts, listener, 0);
                            }

                            @Override
                            public void onFailure() {
                                listener.onFailure();
                            }
                        });
                    } else {
                        listener.onFailure();
                    }
                });
    }

    private void updatePosts(User user, Map<String, Object> doc, List<Post> posts, CompleteListener listener, int counter){
        if(counter < posts.size()){
            Post post = posts.get(counter);
            post.setUser(user);

            collectionPost
                    .document(post.getID())
                    .update("PERSON", doc)
                    .addOnCompleteListener(petTask -> {
                        if(petTask.isSuccessful()){
                            PostObserver.notifyPostEdited(post);
                            updatePosts(user, doc, posts, listener, counter + 1);
                        } else {
                            listener.onFailure();
                        }
                    });
        } else {
            UserObserver.notifyUserEdited(user);
            listener.onSuccess();
        }
    }
}
