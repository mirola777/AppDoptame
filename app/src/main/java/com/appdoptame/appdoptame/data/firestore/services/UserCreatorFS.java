package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LoginListener;
import com.appdoptame.appdoptame.data.parser.ParseUser;
import com.appdoptame.appdoptame.data.service.IUserCreator;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;
import java.util.Objects;

public class UserCreatorFS implements IUserCreator {
    private static final StorageReference storageUser       = FirestoreDB.getStorageUser();
    private static final CollectionReference collectionUser = FirestoreDB.getCollectionUser();

    @Override
    public void createUser(User user, byte[] userImage, CompleteListener listener) {
        if(user != null){
            if(user.getName().length()           > 0 &&
               user.getIdentification().length() > 0 &&
               user.getCity().length()           > 0 &&
               user.getDepartment().length()     > 0 &&
               user.getAge() >= 18){

                // Se genera una ID aleatoria para el nuevo usuario
                FirebaseAuth auth         = FirebaseAuth.getInstance();
                FirebaseUser userFirebase = auth.getCurrentUser();
                assert userFirebase != null;
                String userID             = userFirebase.getUid();
                user.setID(userID);

                if(userImage != null){
                    uploadUserImage(user, userImage, listener);
                } else {
                    uploadUserData(user, listener);
                }

            } else {
                listener.onFailure();
            }
        } else {
            listener.onFailure();
        }
    }

    private void uploadUserData(User user, CompleteListener listener){
        // Se convierte a un Map, que es lo que se llevará a Firebase.
        Map<String, Object> doc = ParseUser.parse(user);
        // Se llama la coleccion de usuarios
        // Se inserta en firebase
        collectionUser.document(user.getID()).set(doc).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                // Se guarfa al usuario en el almacenamiento del celular
                UserRepositoryFS.getInstance().saveUserSession(user);
                listener.onSuccess();
            } else {
                listener.onFailure();
            }
        });
    }

    private void uploadUserImage(User user, byte[] userImage, CompleteListener listener){
        StorageReference referenceImage = storageUser.child(user.getID() + ".jpg");
        UploadTask uploadTask = referenceImage.putBytes(userImage);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) throw Objects.requireNonNull(task.getException());
            return referenceImage.getDownloadUrl();

        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String imageUrl = task.getResult().toString();
                user.setImage(imageUrl);
                uploadUserData(user, listener);
            } else {
                listener.onFailure();
            }
        });
    }

    @Override
    public void verifyProfileCreated(LoginListener listener) {
        // A este punto el usuario si se encuentra registrado en la APP
        // Ahora se debe ver si ya creo su perfil de usuario
        FirebaseAuth auth                  = FirebaseAuth.getInstance();
        CollectionReference collectionUser = FirestoreDB.getCollectionUser();
        FirebaseUser firebaseUser          = auth.getCurrentUser();
        assert firebaseUser != null;
        String userID                      = firebaseUser.getUid();

        // Se busca al usuario en Firestore
        collectionUser.document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if(task.getResult().exists()){
                    // Se guarfa al usuario en el almacenamiento del celular
                    DocumentSnapshot doc = task.getResult();
                    User user            = ParseUser.parse(Objects.requireNonNull(doc.getData()));
                    UserRepositoryFS.getInstance().saveUserSession(user);
                    listener.onSuccess();
                } else {
                    listener.onNewAccount();
                }
            } else {
                listener.onFailure();
            }
        });
    }
}
