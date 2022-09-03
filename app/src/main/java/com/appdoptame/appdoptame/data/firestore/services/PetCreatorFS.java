package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.firestore.UserRepositoryFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.data.parser.ParsePerson;
import com.appdoptame.appdoptame.data.parser.ParsePet;
import com.appdoptame.appdoptame.data.service.IPetCreator;
import com.appdoptame.appdoptame.model.Person;
import com.appdoptame.appdoptame.model.Pet;
import com.appdoptame.appdoptame.model.User;
import com.google.firebase.firestore.CollectionReference;

import java.util.Map;

public class PetCreatorFS implements IPetCreator {

    @Override
    public void createPet(Pet pet, CompleteListener listener) {
        if(pet != null) {
            if(pet.getName().length()        > 0 &&
               pet.getType().length()        > 0 &&
               pet.getSex().length()         > 0 &&
               pet.getDescription().length() > 0 &&
               pet.getBreed().length()       > 0 &&
               pet.getAge()                  >=0 &&
               pet.getSize()                 > 0 &&
               pet.getWeight()               > 0// &&
               /*pet.getImages().size()        > 0*/) {

                CollectionReference collectionPet = FirestoreDB.getCollectionPet();
                String petID                      = collectionPet.document().getId();
                pet.setID(petID);                  // Asignar ID unica desde firestore
                Map<String, Object> petDoc        = ParsePet.parse(pet);
                Person owner                      = (Person) UserRepositoryFS.getInstance().getUserSession();
                Map<String, Object> userDoc       = ParsePerson.parse(owner);
                petDoc.put("OWNER", userDoc);

                collectionPet.document(petID).set(petDoc).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        // Mascota creada, ahora a crear el post
                        System.out.println(petID);



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
    public void verifyPetCreated(PetLoaderListener listener) {

    }
}
