package com.example.whatsapp.Models;

public class Message {
    String message, messageid;
    String timestamp;
    int userId;

    public Message(int userId, String timestamp, String message) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Message(int userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Message() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
