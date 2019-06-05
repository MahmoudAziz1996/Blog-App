package com.example.aziz.blogapp.Models;

public class Like {
    private String userID;

    public String getLikeKey() {
        return likeKey;
    }

    public void setLikeKey(String likeKey) {
        this.likeKey = likeKey;
    }

    private String likeKey;


    public Like(String userID,String key) {
        this.userID = userID;
        this.likeKey=key;
    }

    public Like() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
