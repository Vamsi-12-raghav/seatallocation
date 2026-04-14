package com.example.seatallocation.dto;

public class AdminCredentialResponse {
    private Long adminId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String department;
    private Boolean active;
    private Long createdAt;
    
    public AdminCredentialResponse() {}
    
    public AdminCredentialResponse(Long adminId, String username, String password, String email, 
                                   String fullName, String department, Boolean active, Long createdAt) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.department = department;
        this.active = active;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getAdminId() {
        return adminId;
    }
    
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
    
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
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public Long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
