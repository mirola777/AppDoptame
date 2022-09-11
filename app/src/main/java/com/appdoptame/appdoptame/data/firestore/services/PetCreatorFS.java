package com.appdoptame.appdoptame.data.firestore.services;

import android.net.Uri;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.PostRepositoryFS;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.data.parser.ParsePet;
import com.appdoptame.appdoptame.data.parser.ParseUser;
import com.appdoptame.appdoptame.data.service.IPetCreator;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.Post;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PetCreatorFS implements IPetCreator {
    private static final CollectionReference collectionPet = FirestoreDB.getCollectionPet();
    private static final StorageReference storagePet       = FirestoreDB.getStoragePet();

    @Override
    public void createPet(Pet pet, List<Uri> petImages, CompleteListener listener) {
        if(pet != null) {
            if(pet.getName().length()        > 0 &&
               pet.getType().length()        > 0 &&
               pet.getSex().length()         > 0 &&
               pet.getDescription().length() > 0 &&
               pet.getBreed().length()       > 0 &&
               pet.getAge()                  >=0 &&
               pet.getSize()                 > 0 &&
               pet.getWeight()               > 0 &&
               petImages.size()              > 0) {

                String petID = collectionPet.document().getId();
                pet.setID(petID);
                uploadPetImages(pet, petImages, 0, listener);

            } else {
                listener.onFailure();
            }
        } else {
            listener.onFailure();
        }
    }

    private void uploadPetData(Pet pet, CompleteListener listener){
        Map<String, Object> petDoc        = ParsePet.parse(pet);
        User owner                        = UserRepositoryFS.getInstance().getUserSession();
        Map<String, Object> userDoc       = ParseUser.parse(owner);
        petDoc.put("OWNER", userDoc);

        collectionPet.document(pet.getID()).set(petDoc).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                // Con la mascota creada, se crea un post automaticamente.
                Post post = new Post(owner, pet);
                PostRepositoryFS.getInstance().createPost(post, listener);
            } else {
                listener.onFailure();
            }
        });
    }

    private void uploadPetImages(Pet pet, List<Uri> petImages, int counter, CompleteListener listener){
        if(counter < petImages.size()){
            StorageReference referenceImage = storagePet.child(pet.getID() + counter + ".jpg");
            UploadTask uploadTask = referenceImage.putFile(petImages.get(counter));
            uploadTask.continueWithTask(task -> {

                if (!task.isSuccessful()) throw Objects.requireNonNull(task.getException());
                return referenceImage.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String imageUrl = task.getResult().toString();
                    pet.getImages().add(imageUrl);
                    uploadPetImages(pet, petImages, counter + 1, listener);
                } else {
                    listener.onFailure();
                }
            });

        } else {
            // YA TERMINO LA SUBIDA DE IMAGENES, SE SUBEN LOS DATOS DE LA MASCOTA ACA
           uploadPetData(pet, listener);
        }
    }

    @Override
    public void verifyPetCreated(PetLoaderListener listener) {

    }
}
