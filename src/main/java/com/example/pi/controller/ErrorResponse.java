package com.example.pi.controller;

public class ErrorResponse {
    private String message;

    // Constructor
    public ErrorResponse(String message) {
        this.message = message;
    }

    // Getter
    public String getMessage() {
        return message;
    }

    // Setter (optional)
    public void setMessage(String message) {
        this.message = message;
    }
}
