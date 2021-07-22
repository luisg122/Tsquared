package com.example.tsquared.Models;

public class CommentsRepliesModel {
    private String name = "";
    private String date = "";
    private String comment  = "";
    private String imageUrl = "";
    private String numberOfUpvotes = "";

    public CommentsRepliesModel(String name, String date, String comment){
        this.name    = name;
        this.date    = date;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNumberOfUpvotes() {
        return numberOfUpvotes;
    }

    public void setNumberOfUpvotes(String numberOfUpvotes) {
        this.numberOfUpvotes = numberOfUpvotes;
    }
}
