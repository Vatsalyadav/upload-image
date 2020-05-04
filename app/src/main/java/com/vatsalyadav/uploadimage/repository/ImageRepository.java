package com.vatsalyadav.uploadimage.repository;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vatsalyadav.uploadimage.model.ImageDetails;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ImageRepository {

    private static ImageRepository instance;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    public static ImageRepository getInstance() {
        if (instance == null) {
            instance = new ImageRepository();
        }
        return instance;
    }

    private List<ImageDetails> imageDetailsList;


//    public MutableLiveData<List<String>> getImageList() {
//        MutableLiveData<List<String>> data = new MutableLiveData<>();
//        return data;
//    }

    public void setupFirebase() {
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
    }

    public LiveData<Object> getImagesList() {
        imageDetailsList = new ArrayList<>();
        return LiveDataReactiveStreams.fromPublisher(
                Flowable.create(emitter -> {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot imagesSnapshot : dataSnapshot.getChildren()) {
                                ImageDetails imageDetails = imagesSnapshot.getValue(ImageDetails.class);
                                imageDetailsList.add(imageDetails);
                                Log.e("Repository", imageDetails.getImageUrl());

                            }
                            emitter.onNext(imageDetailsList);
                            emitter.onComplete();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            emitter.isCancelled();
                        }
                    });
                }, BackpressureStrategy.BUFFER)
                        .subscribeOn(Schedulers.io())
        );
    }

    public Single<String> insertImage(Uri selectedImageUri) {
        return Single.create(emitter -> {
            if (selectedImageUri != null) {
                String imageName = System.currentTimeMillis() + "uploadedImage";
                StorageReference fileRef = storageReference.child(imageName);
                fileRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                ImageDetails imageDetails = new ImageDetails(imageName, uri.toString());
                                String uploadId = databaseReference.push().getKey();
                                databaseReference.child(uploadId).setValue(imageDetails);
                                emitter.onSuccess("Image Uploaded");
                            }
                        });
                    }
                })
                        .addOnFailureListener(e -> {
                            emitter.onError(new Throwable("Error uploading image"));
                        });
            } else {
                emitter.onError(new Throwable("Error uploading image"));
            }
        });
    }
}