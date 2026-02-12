package com.example.seatallocation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seatallocation.entity.ExamHall;

public interface ExamHallRepository extends JpaRepository<ExamHall, Long> {
    Optional<ExamHall> findByHallNumber(String hallNumber);
}
