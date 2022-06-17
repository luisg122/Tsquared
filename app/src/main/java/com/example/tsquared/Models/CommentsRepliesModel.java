package com.example.tsquared.Models;

public class CommentsRepliesModel {
    private String name = "";
    private String date = "";
    private String comment  = "";
    private String imageUrl = "";
    private int numberOfUpVotes = 0;
    public boolean  isCommentUpVoted;
    public boolean  isCommentDownVoted;

    public CommentsRepliesModel(String name, String date, String comment, int numberOfUpVotes){
        this.name    = name;
        this.date    = date;
        this.comment = comment;
        this.numberOfUpVotes = numberOfUpVotes;
    }

    public boolean isUpVoted(){
        return isCommentUpVoted;
    }

    public void setUpVoted(boolean isCommentUpVoted){
        this.isCommentUpVoted = isCommentUpVoted;
    }

    public boolean isDownVoted(){
        return isCommentDownVoted;
    }

    public void setDownVoted(boolean isCommentDownVoted){
        this.isCommentDownVoted = isCommentDownVoted;
    }

    public void incrementNumberOfVotes(){
        numberOfUpVotes++;
    }

    public void decrementNumberOfVotes(){
        numberOfUpVotes--;
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

    public int getNumberOfUpVotes() {
        return numberOfUpVotes;
    }

    public void setNumberOfUpVotes(int numberOfUpVotes) {
        this.numberOfUpVotes = numberOfUpVotes;
    }
}
