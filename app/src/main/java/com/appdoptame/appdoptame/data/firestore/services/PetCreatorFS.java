package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.firestore.FirestoreDB;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.data.parser.ParsePet;
import com.appdoptame.appdoptame.data.service.IPetCreator;
import com.appdoptame.appdoptame.model.Pet;
import com.google.firebase.firestore.CollectionReference;

import java.util.Map;

public class PetCreatorFS implements IPetCreator {

    @Override
    public void createPet(Pet pet, CompleteListener listener) {
        if(pet != null) {
            if(pet.getName().length()           > 0 &&
               pet.getType().length()           > 0 &&
               pet.getSex().length()            > 0 &&
               pet.getDescription().length()    > 0 &&
               pet.getBreed().length()          > 0 &&

               pet.getAge()                     > -1 &&
               pet.getSize()                    > 0 &&
               pet.getWeight()                  > 0 &&
               pet.getImages().size()           > 0) {

                //FirebaseAuth auth         = FirebaseAuth.getInstance();

                //FirebaseUser userFirebase = auth.getCurrentUser();
                //String userID             = userFirebase.getUid();

                Map<String, Object> doc = ParsePet.parse(pet);

                CollectionReference collectionPet = FirestoreDB.getCollectionPet();
                collectionPet.add(pet);
                /*.document().set(doc).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        // Se guarfa al usuario en el almacenamiento del celular
                        PetRepositoryFS.getInstance();
                        listener.onSuccess();
                    } else {
                        listener.onFailure();
                    }
                });*/

            }
        }
    }

    @Override
    public void verifyPetCreated(PetLoaderListener listener) {

    }
}
