package com.example.whatsapp.Models;

public class Message {
    String userId, message, messageid;
    Long timestamp;

    public Message(String userId, Long timestamp, String message) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Message(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Message() {
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

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
