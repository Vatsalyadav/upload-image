package com.vatsalyadav.uploadimage.view.imageViewFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vatsalyadav.uploadimage.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ImageListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: RecyclerView for Images grid
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_list, parent, false);
    }

}