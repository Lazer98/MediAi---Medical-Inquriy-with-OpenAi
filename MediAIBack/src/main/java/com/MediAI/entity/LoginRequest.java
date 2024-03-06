package com.MediAI.entity;

public class LoginRequest {
    private String email;
    private String passwordOrDocId;


    // Constructors, getters, and setters

    public LoginRequest(String email, String passwordOrDocId) {
        this.email = email;
        this.passwordOrDocId = passwordOrDocId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordOrDocId() {
        return passwordOrDocId;
    }

    public void setPasswordOrDocId(String passwordOrDocId) {
        this.passwordOrDocId = passwordOrDocId;
    }
}
