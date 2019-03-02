package com.example.entryapp;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class User {

    @SerializedName("userId")
    private UUID userId;
    @SerializedName("userCreds")
    private String userCreds;

    public User(String userCreds) {
        this.userCreds = userCreds;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserCreds() {
        return userCreds;
    }

    public void setUserCreds(String userCreds) {
        this.userCreds = userCreds;
    }
}
