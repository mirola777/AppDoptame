package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.PostLoaderListener;
import com.appdoptame.appdoptame.model.Post;

public interface IPostCreator {
    void createPet(Post post, CompleteListener listener);
    void verifyPetCreated(PostLoaderListener listener);
}
