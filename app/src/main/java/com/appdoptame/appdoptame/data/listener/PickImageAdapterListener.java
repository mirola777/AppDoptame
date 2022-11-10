package com.appdoptame.appdoptame.data.listener;

public interface PickImageAdapterListener {
    void onImageDeleted(int imagesCount);
    void onImagesDeleted();
    void onImageAdded(int   imagesCount);
    void onImagesAdded(int  imagesCount);
    void onAdd();
}
