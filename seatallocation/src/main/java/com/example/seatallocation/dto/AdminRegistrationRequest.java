package com.example.seatallocation.dto;

public class AdminRegistrationRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String department;
    
    public AdminRegistrationRequest() {}
    
    public AdminRegistrationRequest(String username, String password, String email, String fullName, String department) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.department = department;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
}
