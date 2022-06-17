package com.example.tsquared.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class AnswerWithImages {
    public boolean isTextExpanded;
    public boolean isAnswerUpVoted;
    public boolean isAnswerDownVoted;
    public boolean isFollowing;

    public int isAnonymous;
    public int numberOfVotes;
    public String name;
    public String dateAnswered;
    public String answer;
    public String profileImage;

    public String[] imageUrls;

    public AnswerWithImages() {
    }

    public AnswerWithImages(String name, String answer, String dateAnswered, String[] imageUrls, int numberOfVotes) {
        this.name = name;
        this.answer = answer;
        this.dateAnswered = dateAnswered;
        this.imageUrls = imageUrls;
        this.numberOfVotes  = numberOfVotes;

        isTextExpanded = false;
        isAnswerUpVoted = false;
        isAnswerDownVoted = false;
    }

    public boolean isTextExpanded() {
        return isTextExpanded;
    }

    public void setTextExpanded(boolean textCollapsed) {
        isTextExpanded = textCollapsed;
    }

    public boolean isUpVoted() {
        return isAnswerUpVoted;
    }

    public void setUpVoted(boolean isAnswerUpVoted) {
        this.isAnswerUpVoted = isAnswerUpVoted;
    }

    public boolean isDownVoted() {
        return isAnswerDownVoted;
    }

    public void setDownVoted(boolean isAnswerDownVoted) {
        this.isAnswerDownVoted = isAnswerDownVoted;
    }

    public void setFollowing(boolean isFollowing){
        this.isFollowing = isFollowing;
    }

    public boolean isFollowing(){
        return isFollowing;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String[] getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void incrementNumberOfVotes(){
        numberOfVotes++;
    }

    public void decrementNumberOfVotes(){
        numberOfVotes--;
    }

    public void setIsAnonymous(int isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public boolean isAnswerAnonymous(int isAnonymous){
        if(isAnonymous == 1) return true;

        return false;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public int getNumberOfVotes(){
        return numberOfVotes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAnswered() {
        return dateAnswered;
    }

    public void setDateAnswered(String dateAnswered) {
        this.dateAnswered = dateAnswered;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public static AnswerModel fromJson(JSONObject jsonObject) throws JSONException {
        AnswerModel answer = new AnswerModel();
        answer.isAnonymous = jsonObject.getInt("isAnonymous");
        answer.name = jsonObject.getString("RepliedBy");
        answer.answer = jsonObject.getString("Text");
        answer.dateAnswered = jsonObject.getString("DateReplied");

        // must make a server call to determine if answer has been up-voted or down-voted
        // must make a server call to determine the total number of votes an answer has
        // must make a server call to retrieve  the images associated with answer

        if (answer.isAnonymous == 1) {
            answer.name = "Anonymous";
        }

        return answer;
    }
}