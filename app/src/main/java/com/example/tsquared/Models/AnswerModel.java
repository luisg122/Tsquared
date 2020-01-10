package com.example.tsquared.Models;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

public class AnswerModel {
    public int      isAnonymous;
    public String   name;
    public String   dateAnswered;
    public String   answer;
    public Drawable profileImage;

    AnswerModel(){

    }
    public static AnswerModel fromJson(JSONObject jsonObject) throws JSONException {
        AnswerModel answer   = new AnswerModel();
        answer.isAnonymous   = jsonObject.getInt("isAnonymous");
        answer.name          = jsonObject.getString("RepliedBy");
        answer.answer        = jsonObject.getString("Text");
        answer.dateAnswered  = jsonObject.getString("DateReplied");

        if(answer.isAnonymous == 1){
            answer.name = "Anonymous";
        }
        answer.name  = capitalizeFirstCharOfEveryWordInString(answer.name);
        return answer;
    }

    public Drawable getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Drawable profileImage) {
        this.profileImage = profileImage;
    }

    private static String capitalizeFirstCharOfEveryWordInString(String string){
        char[] ch = string.toCharArray();
        for(int i = 0; i < string.length(); i++) {
            // Find first char of a word
            // Make sure the character does not equal a space
            if (i == 0 && ch[i] != ' ' || ch[i] != ' ' && ch[i - 1] == ' ') {
                // If such character is lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {
                    // simply convert it into upper-case
                    // refer to the ASCII table to understand this line of code
                    ch[i] = (char) (ch[i] - 'a' + 'A');
                }
            }
            else if (ch[i] >= 'A' && ch[i] <= 'Z'){
                ch[i] = (char) (ch[i] + 'a' - 'A');
            }
        }
        return new String(ch);
    }

}

    /*AnswerModel(String name, String dateAnswered, String answer, Drawable profileImage){
        this.name = name;
        this.dateAnswered = dateAnswered;
        this.answer = answer;
        this.profileImage = profileImage;
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

    public Drawable getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Drawable profileImage) {
        this.profileImage = profileImage;
    }*/
