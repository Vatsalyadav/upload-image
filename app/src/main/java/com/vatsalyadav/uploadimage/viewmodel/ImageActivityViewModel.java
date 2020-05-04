package com.vatsalyadav.uploadimage.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.vatsalyadav.uploadimage.repository.ImageRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ImageActivityViewModel extends AndroidViewModel {
    private static final String TAG = "ImageActivityViewModel";
    private ImageRepository mImageRepository;
    private CompositeDisposable firebaseUploadDisposable = new CompositeDisposable();
    private Context context = getApplication().getApplicationContext();

    public ImageActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        mImageRepository = ImageRepository.getInstance();
        mImageRepository.setupFirebase();
    }

    public void uploadImage(Uri selectedImageUri) {
        Single<String> firebaseUploadSingleObserver = mImageRepository.insertImage(selectedImageUri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        firebaseUploadSingleObserver.subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                firebaseUploadDisposable.add(d);
            }

            @Override
            public void onSuccess(String s) {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public LiveData<Object> getImagesList() {
        return mImageRepository.getImagesList();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        firebaseUploadDisposable.clear();
    }
}
