package com.example.seatallocation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.seatallocation.dto.ExamHallRequest;
import com.example.seatallocation.entity.ExamHall;
import com.example.seatallocation.service.ExamHallService;

@RestController
@RequestMapping("/api/halls")
public class ExamHallController {

    private final ExamHallService examHallService;

    public ExamHallController(ExamHallService examHallService) {
        this.examHallService = examHallService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExamHall addHall(@RequestBody ExamHallRequest request) {
        return examHallService.addHall(request);
    }

    @GetMapping
    public List<ExamHall> getHalls() {
        return examHallService.getAllHalls();
    }
}
