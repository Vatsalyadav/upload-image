package com.vatsalyadav.uploadimage.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vatsalyadav.uploadimage.R;
import com.vatsalyadav.uploadimage.view.imageViewFragments.ImageListFragment;
import com.vatsalyadav.uploadimage.viewmodel.ImageActivityViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ImageActivity extends AppCompatActivity {
    public static final int CAMERA_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPLY = "selectedImageUri";
    private static final String TAG = "ImageActivity";
    private ImageActivityViewModel imageActivityViewModel;
    private Uri selectedImageUri;
    private ImageListFragment imageListFragment;
    private CompositeDisposable firebaseUploadDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageListFragment = new ImageListFragment();

        // Add Fragment to FrameLayout (flContainer), using FragmentManager
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.flContainer, imageListFragment);
        fragmentTransaction.commit();

        initDataBinding();
    }

    private void initDataBinding() {
        imageActivityViewModel = new ViewModelProvider(this).get(ImageActivityViewModel.class);
        imageActivityViewModel.init();
    }

    public void selectImage(final View view) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
        new MaterialAlertDialogBuilder(view.getContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Select image to upload")
                .setItems(options, (dialog, which) -> {
                    if (options[which].equals("Take Photo")) {
                        Context context = view.getContext();
                        Intent camera = new Intent(context, CameraActivity.class);
                        startActivityForResult(camera, CAMERA_ACTIVITY_REQUEST_CODE);
                    } else if (options[which].equals("Choose from Gallery")) {
                        pickAndCropImage();
                    }
                })
                .show();
    }

    public void pickAndCropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    public void cropImage(String imageUri) {
        CropImage.activity(Uri.parse(imageUri))
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    selectedImageUri = result.getUri();
                    uploadImage();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Problem while selecting image to upload, please try again", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == CAMERA_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    String savedImageUri = data.getStringExtra(EXTRA_REPLY);
                    cropImage(savedImageUri);
                }
            }
        } else {
            Toast.makeText(this, "Problem while selecting image to upload, please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage() {
        Single<String> firebaseUploadSingleObserver = imageActivityViewModel.uploadImage(selectedImageUri);
        firebaseUploadSingleObserver.subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                firebaseUploadDisposable.add(d);
            }

            @Override
            public void onSuccess(String s) {
                imageListFragment.getImagesList();
                Toast.makeText(ImageActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ImageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseUploadDisposable.clear();
    }
}