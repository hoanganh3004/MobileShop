package com.library.MoileShop.entity;

import java.sql.Timestamp;

public class Notification {
    private int id;
    private String user_code;
    private String fullName; //
    private String message;
    private Timestamp createdAt;
    private boolean isRead;

    public Notification() {
    }

    public Notification(int id, String user_code, String fullName, String message, Timestamp createdAt, boolean isRead) {
        this.id = id;
        this.user_code = user_code;
        this.fullName = fullName;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserCode() {
        return user_code;
    }

    public void setUserCode(String user_code) {
        this.user_code = user_code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
