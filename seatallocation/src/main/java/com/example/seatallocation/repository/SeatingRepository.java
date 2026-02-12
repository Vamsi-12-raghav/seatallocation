package com.example.seatallocation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seatallocation.entity.Seating;

public interface SeatingRepository extends JpaRepository<Seating, Long> {
	Optional<Seating> findByRollNo(String rollNo);
}
