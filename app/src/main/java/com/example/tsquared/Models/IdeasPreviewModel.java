package com.example.tsquared.Models;

import java.util.ArrayList;

public class IdeasPreviewModel {
    public String name        = "";
    public String imageUrl    = "";
    public String newsImage   = "";
    public String newsTitle   = "";
    public String sourceTitle = "";
    public ArrayList<IdeasItemModel> ideas;

    public IdeasPreviewModel(){}

    public IdeasPreviewModel(String name, String imageUrl, String newsImage, String newsTitle, String sourceTitle){
        this.name        = name;
        this.imageUrl    = imageUrl;
        this.newsImage   = newsImage;
        this.newsTitle   = newsTitle;
        this.sourceTitle = sourceTitle;
    }

    public String getName() {
        return name;
    }

    public ArrayList<IdeasItemModel> getIdeas() {
        return ideas;
    }

    public void setIdeas(ArrayList<IdeasItemModel> ideas) {
        this.ideas = ideas;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(String sourceUrl) {
        this.sourceTitle = sourceUrl;
    }
}