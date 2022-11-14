package com.appdoptame.appdoptame.data.firestore;

import com.appdoptame.appdoptame.data.firestore.services.PetCreatorFS;
import com.appdoptame.appdoptame.data.firestore.services.PetDeleterFS;
import com.appdoptame.appdoptame.data.firestore.services.PetEditorFS;
import com.appdoptame.appdoptame.data.firestore.services.PetGetterFS;
import com.appdoptame.appdoptame.data.firestore.services.PetPredictorFS;
import com.appdoptame.appdoptame.data.firestore.services.PetRecommendFS;
import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PetListLoaderListener;
import com.appdoptame.appdoptame.data.listener.PetLoaderListener;
import com.appdoptame.appdoptame.data.listener.PetPredictorListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.data.repository.PetRepository;
import com.appdoptame.appdoptame.data.service.IPetCreator;
import com.appdoptame.appdoptame.data.service.IPetDeleter;
import com.appdoptame.appdoptame.data.service.IPetEditor;
import com.appdoptame.appdoptame.data.service.IPetGetter;
import com.appdoptame.appdoptame.data.service.IPetPredictor;
import com.appdoptame.appdoptame.data.service.IPetRecommend;
import com.appdoptame.appdoptame.model.Pet;

import java.util.List;

public class PetRepositoryFS implements PetRepository {
    private static PetRepositoryFS instance;
    private final IPetGetter    iPetGetter;
    private final IPetCreator   iPetCreator;
    private final IPetEditor    iPetEditor;
    private final IPetDeleter   iPetDeleter;
    private final IPetPredictor iPetPredictor;
    private final IPetRecommend iPetRecommend;

    private PetRepositoryFS() {
        this.iPetGetter    = new PetGetterFS();
        this.iPetCreator   = new PetCreatorFS();
        this.iPetEditor    = new PetEditorFS();
        this.iPetDeleter   = new PetDeleterFS();
        this.iPetPredictor = new PetPredictorFS();
        this.iPetRecommend = new PetRecommendFS();
    }

    public static PetRepositoryFS getInstance(){
        if(instance == null){
            instance = new PetRepositoryFS();
        }

        return instance;
    }

    @Override
    public void getPet(String ID, PetLoaderListener listener) {
        iPetGetter.getPet(ID, listener);
    }

    @Override
    public void getUserPets(String userID, PetListLoaderListener listener) {
        iPetGetter.getUserPets(userID, listener);
    }

    @Override
    public void createPet(Pet pet, List<byte[]> petImages, CompleteListener listener) {
        iPetCreator.createPet(pet, petImages, listener);
    }

    @Override
    public void updatePet(Pet pet, List<byte[]> petImages, CompleteListener listener) {
        iPetEditor.updatePet(pet, petImages, listener);
    }

    @Override
    public void changeState(Pet pet, CompleteListener listener) {
        iPetEditor.changeState(pet, listener);
    }

    @Override
    public void deletePet(Pet pet, CompleteListener listener) {
        iPetDeleter.deletePet(pet, listener);
    }

    @Override
    public void predictRace(byte[] image, PetPredictorListener listener) {
        iPetPredictor.predictRace(image, listener);
    }

    @Override
    public void recommend(PostLoaderListener listener) {
        iPetRecommend.recommend(listener);
    }
}
