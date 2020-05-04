package com.vatsalyadav.uploadimage.repository;

import android.net.Uri;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class ImageRepository {

    private static ImageRepository instance;

    public static ImageRepository getInstance() {
        if (instance == null) {
            instance = new ImageRepository();
        }
        return instance;
    }


    public MutableLiveData<List<String>> getImageList() {
        MutableLiveData<List<String>> data = new MutableLiveData<>();
        return data;
    }

    public void insertImage(Uri selectedImageUri) {
        Log.e("Repository", "insertImage: " + selectedImageUri);
    }
}