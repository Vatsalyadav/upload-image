package com.vatsalyadav.uploadimage.model;

import java.io.Serializable;

public class ImageDetails implements Serializable {

    private String imageName;
    private String imageUrl;

    public ImageDetails(String imageName, String imageUrl) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    public ImageDetails() {
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
