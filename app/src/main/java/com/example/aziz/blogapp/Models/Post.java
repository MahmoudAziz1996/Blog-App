package com.example.aziz.blogapp.Models;

import com.google.firebase.database.ServerValue;

public class Post {


    private String postKey;
    private String description;
    private String picture;
    private String userId;
    private String userName;
    private String userPhoto;
    private Object timeStamp ;


    public Post(String description, String picture, String userId,String userName, String userPhoto) {

        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.userName=userName;
        this.userPhoto = userPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

   // make sure to have an empty constructor inside ur model class
    public Post() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }


    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }



    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
