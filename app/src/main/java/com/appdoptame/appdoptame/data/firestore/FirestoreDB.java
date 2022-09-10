package com.appdoptame.appdoptame.data.firestore;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirestoreDB {
    private static CollectionReference collectionPost;
    private static CollectionReference collectionPet;
    private static CollectionReference collectionUser;
    private static StorageReference    storagePet;
    private static StorageReference    storageUser;

    private FirestoreDB(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseStorage storage    = FirebaseStorage.getInstance();
        collectionPost = database.collection("post");
        collectionPet  = database.collection("pet");
        collectionUser = database.collection("user");
        storagePet     = storage.getReference().child("pet/");
        storageUser    = storage.getReference().child("user/");
    }

    public static StorageReference getStorageUser(){
        if(storageUser == null) new FirestoreDB();

        return storageUser;
    }

    public static StorageReference getStoragePet(){
        if(storagePet == null) new FirestoreDB();

        return storagePet;
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
