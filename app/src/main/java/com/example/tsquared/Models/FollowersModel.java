package com.example.tsquared.Models;

import com.example.tsquared.Activities.Followers;

public class FollowersModel {
    private String name = "";
    private String profileImageUrl = "";
    private String followersNum = "";
    private Boolean isFollowing;

    public FollowersModel(String name, String profileImageUrl, String followersNum, Boolean isFollowing){
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.followersNum = followersNum;
        this.isFollowing = isFollowing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getFollowersNum() {
        return followersNum;
    }

    public void setFollowersNum(String followersNum) {
        this.followersNum = followersNum;
    }

    public Boolean getFollowing() {
        return isFollowing;
    }

    public void setFollowing(Boolean following) {
        isFollowing = following;
    }
}
