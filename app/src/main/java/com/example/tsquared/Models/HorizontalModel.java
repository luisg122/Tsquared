package com.example.tsquared.Models;

public class HorizontalModel {
    private int image;
    private String description;

    public HorizontalModel() {
    }

    public HorizontalModel(int image, String description) {
        this.image        = image;
        this.description  = description;
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