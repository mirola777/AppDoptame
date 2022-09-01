package com.appdoptame.appdoptame.data.firestore.services;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.data.service.IPetCreator;
import com.appdoptame.appdoptame.model.Pet;
import com.google.firebase.auth.FirebaseAuth;

public class PetCreatorFS implements IPetCreator {

    @Override
    public void createPet(Pet pet, CompleteListener listener) {
        if(pet != null) {
            if(pet.getID().length()             > 0 &&
               pet.getName().length()           > 0 &&
               pet.getType().length()           > 0 &&
               pet.getSex().length()            > 0 &&
               pet.getDescription().length()    > 0 &&
               pet.getBreed().length()          > 0 &&

               pet.getAge()                     > -1 &&
               pet.getSize()                    > 0 &&
               pet.getWeight()                  > 0 &&
               pet.getImages().size()           > 0) {

                FirebaseAuth auth         = FirebaseAuth.getInstance();

            }
        }
    }

    @Override
    public void verifyPetCreated(PetLoaderListener listener) {

    }
}