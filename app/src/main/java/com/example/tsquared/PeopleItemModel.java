package com.example.tsquared;

public class PeopleItemModel {
    private String name;
    private String desc;
    private String college;
    private int profileImage;

    PeopleItemModel(String name, String college, String desc, int profileImage){
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

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
}
