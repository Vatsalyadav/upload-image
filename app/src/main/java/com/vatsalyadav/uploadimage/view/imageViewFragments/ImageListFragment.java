package com.vatsalyadav.uploadimage.view.imageViewFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vatsalyadav.uploadimage.R;
import com.vatsalyadav.uploadimage.adapter.ImageGridRecyclerAdapter;
import com.vatsalyadav.uploadimage.model.ImageDetails;
import com.vatsalyadav.uploadimage.viewmodel.ImageActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ImageGridRecyclerAdapter imageGridRecyclerAdapter;
    private ImageActivityViewModel imageActivityViewModel;
    private List<ImageDetails> mLiveDataOutput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLiveDataOutput = new ArrayList<>();
        initDataBinding();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_list, parent, false);
        getImagesList();
        initRecyclerView(rootView);
        return rootView;
    }

    private void initDataBinding() {
        imageActivityViewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ImageActivityViewModel.class);
        imageActivityViewModel.init();
    }

    // Initialize RecyclerView to display images in grid
    private void initRecyclerView(View rootView) {
        try {
            mRecyclerView = rootView.findViewById(R.id.image_grid_recycler_view);
            imageGridRecyclerAdapter = new ImageGridRecyclerAdapter(getActivity(), mLiveDataOutput);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mRecyclerView.setAdapter(imageGridRecyclerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Unable to initiate grid, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getImagesList() {
        try {
            Observer<Object> mObserver = new Observer<Object>() {
                @Override
                public void onChanged(Object o) {
                    mLiveDataOutput.clear();
                    for (ImageDetails id : (ArrayList<ImageDetails>) o) {
                        mLiveDataOutput.add(id);
                    }
                    imageGridRecyclerAdapter.notifyDataSetChanged();
                }
            };
            imageActivityViewModel.getImagesList().observe(getActivity(), mObserver);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Unable to fetch Images, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

}