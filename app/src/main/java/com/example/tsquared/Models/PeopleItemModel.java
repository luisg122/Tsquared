package com.example.tsquared.Models;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

public class PeopleItemModel {
    public  String name;
    public  String desc;
    public  String college;
    public  String email;
    public  Drawable profileImage;

    public static PeopleItemModel fromJson(JSONObject jsonObject) throws JSONException {
        PeopleItemModel people = new PeopleItemModel();
        people.name            = jsonObject.getString("FullName");
        people.desc            = jsonObject.getString("Description");
        people.college         = jsonObject.getString("College");
        people.email           = jsonObject.getString("Email");

        people.name            = capitalizeFirstCharOfEveryWordInString(people.name);
        people.college         = capitalizeFirstCharOfEveryWordInString(people.college);
        return people;
    }

    public String getEmail(){
        return email;
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

    /*PeopleItemModel(String name, String college, String desc, Drawable profileImage){
        this.name    = name;
        this.college = college;
        this.desc    = desc;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
*/
