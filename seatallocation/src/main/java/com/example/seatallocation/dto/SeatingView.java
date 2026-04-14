package com.example.seatallocation.dto;

public class SeatingView {
    private String hallNumber;
    private String seatNumber;
    private String rollNo;
    private String department;

    public SeatingView(String hallNumber, String seatNumber, String rollNo, String department) {
        this.hallNumber = hallNumber;
        this.seatNumber = seatNumber;
        this.rollNo = rollNo;
        this.department = department;
    }

    public String getHallNumber() {
        return hallNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getDepartment() {
        return department;
    }
}
