package com.vatsalyadav.uploadimage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vatsalyadav.uploadimage.R;
import com.vatsalyadav.uploadimage.model.ImageDetails;
import com.vatsalyadav.uploadimage.view.imageViewFragments.ImageDetailFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class ImageGridRecyclerAdapter extends RecyclerView.Adapter<ImageGridRecyclerAdapter.ViewHolder> {

    private List<ImageDetails> mImageDetails = new ArrayList<>();
    private Context mContext;

    public ImageGridRecyclerAdapter(Context context, List<ImageDetails> imageDetails) {
        mImageDetails = imageDetails;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_images_grid, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        try {
            // Circular Progress Drawable to show while Glide loads image
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
            circularProgressDrawable.setStrokeWidth(10f);
            circularProgressDrawable.setCenterRadius(48f);
            circularProgressDrawable.setColorSchemeColors(Color.WHITE);
            circularProgressDrawable.start();

            // Set the image using Glide library
            Glide.with(mContext)
                    .load(mImageDetails.get(position).getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(circularProgressDrawable))
                    .apply(new RequestOptions()
                            .fitCenter())
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_broken_image))
                    .into(viewHolder.gridImage);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Unable to set NASA pictures grid data, please try again later.", Toast.LENGTH_SHORT).show();
        }

        try {
//            final PictureDetailsActivity pictureDetailsActivity = new PictureDetailsActivity();
            viewHolder.gridParentLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                    listener.onImageItemSelected(position);
                    ImageDetailFragment imageDetailFragment = new ImageDetailFragment();

                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    imageDetailFragment.setArguments(args);

                    ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContainer, new ImageDetailFragment())
                            .addToBackStack(null)
                            .commit();
//                    Context context = view.getContext();
//                    Intent intent = pictureDetailsActivity.launchPictureDetails(context, position, mImageDetails);
//                    context.startActivity(intent, shared_element_container.toBundle());
                }
            });
        } catch (Exception e) {
            Log.e("PicturesGridRecycler", "Error on launching PictureDetailsActivity");
            e.printStackTrace();
            Toast.makeText(mContext, "Unable to show selected NASA Picture details, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mImageDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView gridImage;
        private LinearLayout gridParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gridImage = itemView.findViewById(R.id.grid_image);
            gridParentLayout = itemView.findViewById(R.id.parent_layout_pictures_grid_item);
        }
    }
}
