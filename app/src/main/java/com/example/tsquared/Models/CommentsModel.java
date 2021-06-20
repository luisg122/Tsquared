package com.example.tsquared.Models;

public class CommentsModel {
    private String name = "";
    private String date = "";
    private String comment  = "";
    private String imageUrl = "";
    private String replies  = "";
    private String numberOfUpvotes = "";

    public CommentsModel(){}

    public CommentsModel(String name, String date, String comment){
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

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public String getNumberOfUpvotes() {
        return numberOfUpvotes;
    }

    public void setNumberOfUpvotes(String numberOfUpvotes) {
        this.numberOfUpvotes = numberOfUpvotes;
    }
}
