package com.appdoptame.appdoptame.data.firestore;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreDB {
    private static CollectionReference collectionPost;
    private static CollectionReference collectionPet;
    private static CollectionReference collectionUser;

    private FirestoreDB(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        collectionPost = database.collection("post");
        collectionPet  = database.collection("pet");
        collectionUser = database.collection("user");
    }

    public static CollectionReference getCollectionPost(){
        if(collectionPost == null) new FirestoreDB();

        return collectionPost;
    }

    public static CollectionReference getCollectionPet(){
        if(collectionPet == null) new FirestoreDB();

        return collectionPet;
    }

    public static CollectionReference getCollectionUser(){
        if(collectionUser == null) new FirestoreDB();

        return collectionUser;
    }
}
