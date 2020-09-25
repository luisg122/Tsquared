package com.example.tsquared.Models;

public class MoreInterestsModel {
    private int interestsImage;
    private String interestsName;

    public MoreInterestsModel(){

    }

    public MoreInterestsModel(int interestsImage, String interestsName){
        this.interestsImage = interestsImage;
        this.interestsName  = interestsName;
    }

    public int getInterestsImage() {
        return interestsImage;
    }

    public void setInterestsImage(int interestsImage) {
        this.interestsImage = interestsImage;
    }

    public String getInterestsName() {
        return interestsName;
    }

    public void setInterestsName(String interestsName) {
        this.interestsName = interestsName;
    }
}
