package com.vatsalyadav.uploadimage.repository;

public class ImageRepository {

    private static ImageRepository instance;

    public static ImageRepository getInstance() {
        if (instance == null) {
            instance = new ImageRepository();
        }
        return instance;
    }

    // TODO: Upload image to Firebase
    // TODO: Fetch images from Firebase
}