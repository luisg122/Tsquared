package com.example.tsquared.Models;

public class SliderImage {
    private String imageUrl = "";

    public SliderImage(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getImageURL() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
