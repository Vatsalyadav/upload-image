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
import com.vatsalyadav.uploadimage.viewmodel.ImageActivityViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class ImageActivity extends AppCompatActivity {
    public static final int CAMERA_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPLY = "selectedImageUri";
    private static final String TAG = "ImageActivity";
    private ImageActivityViewModel imageActivityViewModel;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
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
                    imageActivityViewModel.insertImage(selectedImageUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Problem while cropping image, please try again", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == CAMERA_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    String savedImageUri = data.getStringExtra(EXTRA_REPLY);
                    cropImage(savedImageUri);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Problem while cropping image, please try again", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}