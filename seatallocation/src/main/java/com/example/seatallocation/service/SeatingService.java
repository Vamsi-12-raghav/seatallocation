package com.example.seatallocation.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.stereotype.Service;

import com.example.seatallocation.dto.SeatingView;
import com.example.seatallocation.entity.ExamHall;
import com.example.seatallocation.entity.Seating;
import com.example.seatallocation.entity.Student;
import com.example.seatallocation.repository.ExamHallRepository;
import com.example.seatallocation.repository.SeatingRepository;
import com.example.seatallocation.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class SeatingService {

    private final StudentRepository studentRepository;
    private final ExamHallRepository examHallRepository;
    private final SeatingRepository seatingRepository;

    public SeatingService(
            StudentRepository studentRepository,
            ExamHallRepository examHallRepository,
            SeatingRepository seatingRepository) {
        this.studentRepository = studentRepository;
        this.examHallRepository = examHallRepository;
        this.seatingRepository = seatingRepository;
    }

    @Transactional
    public List<Seating> allocateSeating() {
        List<Student> students = new ArrayList<>(studentRepository.findAll());
        students.sort((a, b) -> compareRollNo(a.getRollNo(), b.getRollNo()));

        List<ExamHall> halls = new ArrayList<>(examHallRepository.findAll());
        halls.sort((a, b) -> compareHallNumber(a.getHallNumber(), b.getHallNumber()));

        int totalCapacity = halls.stream().mapToInt(ExamHall::getCapacity).sum();
        if (students.size() > totalCapacity) {
            throw new IllegalStateException("Not enough hall capacity for all students.");
        }

        List<Student> ordered = alternateDepartments(students);

        seatingRepository.deleteAll();
        List<Seating> saved = new ArrayList<>();

        int studentIndex = 0;
        for (ExamHall hall : halls) {
            for (int seatIndex = 1; seatIndex <= hall.getCapacity(); seatIndex++) {
                if (studentIndex >= ordered.size()) {
                    break;
                }
                Student student = ordered.get(studentIndex++);
                Seating seating = new Seating();
                seating.setHallNumber(hall.getHallNumber());
                seating.setSeatNumber(hall.getHallNumber() + "-S" + seatIndex);
                seating.setRollNo(student.getRollNo());
                saved.add(seating);
            }
        }

        return seatingRepository.saveAll(saved);
    }

    public List<SeatingView> getSeatingViews() {
        List<Seating> seatingList = new ArrayList<>(seatingRepository.findAll());
        seatingList.sort((a, b) -> {
            int hallCompare = compareHallNumber(a.getHallNumber(), b.getHallNumber());
            if (hallCompare != 0) {
                return hallCompare;
            }
            return Integer.compare(extractSeatIndex(a.getSeatNumber()), extractSeatIndex(b.getSeatNumber()));
        });
        Map<String, String> departmentByRoll = new HashMap<>();
        for (Student student : studentRepository.findAll()) {
            departmentByRoll.put(student.getRollNo(), student.getDepartment());
        }

        List<SeatingView> views = new ArrayList<>();
        for (Seating seating : seatingList) {
            String dept = departmentByRoll.getOrDefault(seating.getRollNo(), "");
            views.add(new SeatingView(
                    seating.getHallNumber(),
                    seating.getSeatNumber(),
                    seating.getRollNo(),
                    dept));
        }
        return views;
    }

    public SeatingView getSeatingForRoll(String rollNo) {
        Seating seating = seatingRepository.findByRollNo(rollNo).orElse(null);
        if (seating == null) {
            return null;
        }
        String department = studentRepository.findByRollNo(rollNo)
                .map(Student::getDepartment)
                .orElse("");
        return new SeatingView(
                seating.getHallNumber(),
                seating.getSeatNumber(),
                seating.getRollNo(),
                department);
    }

    private List<Student> alternateDepartments(List<Student> students) {
        Map<String, Deque<Student>> byDepartment = new HashMap<>();
        for (Student student : students) {
            byDepartment
                    .computeIfAbsent(student.getDepartment(), key -> new ArrayDeque<>())
                    .add(student);
        }

        PriorityQueue<DeptBucket> heap = new PriorityQueue<>(Comparator.comparingInt(DeptBucket::getRemaining).reversed());
        for (Map.Entry<String, Deque<Student>> entry : byDepartment.entrySet()) {
            heap.offer(new DeptBucket(entry.getKey(), entry.getValue()));
        }

        List<Student> ordered = new ArrayList<>();
        String lastDepartment = null;

        // Greedy alternation: pick the department with most remaining students, avoiding the last dept when possible.
        while (!heap.isEmpty()) {
            DeptBucket first = heap.poll();
            if (lastDepartment != null && first.getDepartment().equals(lastDepartment) && !heap.isEmpty()) {
                DeptBucket second = heap.poll();
                ordered.add(second.pop());
                lastDepartment = second.getDepartment();
                if (second.getRemaining() > 0) {
                    heap.offer(second);
                }
                heap.offer(first);
            } else {
                ordered.add(first.pop());
                lastDepartment = first.getDepartment();
                if (first.getRemaining() > 0) {
                    heap.offer(first);
                }
            }
        }

        return ordered;
    }

    private int compareRollNo(String left, String right) {
        Integer leftNum = parseInt(left);
        Integer rightNum = parseInt(right);
        if (leftNum != null && rightNum != null) {
            return Integer.compare(leftNum, rightNum);
        }
        return left.compareToIgnoreCase(right);
    }

    private int compareHallNumber(String left, String right) {
        Integer leftNum = parseInt(left);
        Integer rightNum = parseInt(right);
        if (leftNum != null && rightNum != null) {
            return Integer.compare(leftNum, rightNum);
        }
        return left.compareToIgnoreCase(right);
    }

    private Integer parseInt(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value.replaceAll("\\D", ""));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private int extractSeatIndex(String seatNumber) {
        Integer index = parseInt(seatNumber);
        return index == null ? Integer.MAX_VALUE : index;
    }

    private static class DeptBucket {
        private final String department;
        private final Deque<Student> queue;

        private DeptBucket(String department, Deque<Student> queue) {
            this.department = department;
            this.queue = queue;
        }

        private String getDepartment() {
            return department;
        }

        private int getRemaining() {
            return queue.size();
        }

        private Student pop() {
            return queue.pollFirst();
        }
    }
}
