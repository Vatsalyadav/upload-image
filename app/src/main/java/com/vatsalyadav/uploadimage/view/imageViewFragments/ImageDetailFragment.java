package com.vatsalyadav.uploadimage.view.imageViewFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.vatsalyadav.uploadimage.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class ImageDetailFragment extends Fragment {
    private String selectedImageUrlForFullscreen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        if (getArguments() != null) {
            selectedImageUrlForFullscreen = getArguments().getString("position", "");
            Log.e("onCreate: ", selectedImageUrlForFullscreen);
        } else Log.e("onCreate: ", "failed");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_detail, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Circular Progress Drawable to show while Glide loads image
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(view.getContext());
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(48f);
        circularProgressDrawable.setColorSchemeColors(Color.BLACK);
        circularProgressDrawable.start();

        // Use PhotoView image view for in built photo zoom functions
        PhotoView nasaPictureView = (PhotoView) view.findViewById(R.id.nasa_picture);
        Log.e("before glide: ", selectedImageUrlForFullscreen);
        // Set the image using Glide library
        Glide.with(view.getContext())
                .load(selectedImageUrlForFullscreen)
                .apply(new RequestOptions()
                        .placeholder(circularProgressDrawable))
                .apply(new RequestOptions()
                        .fitCenter())
                .apply(new RequestOptions()
                        .error(R.drawable.ic_broken_image))
                .into(nasaPictureView);
    }

}
