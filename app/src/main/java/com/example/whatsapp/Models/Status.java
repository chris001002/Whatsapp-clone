package com.example.whatsapp.Models;

public class Status {
    private int statusID;
    private String userId;
    private String message;
    private String image;
    private String timestamp;

    public Status(int statusID, String userId, String message, String image, String timestamp) {
        this.statusID = statusID;
        this.userId = userId;
        this.message = message;
        this.image = image;
        this.timestamp = timestamp;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}