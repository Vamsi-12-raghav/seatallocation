package com.example.seatallocation.dto;

public class ChatRequest {
    private String message;
    private String rollNo;

    public ChatRequest() {
    }

    public ChatRequest(String message, String rollNo) {
        this.message = message;
        this.rollNo = rollNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
}
