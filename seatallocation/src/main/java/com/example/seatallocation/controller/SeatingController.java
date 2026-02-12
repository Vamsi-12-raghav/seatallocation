package com.example.seatallocation.controller;

import com.example.seatallocation.dto.SeatingView;
import com.example.seatallocation.entity.Seating;
import com.example.seatallocation.service.AdminAuthService;
import com.example.seatallocation.service.SeatingService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/seating")
public class SeatingController {

    private final SeatingService seatingService;
    private final AdminAuthService adminAuthService;

    public SeatingController(SeatingService seatingService, AdminAuthService adminAuthService) {
        this.seatingService = seatingService;
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/allocate")
    public List<Seating> allocate(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        if (!adminAuthService.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin login required");
        }
        return seatingService.allocateSeating();
    }

    @GetMapping
    public List<SeatingView> getSeating() {
        return seatingService.getSeatingViews();
    }

    @GetMapping("/lookup")
    public SeatingView lookup(@RequestParam("rollNo") String rollNo) {
        SeatingView seating = seatingService.getSeatingForRoll(rollNo);
        if (seating == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No allocation found");
        }
        return seating;
    }
}
