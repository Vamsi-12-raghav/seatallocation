package com.example.seatallocation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.seatallocation.dto.ExamHallRequest;
import com.example.seatallocation.entity.ExamHall;
import com.example.seatallocation.repository.ExamHallRepository;

@Service
public class ExamHallService {

    private final ExamHallRepository examHallRepository;

    public ExamHallService(ExamHallRepository examHallRepository) {
        this.examHallRepository = examHallRepository;
    }

    public ExamHall addHall(ExamHallRequest request) {
        ExamHall hall = new ExamHall();
        hall.setHallNumber(request.getHallNumber());
        hall.setCapacity(request.getCapacity());
        return examHallRepository.save(hall);
    }

    public List<ExamHall> getAllHalls() {
        return examHallRepository.findAll();
    }
}
