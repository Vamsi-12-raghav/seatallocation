package com.example.seatallocation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.seatallocation.dto.StudentRequest;
import com.example.seatallocation.entity.Student;
import com.example.seatallocation.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(StudentRequest request) {
        Student student = new Student();
        student.setRollNo(request.getRollNo());
        student.setName(request.getName());
        student.setDepartment(request.getDepartment());
        student.setSubject(request.getSubject());
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
