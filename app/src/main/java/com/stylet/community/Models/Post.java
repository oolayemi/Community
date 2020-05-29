package com.stylet.community.Models;

import com.google.firebase.database.ServerValue;

public class Post {

    private String postKey;
    private String username;
    private String title;
    private String description;
    private String picture;
    private String userid;
    private String userphoto;
    private Object timestamp;

    public Post(String username, String title, String description, String picture, String userid, String userphoto) {
        this.username = username;
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userid = userid;
        this.userphoto = userphoto;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public Post() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
