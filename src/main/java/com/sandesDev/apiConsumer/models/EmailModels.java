package com.sandesDev.apiConsumer.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "email_tb")
public class EmailModels {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;
    private String username;
    private String email;
    private String message;

    public EmailModels() {
    }

    public EmailModels(UUID userId, String username, String email, String message) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.message = message;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
