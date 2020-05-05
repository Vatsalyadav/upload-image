package com.vatsalyadav.uploadimage.viewmodel;

import android.net.Uri;

import com.vatsalyadav.uploadimage.repository.ImageRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImageActivityViewModel extends ViewModel {
    private static final String TAG = "ImageActivityViewModel";
    private ImageRepository mImageRepository;

    public void init() {
        mImageRepository = ImageRepository.getInstance();
        mImageRepository.setupFirebase();
    }

    public Single<String> uploadImage(Uri selectedImageUri) {
        return mImageRepository.insertImage(selectedImageUri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public LiveData<Object> getImagesList() {
        return mImageRepository.getImagesList();
    }

}
