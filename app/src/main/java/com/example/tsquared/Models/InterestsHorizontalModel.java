package com.example.tsquared.Models;

public class InterestsHorizontalModel {
    private int image;
    private String description;

    public InterestsHorizontalModel() {
    }

    public InterestsHorizontalModel(int image, String description){
        this.image = image;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
