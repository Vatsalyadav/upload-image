package com.vatsalyadav.uploadimage.viewmodel;

import android.net.Uri;

import com.vatsalyadav.uploadimage.repository.ImageRepository;

import androidx.lifecycle.ViewModel;

public class ImageActivityViewModel extends ViewModel {
    private static final String TAG = "ImageActivityViewModel";
    private ImageRepository mImageRepository;

    public void init() {
        mImageRepository = ImageRepository.getInstance();
    }

    public void insertImage(Uri selectedImageUri) {
        mImageRepository.insertImage(selectedImageUri);
    }
}
