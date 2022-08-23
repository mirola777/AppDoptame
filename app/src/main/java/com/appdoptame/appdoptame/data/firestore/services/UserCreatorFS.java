package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LoginListener;
import com.appdoptame.appdoptame.data.parser.ParsePerson;
import com.appdoptame.appdoptame.data.service.IUserCreator;
import com.appdoptame.appdoptame.model.Organization;
import com.appdoptame.appdoptame.model.Person;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;
import java.util.Objects;

public class UserCreatorFS implements IUserCreator {
    @Override
    public void createPerson(Person person, CompleteListener listener) {
        if(person != null){
            if(person.getName().length()           > 0 &&
               person.getIdentification().length() > 0 &&
               person.getCity().length()           > 0 &&
               person.getDepartment().length()     > 0 &&
               person.getAge() >= 18){

                // Se genera una ID aleatoria para el nuevo usuario
                FirebaseAuth auth         = FirebaseAuth.getInstance();
                FirebaseUser userFirebase = auth.getCurrentUser();
                assert userFirebase != null;
                String userID             = userFirebase.getUid();
                person.setID(userID);
                // Se convierte a un Map, que es lo que se llevará a Firebase.
                Map<String, Object> doc = ParsePerson.parse(person);
                // Se llama la coleccion de usuarios
                // Se inserta en firebase
                CollectionReference collectionUser = FirestoreDB.getCollectionUser();
                collectionUser.document(userID).set(doc).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        // Se guarfa al usuario en el almacenamiento del celular
                        UserRepositoryFS.getInstance().saveUserSession(person);
                        listener.onSuccess();
                    } else {
                        listener.onFailure();
                    }
                });


            } else {
                listener.onFailure();
            }
        } else {
            listener.onFailure();
        }
    }

    @Override
    public void createOrganization(Organization organization, CompleteListener listener) {

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
                    User user            = ParsePerson.parse(Objects.requireNonNull(doc.getData()));
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
